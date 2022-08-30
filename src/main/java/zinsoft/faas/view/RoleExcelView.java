package zinsoft.faas.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.web.common.dto.RoleDto;

public class RoleExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<RoleDto> list = (List<RoleDto>) model.get("list");
        HSSFCell cell = null;
        int row = 0, col = 0;

        sheet.setColumnWidth(col++, (18 * 256));
        sheet.setColumnWidth(col++, (30 * 256));

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;

        String val = cond.get("keyword");
        if (val != null && !val.equals("")) {
            String fieldName = null;

            switch (cond.get("field")) {
            case "roleId":
                fieldName = "그룹ID";
                break;
            case "roleNm":
                fieldName = "그룹명";
                break;
            }

            setText(row++, 0, fieldName + " : " + val, summaryStyle);
        }

        row++;

        col = 0;
        setText(row, col++, "그룹ID", headerStyle);
        setText(row, col++, "그룹명", headerStyle);

        if (list.size() > 0) {
            for (RoleDto dto : list) {
                row++;
                col = 0;

                setText(row, col++, dto.getRoleId().substring(5), dataCStyle);
                setText(row, col++, dto.getRoleNm(), dataLStyle);
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
