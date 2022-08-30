package zinsoft.faas.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractView;

import zinsoft.util.AppPropertyUtil;
import zinsoft.util.CommonUtil;
import zinsoft.util.Constants;

public abstract class AbstractExcelView extends AbstractView {

    protected static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";
    protected static final String EXTENSION_XLS = ".xls";
    protected static final String EXTENSION_XLSX = ".xlsx";
    protected static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    protected static final String OUTPUT_TYPE_EXCEL = "xls";
    protected static final String CONTENT_TYPE_PDF = "application/pdf";
    protected static final String EXTENSION_PDF = ".pdf";
    protected static final String OUTPUT_TYPE_PDF = "pdf";
    private String url;
    private String outputType;
    private String outputExtension;
    private String workingDir;

    protected HSSFSheet sheet;
    protected HSSFCellStyle titleStyle;
    protected HSSFCellStyle subTitleStyle;
    protected HSSFCellStyle summaryStyle;
    protected HSSFCellStyle headerStyle;
    protected HSSFCellStyle dataLStyle;
    protected HSSFCellStyle dataLVTStyle;
    protected HSSFCellStyle dataCStyle;
    protected HSSFCellStyle dataRStyle;
    protected HSSFCellStyle dataNStyle;
    protected HSSFCellStyle dataFStyle;
    protected HSSFCellStyle cellBStyle;
    protected HSSFCellStyle cellRStyle;
    protected HSSFCellStyle cellRBStyle;

    public AbstractExcelView() {
        setContentType(CONTENT_TYPE_XLS);
        this.workingDir = AppPropertyUtil.get(Constants.UPLOAD_DIR) + "xls_to_pdf";
        new File(workingDir).mkdirs();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HSSFWorkbook workbook;
        if (this.url != null) {
            workbook = getTemplateSource(this.url, request);
        } else {
            workbook = new HSSFWorkbook();
            logger.debug("Created Excel Workbook from scratch");
        }

        this.outputType = (String) model.get("outputType");

        if (outputType == null) {
            outputType = OUTPUT_TYPE_EXCEL;
        } else {
            outputType = outputType.toLowerCase();
        }

        if (!outputType.equals(OUTPUT_TYPE_EXCEL) && !outputType.equals(OUTPUT_TYPE_PDF)) {
            throw new IllegalArgumentException("Output type is not supported: " + outputType);
        }

        this.outputExtension = "." + outputType;
        buildExcelDocument(model, workbook, request, response);

        ServletOutputStream out = response.getOutputStream();
        String isMadeTemplate = response.getHeader("isMadeTemplate"); // "true" or Null

        if (outputType.equals(OUTPUT_TYPE_EXCEL)) {
            if (isMadeTemplate == null) {
                workbook.write(out);
                out.flush();
            } else {
                String basepath = workingDir + "/" + request.getSession().getId();
                String excelFilename = basepath + EXTENSION_XLSX;
                File excelFile = new File(excelFilename);
                FileInputStream fis = null;

                setContentType(CONTENT_TYPE_XLSX);

                try {
                    fis = new FileInputStream(excelFile);
                    FileCopyUtils.copy(fis, out);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (Exception e) {
                        }
                    }
                }

                out.flush();

                if (excelFile != null) {
                    try {
                        excelFile.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            String basepath = workingDir + "/" + request.getSession().getId();
            String excelFilename = basepath + (isMadeTemplate == null ? EXTENSION_XLS : EXTENSION_XLSX);
            String pdfFilename = basepath + EXTENSION_PDF;
            File excelFile = new File(excelFilename);
            FileOutputStream fos = null;

            if (isMadeTemplate == null) {
                fos = new FileOutputStream(excelFile);
                workbook.write(fos);
                fos.flush();
            }

            String xlsToPdf = AppPropertyUtil.get(Constants.XLS_TO_PDF);
            xlsToPdf = xlsToPdf.replaceAll("\\[in\\]", excelFilename);
            xlsToPdf = xlsToPdf.replaceAll("\\[out\\]", pdfFilename);
            xlsToPdf = xlsToPdf.replaceAll("\\[outdir\\]", workingDir);

            DefaultExecutor executor = new DefaultExecutor();
            CommandLine cmdLine = CommandLine.parse(xlsToPdf);
            /*
            String xlsToPdfParam = SiteConfig.XLS_TO_PDF_PARAM;
            xlsToPdfParam = xlsToPdfParam.replaceAll("\\[in\\]", excelFilename);
            xlsToPdfParam = xlsToPdfParam.replaceAll("\\[out\\]", pdfFilename);
            xlsToPdfParam = xlsToPdfParam.replaceAll("\\[outdir\\]", workingDir);
            String[] xlsToPdfParams = xlsToPdfParam.split(" ");
            for (int i = 0, cnt = xlsToPdfParams.length; i < cnt; i++) {
                cmdLine.addArgument(xlsToPdfParams[i]);
            }
            */
            executor.execute(cmdLine);

            File pdfFile = new File(pdfFilename);
            FileInputStream fis = null;

            setContentType(CONTENT_TYPE_PDF);

            try {
                fis = new FileInputStream(pdfFile);
                FileCopyUtils.copy(fis, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Exception e) {
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e) {
                    }
                }
            }

            out.flush();

            if (excelFile != null) {
                try {
                    excelFile.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pdfFile != null) {
                try {
                    pdfFile.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected HSSFWorkbook getTemplateSource(String url, HttpServletRequest request) throws Exception {
        LocalizedResourceHelper helper = new LocalizedResourceHelper(getApplicationContext());
        Locale userLocale = RequestContextUtils.getLocale(request);
        Resource inputFile = helper.findLocalizedResource(url, EXTENSION_XLS, userLocale);

        if (logger.isDebugEnabled()) {
            logger.debug("Loading Excel workbook from " + inputFile);
        }
        POIFSFileSystem fs = new POIFSFileSystem(inputFile.getInputStream());
        return new HSSFWorkbook(fs);
    }

    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HSSFFont font = null;
        String sheetName = (String) model.get("sheetName");

        sheet = wb.createSheet(sheetName);

        HSSFPrintSetup hps = sheet.getPrintSetup();
        hps.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); // 용지 크기 설정
        hps.setLandscape(false); // 프린트 출력 방향, true:가로, false:세로

        font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 15);
        font.setFontName("돋움");
        titleStyle = wb.createCellStyle();
        titleStyle.setFont(font);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 11);
        font.setFontName("돋움");
        subTitleStyle = wb.createCellStyle();
        subTitleStyle.setFont(font);
        subTitleStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        subTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        font.setFontName("돋움");
        summaryStyle = wb.createCellStyle();
        summaryStyle.setFont(font);
        summaryStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints((short) 10);
        font.setFontName("돋움");
        font.setColor(HSSFColor.WHITE.index);
        headerStyle = wb.createCellStyle();
        headerStyle.setFont(font);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setWrapText(true);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setFillBackgroundColor(HSSFColor.GREEN.index);
        headerStyle.setFillForegroundColor(HSSFColor.GREEN.index);

        font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("돋움");
        dataLStyle = wb.createCellStyle();
        dataLStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        dataLStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        dataLStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataLStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataLStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dataLStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataLStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
        dataLStyle.setWrapText(true);

        font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("돋움");
        dataLVTStyle = wb.createCellStyle();
        dataLVTStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        dataLVTStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        dataLVTStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataLVTStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataLVTStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dataLVTStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataLVTStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
        dataLVTStyle.setWrapText(true);

        font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("돋움");
        dataCStyle = wb.createCellStyle();
        dataCStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        dataCStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        dataCStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataCStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataCStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dataCStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataCStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
        dataCStyle.setWrapText(true);

        font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("돋움");
        dataRStyle = wb.createCellStyle();
        dataRStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        dataRStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        dataRStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataRStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataRStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dataRStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataRStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("@"));
        dataRStyle.setWrapText(true);

        font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("돋움");
        dataNStyle = wb.createCellStyle();
        dataNStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        dataNStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        dataNStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataNStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataNStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dataNStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataNStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        dataNStyle.setWrapText(true);

        font = wb.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("돋움");
        dataFStyle = wb.createCellStyle();
        dataFStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        dataFStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        dataFStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataFStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataFStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dataFStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataFStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.#"));
        dataFStyle.setWrapText(true);

        cellBStyle = wb.createCellStyle();
        cellBStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        cellRStyle = wb.createCellStyle();
        cellRStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

        cellRBStyle = wb.createCellStyle();
        cellRBStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellRBStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        build(model, wb, request, response);

        String isMadeTemplate = response.getHeader("isMadeTemplate"); // "true" or Null
        String fileName = CommonUtil.urlEncode(sheetName) + "_" + CommonUtil.getToday() + (isMadeTemplate != null && outputType.equals(OUTPUT_TYPE_EXCEL) ? EXTENSION_XLSX : outputExtension);
        String browser = getBrowser(request);

        if (browser.equals("MSIE")) {
            fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < fileName.length(); i++) {
                char c = fileName.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            fileName = sb.toString();
        } else if (browser.equals("Opera")) {
            fileName = new String(fileName.getBytes("UTF-8"), "8859_1");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "8859_1");
        }

        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    }

    @SuppressWarnings("deprecation")
    protected HSSFCell getCell(HSSFSheet sheet, int row, int col) {
        HSSFRow sheetRow = sheet.getRow(row);
        if (sheetRow == null) {
            sheetRow = sheet.createRow(row);
        }
        HSSFCell cell = sheetRow.getCell((short) col);
        if (cell == null) {
            cell = sheetRow.createCell((short) col);
        }
        return cell;
    }

    protected void setText(HSSFCell cell, String text) {
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(text);
    }

    protected void setText(int row, int col, String text, HSSFCellStyle style) {
        HSSFCell cell = getCell(sheet, row, col);
        cell.setCellStyle(style);
        setText(cell, text);
    }

    protected void setText(int row, int col, String text, HSSFCellStyle style, HSSFSheet sheet) {
        HSSFCell cell = getCell(sheet, row, col);
        cell.setCellStyle(style);
        setText(cell, text);
    }

    protected void setNumber(int row, int col, Object num) {
        if (num == null) {
            setText(row, col, "", dataRStyle);
            return;
        }

        if (num instanceof Number) {
            double n = ((Number) num).doubleValue();

            if (n == 0) {
                setText(row, col, "", dataRStyle);
            } else {
                HSSFCell cell = getCell(sheet, row, col);
                cell.setCellStyle((num instanceof Float || num instanceof Double) ? dataFStyle : dataNStyle);
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(n);
            }
        } else {
            setText(row, col, num.toString(), dataRStyle);
        }
    }

    protected void setNumber2(int row, int col, Object num) {
        if (num == null) {
            setText(row, col, "", dataRStyle);
            return;
        }

        if (num instanceof Number) {
            float n = ((Number) num).floatValue();

            if (n == 0) {
                setText(row, col, "0", dataRStyle);
            } else {
                HSSFCell cell = getCell(sheet, row, col);
                cell.setCellStyle((num instanceof Float || num instanceof Double) ? dataFStyle : dataNStyle);
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(Math.round(n * 10.0) / 10.0);
            }
        } else {
            setText(row, col, num.toString(), dataRStyle);
        }
    }

    protected void setNumber3(int row, int col, Object num) {
        if (num == null) {
            setText(row, col, "", dataRStyle);
            return;
        }

        if (num instanceof Number) {
            double n = ((Number) num).doubleValue();

            if (n == 0) {
                setText(row, col, "0", dataRStyle);
            } else {
                HSSFCell cell = getCell(sheet, row, col);
                cell.setCellStyle((num instanceof Float || num instanceof Double) ? dataFStyle : dataNStyle);
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(n);
            }
        } else {
            setText(row, col, num.toString(), dataRStyle);
        }
    }

    protected void setLong(int row, int col, String val, HSSFCellStyle style) {
        setText(row, col, val, style);
    }

    protected void setDt(int row, int col, String dt, HSSFCellStyle style) {
        String val = dt != null ? dt.substring(0, 4) + "-" + dt.substring(4, 6) + "-" + dt.substring(6, 8) : "";
        setText(row, col, val, style);
    }

    protected String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }
        return "Firefox";
    }

    protected String getDate(String dt) {
        if (dt == null || dt.length() != 8) {
            return "";
        }
        return Integer.toString(Integer.parseInt(dt.substring(6, 8), 10));
    }

    protected String getFullDate(String dt) {
        if (dt == null || dt.length() != 8) {
            return "";
        }
        return dt.substring(0, 4) + "-" + dt.substring(4, 6) + "-" + dt.substring(6, 8);
    }

    protected String leadingZero(String str, String zeros) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        String lz = zeros + str;
        return lz.substring(lz.length() - zeros.length());
    }

    protected String comma(Object num) {
        if (num == null) {
            return "";
        }

        if (num instanceof Number) {
            if(num != null && (num instanceof Double)) {
               NumberFormat formatter = new DecimalFormat("#0.00");
               return formatter.format(num).replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",").replaceAll("\\.0$", "");
            }
            return num.toString().replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",").replaceAll("\\.0$", "");
        } else {
            return num.toString();
        }
    }

    protected String setDashDate(String date) {
        return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
    }

    protected abstract void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response);

}
