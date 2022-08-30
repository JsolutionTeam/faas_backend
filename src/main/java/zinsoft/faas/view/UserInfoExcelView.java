package zinsoft.faas.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.web.common.dto.UserInfoDto;

public class UserInfoExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserInfoDto> list = (List<UserInfoDto>) model.get("list");
        HSSFCell cell = null;
        int row = 0, col = 0;

        sheet.getPrintSetup().setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setAutobreaks(true);

        sheet.setColumnWidth(col++, (8 * 256));
        sheet.setColumnWidth(col++, (30 * 256));
        sheet.setColumnWidth(col++, (22 * 256));
        sheet.setColumnWidth(col++, (60 * 256));
        sheet.setColumnWidth(col++, (28 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (20 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        // 주작물, 주작물1, 주작물2
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (15 * 256));

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;

        String val = cond.get("keyword");
        if (val != null && !val.equals("")) {
            String fieldName = null;

            switch (cond.get("field")) {
            case "userNm":
                fieldName = "성명";
                break;
            case "userId":
                fieldName = "아이디";
                break;
            case "roleNm":
                fieldName = "그룹";
                break;
            case "mobileNum":
                fieldName = "전화번호";
                break;
            case "cropId":
                fieldName = "품목";
                break;
            }

            setText(row++, 0, fieldName + " : " + val, summaryStyle);
        }

        row++;

        col = 0;
        setText(row, col++, "성명", headerStyle);
        setText(row, col++, "아이디", headerStyle);
        setText(row, col++, "농장(회사)명", headerStyle);
        setText(row, col++, "주소", headerStyle);
        setText(row, col++, "이메일", headerStyle);
        setText(row, col++, "휴대폰", headerStyle);
        setText(row, col++, "그룹", headerStyle);
        setText(row, col++, "가입일", headerStyle);
        setText(row, col++, "작업일지", headerStyle);
        setText(row, col++, "전표", headerStyle);
        setText(row, col++, "주 작물", headerStyle);
        setText(row, col++, "주 작물1", headerStyle);
        setText(row, col++, "주 작물2", headerStyle);

        if (list.size() > 0) {
            String addr = null;
            for (UserInfoDto dto : list) {
                row++;
                col = 0;
                addr = "";

                if (dto.getAddr1() != null && !dto.getAddr1().isEmpty()) {
                    addr += dto.getAddr1() + " ";
                }
                if (dto.getAddr2() != null && !dto.getAddr2().isEmpty()) {
                    addr += dto.getAddr2() + " ";
                }

                setText(row, col++, dto.getUserNm(), dataCStyle);
                setText(row, col++, dto.getUserId(), dataLStyle);
                setText(row, col++, dto.getCompanyNm(), dataLStyle);
                setText(row, col++, addr, dataLStyle);
                setText(row, col++, dto.getEmailAddr(), dataLStyle);
                setText(row, col++, dto.getMobileNum(), dataCStyle);
                setText(row, col++, dto.getRoleNm(), dataCStyle);
                setText(row, col++, dto.getRegDtm() != null? formatDate(dto.getRegDtm(), "yyyy-MM-dd"): "", dataCStyle);
                setDt(row, col++, dto.getUserActivityActDt(), dataCStyle);
                setDt(row, col++, dto.getSlipTrdDt(), dataCStyle);
                setText(row, col++, dto.getCropNm(), dataCStyle);
                setText(row, col++, dto.getCropNm1(), dataCStyle);
                setText(row, col++, dto.getCropNm2(), dataCStyle);
            }
        } else {
            row++;
            setText(row, 0, "검색 결과 없음", dataCStyle);

            for (int i = 1; i < col; i++) {
                setText(row, i, "", dataCStyle);
            }

            sheet.addMergedRegion(new CellRangeAddress(row, row, 0, col - 1));
        }
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat toFormat = new SimpleDateFormat(format);
        return toFormat.format(date);
    }

}
