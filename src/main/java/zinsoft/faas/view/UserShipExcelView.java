package zinsoft.faas.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.faas.dto.UserShipDto;
import zinsoft.util.UserInfoUtil;

public class UserShipExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserShipDto> list = (List<UserShipDto>) model.get("list");
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
            setText(row++, 0, "?????? : " + stDt+" ~ "+ edDt, summaryStyle);
        }

        String fieldName = null;
        String val = null;
        val = cond.get("planTCdNm");
        if (val != null && !val.equals("")) {
            fieldName = "??????";
            setText(row++, 0, fieldName + " : " + val, summaryStyle);
        }
        row++;
        col = 0;

        if(isManager) {
	        sheet.addMergedRegion(new CellRangeAddress(row, row+1, col, col)); //ID
	        setCellStyle(row, row+1, col, col, headerStyle);
	        col++;
	        sheet.addMergedRegion(new CellRangeAddress(row, row+1, col, col)); //??????
	        setCellStyle(row, row+1, col, col, headerStyle);
	        col++;
        }
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, col, col)); //??????
        setCellStyle(row, row+1, col, col, headerStyle);
        col++;
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, col, col)); //??????
        setCellStyle(row, row+1, col, col, headerStyle);
        col++;
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, col, col)); //??????
        setCellStyle(row, row+1, col, col, headerStyle);
        col++;

        sheet.addMergedRegion(new CellRangeAddress(row, row, col, col+1)); //?????????
        setCellStyle(row, row, col, col+1, headerStyle);
        col+=2;
        sheet.addMergedRegion(new CellRangeAddress(row, row, col, col+1)); //?????????
        setCellStyle(row, row, col, col+1, headerStyle);
        col+=2;

        sheet.addMergedRegion(new CellRangeAddress(row, row+1, col, col)); //?????????
        setCellStyle(row, row+1, col, col, headerStyle);
        col++;
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, col, col)); //??????
        setCellStyle(row, row+1, col, col, headerStyle);
        col++;
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, col, col)); //??????
        setCellStyle(row, row+1, col, col, headerStyle);
        col++;
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, col, col)); //??????
        setCellStyle(row, row+1, col, col, headerStyle);
        col++;
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, col, col)); //????????????
        setCellStyle(row, row+1, col, col, headerStyle);
        col++;
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, col, col)); //?????????
        setCellStyle(row, row+1, col, col, headerStyle);
        col++;
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, col, col)); //??????
        setCellStyle(row, row+1, col, col, headerStyle);

        col = 0;
        if(isManager) {
            setText(row, col++, "?????????", headerStyle);
            setText(row, col++, "????????????", headerStyle);
        }
        setText(row, col++, "??????", headerStyle);
        setText(row, col++, "??????", headerStyle);
        setText(row, col++, "??????", headerStyle);

        int subCol = col;
        setText(row, col, "?????????", headerStyle);
        col+=2;
        setText(row, col, "?????????", headerStyle);
        col+=2;
        setText(row, col++, "?????????", headerStyle);
        setText(row, col++, "????????????(???)", headerStyle);
        setText(row, col++, "????????????(???)", headerStyle);
        setText(row, col++, "??????", headerStyle);
        setText(row, col++, "????????????", headerStyle);
        setText(row, col++, "?????????", headerStyle);
        setText(row, col++, "??????", headerStyle);

        col = subCol;
        row += 1;
        setText(row, col++, "??????", headerStyle);
        setText(row, col++, "????????????", headerStyle);
        setText(row, col++, "??????", headerStyle);
        setText(row, col++, "????????????", headerStyle);

        if (list.size() > 0) {
            for (UserShipDto dto : list) {
                row++;
                col = 0;

                if(isManager) {
                    setText(row, col++, dto.getUserId(), dataCStyle);
                    setText(row, col++, dto.getUserNm(), dataCStyle);
                }
                setText(row, col++, dto.getPlanTCdNm(), dataCStyle);
                setText(row, col++, getFullDate(dto.getShipDt()), dataCStyle);
                setText(row, col++, dto.getGradeTCdNm(), dataCStyle);

                if(dto.getPlanTCdNm().startsWith("??????")) {
                    if(dto.getPlanTCdNm().endsWith("??????")) {
                    	sheet.addMergedRegion(new CellRangeAddress(row, row, col, col+1));
                        cell = getCell(sheet, row, col++);
                        cell.setCellStyle(dataCStyle);
                        
                        if(dto.getUnitPack() > 0) {
                            String unitPack = comma(dto.getUnitPack());
                            String packTCdNm = dto.getPackTCdNm() != null ? dto.getPackTCdNm() : "";
                            setText(cell, unitPack+packTCdNm);
                        }else {
                        	setText(cell, "");
                        }
                        
                        cell = getCell(sheet, row, col++);
                        cell.setCellStyle(dataCStyle);
                        //col += 2;

                        sheet.addMergedRegion(new CellRangeAddress(row, row, col, col+1));
                        cell = getCell(sheet, row, col++);
                        cell.setCellStyle(dataCStyle);
                        setText(cell, "");
                        cell = getCell(sheet, row, col++);
                        cell.setCellStyle(dataCStyle);
                        //col += 2;
                    }else {
                    	if(dto.getUnitPack() > 0) {
                            String unitPack = comma( dto.getUnitPack());
                            String packTCdNm = dto.getPackTCdNm() != null ? dto.getPackTCdNm() : "";
                            setText(row, col++, unitPack+packTCdNm, dataCStyle);
                        }else {
                        	setText(row, col++, "", dataCStyle);
                        }
                        setNumber(row, col++, dto.getQuan());
                        setText(row, col++, "", dataCStyle);
                        setText(row, col++, "", dataCStyle);
                    }
                }else {
                	if(dto.getPlanTCdNm().endsWith("??????")) {
                		sheet.addMergedRegion(new CellRangeAddress(row, row, col, col+1));
                        cell = getCell(sheet, row, col);
                        cell.setCellStyle(dataCStyle);
                        setText(cell, "");
                        col += 2;

                		sheet.addMergedRegion(new CellRangeAddress(row, row, col, col+1));
                        cell = getCell(sheet, row, col);
                        cell.setCellStyle(dataCStyle);
                        if(dto.getUnitPack() > 0) {
                            String unitPack = comma( dto.getUnitPack());
                            String packTCdNm = dto.getPackTCdNm() != null ? dto.getPackTCdNm() : "";
                            setText(cell, unitPack+packTCdNm);
                        }else {
                        	setText(cell, "");
                        }
                        col += 2;
                    }else {
                    	setText(row, col++, "", dataCStyle);
                        setText(row, col++, "", dataCStyle);
                        if(dto.getUnitPack() > 0) {
                            String unitPack = comma( dto.getUnitPack());
                            String packTCdNm = dto.getPackTCdNm() != null ? dto.getPackTCdNm() : "";
                            setText(row, col++, unitPack+packTCdNm, dataCStyle);
                        }else {
                        	setText(row, col++, "", dataCStyle);
                        }


                        setNumber(row, col++, dto.getQuan());
                    }
                }
                String rate = (dto.getShipRate() != null) ? dto.getShipRate()+"%" : "";
                setText(row, col++, rate , dataCStyle);

                setNumber(row, col++, dto.getUnitAmt());
                setNumber(row, col++, dto.getAmt());
                setText(row, col++, dto.getInoutTCdNm(), dataCStyle);

                String rate2 = (dto.getDefRate() != null) ? dto.getDefRate()+"%" : "";
                setText(row, col++, rate2, dataCStyle);
                setText(row, col++, dto.getDestNm(), dataCStyle);
                setText(row, col++, dto.getRemark(), dataCStyle);
            }
        } else {
            row++;
            setText(row, 0, "?????? ?????? ??????", dataCStyle);

            for (int i = 1; i < col; i++) {
                setText(row, i, "", dataCStyle);
            }

            sheet.addMergedRegion(new CellRangeAddress(row, row, 0, col - 1));
        }
    }

    private void setCellStyle(int startRow, int endRow, int startCol, int endCol, HSSFCellStyle cellStyle) {
        HSSFCell cell = null;
        int cntRow = (endRow - startRow);
        int cntCol = (endCol - startCol);
        for (int i = 0; i <= cntRow; i++) {
            int colIdx = startCol;
            for (int j = 0; j <= cntCol; j++) {
                cell = getCell(sheet, startRow, colIdx++);
                cell.setCellStyle(cellStyle);
            }
            startRow++;
        }
    }
}
