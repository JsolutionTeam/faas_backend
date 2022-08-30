package zinsoft.faas.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.faas.dto.ActivityDto;

public class ActivityExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        List<ActivityDto> list = (List<ActivityDto>) model.get("list");
        HSSFCell cell = null;
        int row = 0, col = 0;

        sheet.setColumnWidth(col++, (40 * 256));
        //sheet.setColumnWidth(col++, (10 * 256));
        sheet.setColumnWidth(col++, (10 * 256));

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;

        col = 0;
        setText(row, col++, "영농활동명", headerStyle);
        //setText(row, col++, "아이콘", headerStyle);
        setText(row, col++, "표출여부", headerStyle);

        if (list.size() > 0) {
            for (ActivityDto dto : list) {
                row++;
                col = 0;

                setText(row, col++, dto.getActNm(), dataLStyle);
          //      setText(row, col++, dto.getMarkTCdNm() != null ? dto.getMarkTCdNm() : "", dataLStyle);
                setText(row, col++, dto.getExprYn(), dataLStyle);

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
