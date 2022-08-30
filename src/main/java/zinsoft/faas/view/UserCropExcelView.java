package zinsoft.faas.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.faas.dto.UserCropDto;

public class UserCropExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserCropDto> list = (List<UserCropDto>) model.get("list");
        HSSFCell cell = null;
        int row = 0, col = 0;

        sheet.getPrintSetup().setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setAutobreaks(true);

        HSSFPrintSetup pts = sheet.getPrintSetup();
        pts.setFitWidth((short) 1);
        pts.setFitHeight((short) 0);

        sheet.setColumnWidth(col++, (30 * 256));
        sheet.setColumnWidth(col++, (10 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (10 * 256));
        sheet.setColumnWidth(col++, (10 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (15 * 256));
        sheet.setColumnWidth(col++, (50 * 256));

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
            case "cropId": // TODO
                fieldName = "품목";
                break;
            }

            setText(row++, 0, fieldName + " : " + val, summaryStyle);
        }

        row++;

        col = 0;
        setText(row, col++, "품목", headerStyle);
        setText(row, col++, "사업유형", headerStyle);
        setText(row, col++, "푸목별칭", headerStyle);
        setText(row, col++, "조성년도(년)", headerStyle);
        setText(row, col++, "조성주수(주)", headerStyle);
        setText(row, col++, "면적(m²)", headerStyle);
        setText(row, col++, "주요품종", headerStyle);
        setText(row, col++, "재배유형", headerStyle);
        setText(row, col++, "재배기간", headerStyle);
        setText(row, col++, "비고", headerStyle);

        if (list.size() > 0) {
            String stedCrop = "";

            for (UserCropDto vo : list) {
                row++;
                col = 0;

                if (vo.getStCrop() == null || vo.getEdCrop() == null) {
                    stedCrop = "";
                } else {
                    stedCrop = vo.getStCrop() + "월" + " - " + vo.getEdCrop() + "월";
                }

                setText(row, col++, vo.getCropNm(), dataCStyle);
                setText(row, col++, vo.getPartTCdNm(), dataCStyle);
                setText(row, col++, vo.getAliasNm(), dataCStyle);
                setText(row, col++, vo.getPlantYear() != null?vo.getPlantYear():"" + "", dataCStyle);
                setText(row, col++, vo.getPlantNum() + "", dataCStyle);
                setText(row, col++, vo.getArea() + "", dataCStyle);
                if(vo.getMainKind() != null && vo.getMainKind().isEmpty() == false) {
                    setText(row, col++, vo.getMainKind(), dataCStyle);
                } else {
                    setText(row, col++, "", dataCStyle);
                }
                
                if(vo.getCropSCdNm() != null && vo.getCropSCdNm().isEmpty() == false) {
                    setText(row, col++, vo.getCropSCdNm(), dataCStyle);
                } else {
                    setText(row, col++, "", dataCStyle);
                }
                
                setText(row, col++, stedCrop, dataCStyle);
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
