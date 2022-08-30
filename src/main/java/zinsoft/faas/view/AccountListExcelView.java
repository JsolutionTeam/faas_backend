package zinsoft.faas.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.faas.dto.AccountDto;

public class AccountListExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<AccountDto> list = (List<AccountDto>) model.get("list");
        HSSFCell cell = null;
        int row = 0, col = 0;

        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (22 * 256));
        sheet.setColumnWidth(col++, (17 * 256));
        sheet.setColumnWidth(col++, (22 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (10 * 256));
        sheet.setColumnWidth(col++, (10 * 256));

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;

        String val = cond.get("keyword");
        if (val != null && !val.equals("")) {
            String fieldName = null;

            switch (cond.get("field")) {
            case "acId":
                fieldName = "계정코드";
                break;
            case "acNm":
                fieldName = "계정과목명";
                break;
            case "costTCdNm":
                fieldName = "비용구분";
                break;
            }

            setText(row++, 0, fieldName + " : " + val, summaryStyle);
        }

        row++;

        col = 0;
        setText(row, col++, "계정과목코드", headerStyle);
        setText(row, col++, "계정과목명", headerStyle);
        setText(row, col++, "상위계정과목코드", headerStyle);
        setText(row, col++, "상위계정과목명", headerStyle);
        setText(row, col++, "비용/수익구분", headerStyle);
        setText(row, col++, "입력가능여부", headerStyle);
        setText(row, col++, "비용구분", headerStyle);
        setText(row, col++, "표출여부", headerStyle);
        setText(row, col++, "표출순번", headerStyle);

        if (list.size() > 0) {

            for (AccountDto vo : list) {
                row++;
                col = 0;

                setText(row, col++, vo.getAcId(), dataCStyle);
                setText(row, col++, vo.getAcNm(), dataLStyle);
                setText(row, col++, vo.getUpAcId(), dataCStyle);
                setText(row, col++, vo.getUpAcNm(), dataCStyle);
                if(vo.getCdTCdNm().equals("차변")) {
                    setText(row, col++, "비용", dataCStyle);
                }else {
                    setText(row, col++, "수익", dataCStyle);
                }

                setText(row, col++, vo.getInputYn(), dataCStyle);
                setText(row, col++, vo.getCostTCdNm(), dataCStyle);
                setText(row, col++, vo.getExprYn(), dataCStyle);
                setText(row, col++, vo.getExprSeq()+"", dataCStyle);

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
