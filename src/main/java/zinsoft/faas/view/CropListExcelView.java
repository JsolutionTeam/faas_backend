package zinsoft.faas.view;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.faas.dto.CropDto;

public class CropListExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        //Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<CropDto> list = (List<CropDto>) model.get("list");
        HSSFCell cell = null;
        int row = 0, col = 0;

        sheet.setColumnWidth(col++, (20 * 256)); //대분류
        sheet.setColumnWidth(col++, (25 * 256)); //품목
        sheet.setColumnWidth(col++, (25 * 256)); //작업목록
        sheet.setColumnWidth(col++, (10 * 256)); //재배유형
        sheet.setColumnWidth(col++, (9 * 256)); // 내용연수
        sheet.setColumnWidth(col++, (13 * 256)); //손익분기년도

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;

        /*
        String val = cond.get("keyword");
        if (val != null && !val.equals("")) {
            String fieldName = null;

            switch (cond.get("field")) {
            case "cropACdNm":
                fieldName = "작목 대분류";
                break;
            case "cropBCdNm":
                fieldName = "작목 소분류";
                break;
            case "cropCCdNm":
                fieldName = "작목";
                break;
            }

            setText(row++, 0, fieldName + " : " + val, summaryStyle);
        }
        */

        row++;

        col = 0;
        setText(row, col++, "대분류", headerStyle);
        setText(row, col++, "품목", headerStyle);
        setText(row, col++, "작업목록", headerStyle);
        setText(row, col++, "재배유형", headerStyle);
        setText(row, col++, "내용연수", headerStyle);
        setText(row, col++, "손익분기년도", headerStyle);

        if (list.size() > 0) {
            String activityTCd ="";
            String activityTCdNm ="";
            for (CropDto vo : list) {
                row++;
                col = 0;

                setText(row, col++, vo.getCropACdNm(), dataCStyle);
                setText(row, col++, vo.getExprNm(), dataCStyle);

                activityTCd = (vo.getActivityTCd() != null ? vo.getActivityTCd() : "").toString() ;
                activityTCdNm = (vo.getActivityTCdNm() != null ? vo.getActivityTCdNm() : activityTCd);

                setText(row, col++, !Objects.equals(activityTCdNm, "") ? "작업목록_"+ activityTCdNm : "", dataCStyle);
                setText(row, col++, vo.getCropSCdNm(), dataCStyle);
                setNumber(row, col++, vo.getSerLife());
                setNumber(row, col++, vo.getMaLife());
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
