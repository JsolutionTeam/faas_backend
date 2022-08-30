package zinsoft.util;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.WhitespaceRule;
import org.springframework.web.multipart.MultipartFile;

import zinsoft.faas.vo.ExcelData;
import zinsoft.web.common.dto.UserInfoDto;

public class CommonUtil {

    private static final String DATE_FORMAT = "yyyyMMdd";
    private static PasswordValidator pwdValidator = null;

    static {
        // 8~32자리
        LengthRule r1 = new LengthRule(8, 32);

        // 대문자, 소문자, 숫자, 특수문자 중 3가지 이상 조합
        CharacterCharacteristicsRule r2 = new CharacterCharacteristicsRule();
        r2.setNumberOfCharacteristics(3);
        r2.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        r2.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        r2.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 1));
        r2.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));

        // 영문, 숫자, 쿼티 키보드 연속 문자열 안됨
        WhitespaceRule r3 = new WhitespaceRule();
        IllegalSequenceRule r4 = new IllegalSequenceRule(EnglishSequenceData.Alphabetical);
        IllegalSequenceRule r5 = new IllegalSequenceRule(EnglishSequenceData.Numerical);
        IllegalSequenceRule r6 = new IllegalSequenceRule(EnglishSequenceData.USQwerty);

        pwdValidator = new PasswordValidator(Arrays.asList(r1, r2, r3, r4, r5, r6));
    }

    private CommonUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * @param str
     * @param algorithm
     *            MD5, SHA-256...
     * @return
     */
    public static final String hash(String str, String algorithm, byte[] salt, String digestEnc) {
        if (str == null) {
            return null;
        }

        try {
            MessageDigest di = MessageDigest.getInstance(algorithm);
            if (salt != null && salt.length > 0) {
                di.update(salt);
            }
            byte[] digest = di.digest(str.getBytes());

            if ("BASE64".equals(digestEnc)) {
                return new String(Base64.encodeBase64(digest));
            } else {
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < digest.length; i++) {
                    sb.append(String.format("%02x", 0xff & (char) digest[i]));
                }

                return sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static final String md5(String str) {
        return CommonUtil.hash(str, "MD5", null, null);
    }

    public static final String sha256(String str) {
        return CommonUtil.sha256(str, "kl@#$jigt4G");
    }

    public static final String sha256(String str, String salt) {
        return CommonUtil.hash(str, "SHA-256", salt.getBytes(), null);
    }

    public static final String sha256(String str, String salt, String digestEnc) {
        return CommonUtil.hash(str, "SHA-256", salt.getBytes(), digestEnc);
    }

    public static final String mysqlPassword(String str) throws NoSuchAlgorithmException {
        byte[] bpara = new byte[str.length()];
        byte[] rethash;
        int i;
        int cnt;

        for (i = 0, cnt = str.length(); i < cnt; i++) {
            bpara[i] = (byte) (str.charAt(i) & 0xff);
        }

        MessageDigest sha1er = MessageDigest.getInstance("SHA1");
        rethash = sha1er.digest(bpara); // stage1
        rethash = sha1er.digest(rethash); // stage2

        StringBuilder r = new StringBuilder(41);
        r.append("*");

        for (i = 0, cnt = rethash.length; i < cnt; i++) {
            String x = Integer.toHexString(rethash[i] & 0xff).toUpperCase();
            if (x.length() < 2) {
                r.append("0");
            }
            r.append(x);
        }

        return r.toString();
    }

    public static final String mysqlOldPassword(String str) {
        byte[] bpara = new byte[str.length()];
        long lvar1 = 1345345333;
        long ladd = 7;
        long lvar2 = 0x12345671;
        int i;
        int cnt;

        if (str.length() <= 0) {
            return "";
        }

        for (i = 0, cnt = str.length(); i < cnt; i++) {
            bpara[i] = (byte) (str.charAt(i) & 0xff);
        }

        for (i = 0, cnt = str.length(); i < cnt; i++) {
            if (bpara[i] == ' ' || bpara[i] == '\t') {
                continue;
            }
            lvar1 ^= (((lvar1 & 63) + ladd) * bpara[i]) + (lvar1 << 8);
            lvar2 += (lvar2 << 8) ^ lvar1;
            ladd += bpara[i];
        }

        lvar1 = lvar1 & 0x7fffffff;
        lvar2 = lvar2 & 0x7fffffff;
        StringBuilder r = new StringBuilder(16);
        String x = Long.toHexString(lvar1);

        for (i = 8, cnt = x.length(); i > cnt; i--) {
            r.append("0");
        }
        r.append(x);

        x = Long.toHexString(lvar2);
        for (i = 8, cnt = x.length(); i > cnt; i--) {
            r.append("0");
        }
        r.append(x);

        return r.toString();
    }

    /**
     * 비밀번호 유효성 검사
     *
     * @param pwd
     *            검사할 비밀번호
     * @return
     */
    public static boolean isValidPassword(String pwd) {
        return pwdValidator.validate(new PasswordData(pwd)).isValid();
    }

    /**
     * URL encode
     *
     * @param text
     *            문자열
     * @param enc
     *            Charset
     * @return 변환된 문자열
     */
    public static String urlEncode(String text, String enc) {
        String encText = null;

        try {
            encText = URLEncoder.encode(text, enc);
        } catch (Exception e) {
            encText = text;
        }

        return encText;
    }

    public static String urlEncode(String text) {
        return urlEncode(text, "utf-8");
    }

    public static String getFileExt(String filename, boolean withDot) {
        if (filename == null) {
            return "";
        }

        int dotIdx = filename.lastIndexOf(".");
        String dot = withDot ? "." : "";

        return dotIdx > -1 ? (dot + filename.substring(dotIdx + 1)) : "";
    }

    public static void deleteFile(String path) throws IOException {
        Files.delete(FileSystems.getDefault().getPath(path)); // Path.of(path)
    }

    public static boolean existFile(String file) {
        return new File(file).exists();
    }

    public static File getUploadFile() {
        File file = null;

        do {
            String filename = UUID.randomUUID().toString();
            String path = AppPropertyUtil.get(Constants.UPLOAD_DIR) + getUploadFilePath(filename);
            String fullpath = path + File.separator + filename;
            new File(path).mkdirs();
            file = new File(fullpath);
        } while (file.exists());

        return file;
    }

    public static String getUploadFilePath(String filename) {
        return filename.substring(0, 2) + File.separator + filename.substring(2, 4);
    }

    public static String getBaseUri(HttpServletRequest request, boolean withCtxPath) {
        String scheme = request.getScheme();
        int port = request.getServerPort();
        boolean isDefaultPort = (scheme.equalsIgnoreCase("http") && port == 80) || (scheme.equalsIgnoreCase("https") && port == 443);
        String baseUri = null;

        if (isDefaultPort) {
            baseUri = request.getScheme() + "://" + request.getServerName();
        } else {
            baseUri = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        }

        if (withCtxPath) {
            return baseUri + request.getContextPath();
        } else {
            return baseUri;
        }
    }

    public static String getBaseUri(HttpServletRequest request) {
        return getBaseUri(request, true);
    }

    public static String getFrontEndBaseUri(HttpServletRequest request) {
        String baseUri = getBaseUri(request, false);
        String contextPath = request.getContextPath();

        if (contextPath.endsWith("_API")) {
            contextPath = contextPath.substring(0, contextPath.length() - 4);
        } else if (contextPath.endsWith("API")) {
            contextPath = contextPath.substring(0, contextPath.length() - 3);
        }

        return baseUri + contextPath;
    }

    public static String addslashes(String str) {
        if (str == null) {
            return null;
        }

        str = str.replace("\\", "\\\\");
        str = str.replace("\"", "\\\"");
        str = str.replace("'", "\\'");

        return str;
    }

    public static String stripslashes(String str) {
        if (str == null) {
            return null;
        }

        str = str.replace("\\'", "'");
        str = str.replace("\\\"", "\"");
        str = str.replace("\\\\", "\\");

        return str;
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String formatDate(String dtm) {
        return formatDate(dtm, "-", ":");
    }

    public static String formatDate(String dtm, String dtDelim, String tmDelim) {
        if (dtm == null) {
            return "";
        }

        switch (dtm.length()) {
            case 6:
                return dtm.substring(0, 4) + dtDelim + dtm.substring(4, 6);
            case 8:
                return dtm.substring(0, 4) + dtDelim + dtm.substring(4, 6) + dtDelim + dtm.substring(6, 8);
            case 10:
                return dtm.substring(0, 4) + dtDelim + dtm.substring(4, 6) + dtDelim + dtm.substring(6, 8) + " " + dtm.substring(8, 10);
            case 12:
                return dtm.substring(0, 4) + dtDelim + dtm.substring(4, 6) + dtDelim + dtm.substring(6, 8) + " " + dtm.substring(8, 10) + tmDelim + dtm.substring(10, 12);
            case 14:
                return dtm.substring(0, 4) + dtDelim + dtm.substring(4, 6) + dtDelim + dtm.substring(6, 8) + " " + dtm.substring(8, 10) + tmDelim + dtm.substring(10, 12) + tmDelim + dtm.substring(10, 14);
            default:
                return dtm;
        }
    }

    public static String formatDate(Date date, String format, TimeZone timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    public static String formatTime(String tm) {
        return formatTime(tm, ":");
    }

    public static String formatTime(String tm, String tmDelim) {
        if (tm == null) {
            return "";
        }

        switch (tm.length()) {
            case 4:
                return tm.substring(0, 2) + tmDelim + tm.substring(2, 4);
            case 6:
                return tm.substring(0, 2) + tmDelim + tm.substring(2, 4) + tmDelim + tm.substring(4, 6);
            default:
                return tm;
        }
    }

    public static String timestampFormat(long timestamp, String format) { // ms
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(date);
    }

    public static String unixtimestampFormat(int unixTime, String format) { // sec
        return timestampFormat(unixTime * 1000L, format);
    }

    public static int unixtimestamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static String getToday(String format) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(calendar.getTime());
    }

    public static String getToday() {
        return getToday(DATE_FORMAT);
    }

    public static String[] getFirstLastDayOfWeek(String date, String format) throws ParseException {
        String[] ret = new String[2];
        DateFormat df = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();

        cal.setTime(df.parse(date));

        cal.set(Calendar.DAY_OF_WEEK, 1);
        ret[0] = df.format(cal.getTime());

        cal.set(Calendar.DAY_OF_WEEK, 7);
        ret[1] = df.format(cal.getTime());

        return ret;
    }

    public static String[] getFirstLastDayOfMonth(String date, String format) throws ParseException {
        String[] ret = new String[2];
        DateFormat df = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();

        cal.setTime(df.parse(date));

        cal.set(Calendar.DATE, 1);
        ret[0] = df.format(cal.getTime());

        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        ret[1] = df.format(cal.getTime());

        return ret;
    }

    public static String[] getFirstLastDayOfYear(String date, String format) throws ParseException {
        String[] ret = new String[2];
        DateFormat df = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();

        cal.setTime(df.parse(date));

        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 1);
        ret[0] = df.format(cal.getTime());

        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DATE, 31);
        ret[1] = df.format(cal.getTime());

        return ret;
    }

    public static String addDay(Date date, String format, int day) {
        DateFormat df = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.DATE, day);

        return df.format(cal.getTime());
    }

    public static String addDay(String date, String format, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return addDay(sdf.parse(date), format, day);
        } catch (ParseException e) {
            return "";
        }
    }

    public static String addMonth(Date date, String format, int month) {
        DateFormat df = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.MONTH, month);

        return df.format(cal.getTime());
    }

    public static String addMonth(String date, String format, int month) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return addMonth(sdf.parse(date), format, month);
        } catch (ParseException e) {
            return "";
        }
    }

    public static String addYear(Date date, String format, Integer year) {
        DateFormat df = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.YEAR, year);

        return df.format(cal.getTime());
    }

    public static boolean isValidMapItems(Map<String, Object> map, String[] keys) {
        for (String key : keys) {
            Object val = map.get(key);
            if (val == null) {
                return false;
            }

            if (val instanceof String && ((String) val).isEmpty()) {
                return false;
            }
            if (val instanceof Number && ((Number) val).longValue() == 0) {
                return false;
            }
        }

        return true;
    }

    public static int diffOfDate(String begin, String end) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date beginDate = formatter.parse(begin);
        Date endDate = formatter.parse(end);
        long diff = endDate.getTime() - beginDate.getTime();

        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    public static Map<String, Object> createParam(String... params) {
        Map<String, Object> param = new HashMap<>();

        if (params == null || params.length == 0) {
            return param;
        }

        int cnt = params.length;

        if (cnt % 2 == 1) {
            param.put(params[cnt - 1], null);
            cnt--;
        }

        for (int i = 0; i < cnt; i += 2) {
            param.put(params[i], params[i + 1]);
        }

        return param;
    }

    public static int getDayOfWeek(String date) throws ParseException {
        DateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date parseDate = sdf.parse(date);
        Calendar cal = Calendar.getInstance();

        cal.setTime(parseDate);

        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String leadingZero(int n, int len) {
        String mask = "00000000000";
        String ns = Integer.toString(n);

        return mask.substring(0, len - ns.length()) + ns;
    }

    public static ExcelData getJsonFromIOExcel(MultipartFile xls, UserInfoDto farmerInfo) throws EncryptedDocumentException, InvalidFormatException, IOException {
        ExcelData excelData = new ExcelData();
        Workbook wb = WorkbookFactory.create(xls.getInputStream());
        Sheet sheet = wb.getSheetAt(0);
        List<List<String>> data = new ArrayList<List<String>>();
        int rowCnt = 0;
        int colCnt = 0;

        Iterator<Row> rowItr = sheet.iterator();
        while (rowItr.hasNext()) {
            Row row = rowItr.next();
            List<String> cellData = new ArrayList<String>();
            int c = 0;

            for (int i = 0, cnt = row.getLastCellNum(); i < cnt; i++) {
                Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);

                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cellData.add(cell.getStringCellValue());
                        break;

                    case Cell.CELL_TYPE_NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();

                            date = cell.getDateCellValue();
                            String convertDate = sdf.format(date);
                            cellData.add(convertDate);
                        } else {
                            DecimalFormat fmt = new DecimalFormat("###,###");
                            DecimalFormat dfmt = new DecimalFormat("###,###.##");
                            double num = cell.getNumericCellValue();
                            String fStr = "";
                            if ((num - (int) num) != 0) {
                                fStr = dfmt.format(num);
                            } else {
                                fStr = fmt.format(num);
                            }
                            cellData.add(fStr);
                        }
                        break;

                    case Cell.CELL_TYPE_BLANK:
                        cellData.add(cell.getStringCellValue());
                        break;

                    default:
                        break;
                }

                c++;
            }

            if (c > colCnt) {
                colCnt = c;
            }

            data.add(cellData);
            rowCnt++;
        }

        excelData.setRowCnt(rowCnt);
        excelData.setColCnt(colCnt);
        excelData.setData(data);

        return excelData;
    }

}
