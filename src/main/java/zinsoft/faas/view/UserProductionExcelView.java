package zinsoft.faas.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.faas.dto.UserProductionDto;
import zinsoft.util.UserInfoUtil;

public class UserProductionExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserProductionDto> list = (List<UserProductionDto>) model.get("list");
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

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;
        String stDt = cond.get("stDt");
        String edDt = cond.get("edDt");
        if(StringUtils.isNoneBlank(stDt) && StringUtils.isNoneBlank(edDt)) {
            setText(row++, 0, "기간 : " + stDt+" ~ "+ edDt, summaryStyle);
        }

        String fieldName = null;
        String val = null;
        val = cond.get("planTCdNm");
        if (val != null && !val.equals("")) {
            fieldName = "구분";
            setText(row++, 0, fieldName + " : " + val, summaryStyle);
        }
        row++;
        col = 0;

        if(isManager) {
            setText(row, col++, "아이디", headerStyle);
            setText(row, col++, "사용자명", headerStyle);
        }
        setText(row, col++, "구분", headerStyle);
        setText(row, col++, "일자", headerStyle);
        setText(row, col++, "등급", headerStyle);
        setText(row, col++, "계획량", headerStyle);
        setText(row, col++, "생산량", headerStyle);
        setText(row, col++, "생산율", headerStyle);
        setText(row, col++, "비고", headerStyle);

        if (list.size() > 0) {
            for (UserProductionDto dto : list) {
                row++;
                col = 0;

                if(isManager) {
                    setText(row, col++, dto.getUserId(), dataCStyle);
                    setText(row, col++, dto.getUserNm(), dataCStyle);
                }
                setText(row, col++, dto.getPlanTCdNm(), dataCStyle);
                setText(row, col++, getFullDate(dto.getPrdDt()), dataCStyle);
                setText(row, col++, dto.getGradeTCdNm(), dataCStyle);
                if(dto.getPlanTCdNm().startsWith("계획")) {
                    setText(row, col++, comma(dto.getQuan())+(dto.getPackTCdNm() != null ? dto.getPackTCdNm() : ""), dataCStyle);
                    setText(row, col++, "", dataCStyle);
                }else {
                    setText(row, col++, "", dataCStyle);
                    setText(row, col++, comma(dto.getQuan())+(dto.getPackTCdNm() != null ? dto.getPackTCdNm() : ""), dataCStyle);
                }

                String rate = (dto.getPrdRate() != null) ? comma(dto.getPrdRate())+"%" : "";
                setText(row, col++, rate , dataCStyle);
                setText(row, col++, dto.getRemark() , dataCStyle);
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
