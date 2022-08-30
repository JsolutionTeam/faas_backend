package zinsoft.faas.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.faas.dto.SelfLaborCostDto;

public class SelfLaborCostExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<SelfLaborCostDto> list = (List<SelfLaborCostDto>) model.get("list");
        HSSFCell cell = null;
        int row = 0, col = 0;

        sheet.setColumnWidth(col++, (20 * 256));
        sheet.setColumnWidth(col++, (25 * 256));
        sheet.setColumnWidth(col++, (25 * 256));

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;

        String val = cond.get("keyword");
        if (val != null && !val.equals("")) {
            String fieldName = null;

            switch (cond.get("field")) {
            case "year":
                fieldName = "년도";
                break;
            default:
                break;
            }

            setText(row++, 0, fieldName + " : " + val, summaryStyle);
        }

        row++;

        col = 0;
        setText(row, col++, "년도", headerStyle);
        setText(row, col++, "남성금액(시간당단가, 원)", headerStyle);
        setText(row, col++, "여성금액(시간당단가, 원)", headerStyle);

        if (list.size() > 0) {
            for (SelfLaborCostDto vo : list) {
                row++;
                col = 0;

                setText(row, col++, vo.getYear(), dataCStyle);
                setNumber3(row, col++, vo.getManAmt());
                setNumber3(row, col++, vo.getWomanAmt());
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
