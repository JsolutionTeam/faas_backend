package zinsoft.faas.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.faas.dto.UserManureDto;
import zinsoft.util.UserInfoUtil;

public class UserManureExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserManureDto> list = (List<UserManureDto>) model.get("list");
        HSSFCell cell = null;
        int row = 0, col = 0;
        boolean isManager = (UserInfoUtil.isAdmin() || UserInfoUtil.isManager()) ? true : false;

        sheet.getPrintSetup().setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setAutobreaks(true);

        HSSFPrintSetup pts = sheet.getPrintSetup();
        pts.setFitWidth((short) 1);
        pts.setFitHeight((short) 0);

        if(isManager) {
            sheet.setColumnWidth(col++, (20 * 256));//아이디
            sheet.setColumnWidth(col++, (20 * 256));//이름
        }
        sheet.setColumnWidth(col++, (20 * 256)); // 비료명
        sheet.setColumnWidth(col++, (20 * 256)); // 포장단위
        sheet.setColumnWidth(col++, (20 * 256)); // 비료상세
        sheet.setColumnWidth(col++, (30 * 256)); // 복합비료비율
        sheet.setColumnWidth(col++, (20 * 256)); // 제조사
        sheet.setColumnWidth(col++, (50 * 256)); // 비고

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;

        String val = cond.get("keyword");
        if (val != null && !val.equals("")) {
            String fieldName = null;

            switch (cond.get("field")) {
            case "manureNm":
                fieldName = "비료명";
                break;
            case "makerNm":
                fieldName = "제조사";
                break;
            default:
                break;
            }

            setText(row++, 0, fieldName + " : " + val, summaryStyle);
        }

        row++;

        col = 0;
        if(isManager) {
        	setText(row, col++, "아이디", headerStyle);
            setText(row, col++, "이름", headerStyle);
        }
        setText(row, col++, "비료명", headerStyle);
        setText(row, col++, "포장단위", headerStyle);
        setText(row, col++, "비료상세", headerStyle);
        setText(row, col++, "복합비료비율(N-P-K)", headerStyle);
        setText(row, col++, "제조사", headerStyle);
        setText(row, col++, "비고", headerStyle);

        if (list.size() > 0) {
            for (UserManureDto vo : list) {
                row++;
                col = 0;
                if(isManager) {
                	setText(row, col++, vo.getUserId(), dataCStyle);
                    setText(row, col++, vo.getUserNm(), dataCStyle);
                }
                setText(row, col++, vo.getManureNm(), dataCStyle);
                setText(row, col++, vo.getPackTCdNm(), dataCStyle);
                setText(row, col++, vo.getManureTCdNm2(), dataCStyle);

                int cpFerN = (vo.getCpFerN() == null) ?  0 : vo.getCpFerN();
                int cpFerP = (vo.getCpFerP() == null) ?  0 : vo.getCpFerP();
                int cpFerK = (vo.getCpFerK() == null) ?  0 : vo.getCpFerK();
                if(cpFerN == 0 && cpFerP == 0 && cpFerK == 0) {
                	setText(row, col++, "", dataCStyle);
                }else {
	                String cpFer = cpFerN + "%-" + cpFerP + "%-"+cpFerK+"%";
	                setText(row, col++, cpFer, dataCStyle);
                }
                setText(row, col++, vo.getMakerNm(), dataCStyle);
                setText(row, col++, vo.getRemark(), dataCStyle);
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

}
