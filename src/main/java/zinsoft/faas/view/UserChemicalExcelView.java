package zinsoft.faas.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.faas.dto.UserChemicalDto;
import zinsoft.util.UserInfoUtil;

public class UserChemicalExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserChemicalDto> list = (List<UserChemicalDto>) model.get("list");
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
            sheet.setColumnWidth(col++, (20 * 256));
            sheet.setColumnWidth(col++, (20 * 256));
        }
        sheet.setColumnWidth(col++, (20 * 256));
        sheet.setColumnWidth(col++, (20 * 256));
        sheet.setColumnWidth(col++, (20 * 256));
        sheet.setColumnWidth(col++, (20 * 256));
        sheet.setColumnWidth(col++, (20 * 256));
        sheet.setColumnWidth(col++, (20 * 256));
        sheet.setColumnWidth(col++, (20 * 256));
        sheet.setColumnWidth(col++, (20 * 256));

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;

        String val = cond.get("keyword");
        if (val != null && !val.equals("")) {
            String fieldName = null;

            switch (cond.get("field")) {
            case "userChemicalNm":
                fieldName = "농약명";
                break;
            case "insect":
                fieldName = "적용병해충";
                break;
            case "productNm":
                fieldName = "상표명";
                break;
            case "chemicalNm":
                fieldName = "농약품목명";
                break;
            case "makerNm":
                fieldName = "제조사";
                break;
            default:
                fieldName = "";
                break;
            }

            setText(row++, 0, fieldName + " : " + val, summaryStyle);
        }

        row++;

        col = 0;
        if(isManager) {
            setText(row, col++, "아이디", headerStyle);
            setText(row, col++, "사용자명", headerStyle);
        }
        setText(row, col++, "용도", headerStyle);
        setText(row, col++, "농약명", headerStyle);
        setText(row, col++, "포장단위", headerStyle);
        setText(row, col++, "적용병해충", headerStyle);
        setText(row, col++, "상표명", headerStyle);
        setText(row, col++, "농약품목명", headerStyle);
        setText(row, col++, "제조사", headerStyle);
        setText(row, col++, "비고", headerStyle);

        if (list.size() > 0) {
            for (UserChemicalDto vo : list) {
                row++;
                col = 0;
                if(isManager) {
                    setText(row, col++, vo.getUserId(), dataCStyle);
                    setText(row, col++, vo.getUserNm(), dataCStyle);
                }
                setText(row, col++, vo.getChemicalTCdNm(), dataCStyle);
                setText(row, col++, vo.getUserChemicalNm(), dataCStyle);
                setText(row, col++, vo.getPackTCdNm(), dataCStyle);
                setText(row, col++, vo.getInsect(), dataCStyle);
                setText(row, col++, vo.getProductNm(), dataCStyle);
                setText(row, col++, vo.getChemicalNm(), dataCStyle);
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
