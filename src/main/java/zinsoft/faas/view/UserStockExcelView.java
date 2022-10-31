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

import zinsoft.faas.dto.UserChemicalStockDto;
import zinsoft.faas.dto.UserManureStockDto;
import zinsoft.util.UserInfoUtil;

public class UserStockExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserChemicalStockDto> list1 = (List<UserChemicalStockDto>) model.get("chemicalList");
        List<UserManureStockDto> list2 = (List<UserManureStockDto>) model.get("manureList");
        HSSFCell cell = null;
        int row = 0, col = 0;
        boolean isManager = (UserInfoUtil.isAdmin() || UserInfoUtil.isManager()) ? true : false;
        boolean isAllTerm = false;

        String stDt = cond.get("stDt");
        String edDt = cond.get("edDt");
        if(StringUtils.isBlank(stDt) && StringUtils.isBlank(edDt)) {
            isAllTerm = true;
        }

        sheet.getPrintSetup().setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setAutobreaks(true);

        HSSFPrintSetup pts = sheet.getPrintSetup();
        pts.setFitWidth((short) 1);
        pts.setFitHeight((short) 0);

        if(isManager) {
            sheet.setColumnWidth(col++, (35 * 256)); // ID
            sheet.setColumnWidth(col++, (15 * 256)); // 이름
        }
        sheet.setColumnWidth(col++, (11 * 256)); //일
        sheet.setColumnWidth(col++, (15 * 256)); //구분
        sheet.setColumnWidth(col++, (20 * 256)); //용도
        sheet.setColumnWidth(col++, (20 * 256)); //구분
        sheet.setColumnWidth(col++, (20 * 256)); //농약명 or 비료명
        // sheet.setColumnWidth(col++, (20 * 256)); // 비료상세
        sheet.setColumnWidth(col++, (10 * 256)); //수량
        sheet.setColumnWidth(col++, (15 * 256)); //구입금액
        if(isAllTerm) {
            sheet.setColumnWidth(col++, (10 * 256)); //잔여량
        }
        sheet.setColumnWidth(col++, (30 * 256)); //증감이유

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;
        if(StringUtils.isNoneBlank(stDt) && StringUtils.isNoneBlank(edDt)) {
            setText(row++, 0, "기간 : " + getFullDate(stDt) +" ~ "+ getFullDate(edDt), summaryStyle);
        } else {
            setText(row++, 0, "기간 : 전체", summaryStyle);
        }

        String val = null;
        val = cond.get("keyword");
        if (val != null && !val.equals("")) {
            setText(row++, 0, "농약명/비료명 : " + val, summaryStyle);
        }

        row++;
        col = 0;
        setText(row, col++, "농약재고", headerStyle);

        row++;
        col = 0;
        if(isManager) {
            setText(row, col++, "아이디", headerStyle);
            setText(row, col++, "이름", headerStyle);
        }
        setText(row, col++, "일자", headerStyle);
        setText(row, col++, "구분", headerStyle);
        setText(row, col++, "용도", headerStyle);
        setText(row, col, "농약명", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row, col, col+1));
        col += 2;
        setText(row, col++, "수량", headerStyle);
        setText(row, col++, "구입금액", headerStyle);
        if(isAllTerm) {
            setText(row, col++, "잔여량", headerStyle);
        }
        setText(row, col++, "증감이유", headerStyle);

        if (list1.size() > 0) {
            for (UserChemicalStockDto dto : list1) {
                row++;
                col = 0;
                Long amt = (dto.getAmt() != null) ? dto.getAmt(): 0;
                if(isManager) {
                    setText(row, col++, dto.getUserId(), dataCStyle);
                    setText(row, col++, dto.getUserNm(), dataCStyle);
                }
                setText(row, col++, getFullDate(dto.getInoutDt()), dataCStyle);
                setText(row, col++, dto.getSupInoutCdNm(), dataCStyle);
                setText(row, col++, dto.getChemicalTCdNm(), dataCStyle);
                setText(row, col, dto.getUserChemicalNm(), dataCStyle);
                setText(row, col+1, "", dataCStyle);
                sheet.addMergedRegion(new CellRangeAddress(row, row, col, col+1));
                col += 2;
                setText(row, col++, comma(dto.getQuan())+dto.getPackTCdNm(), dataCStyle);
                setNumber(row, col++, amt);
                if(isAllTerm) {
                    setText(row, col++, (dto.getRemainingQuan() != null) ? comma(dto.getRemainingQuan())+dto.getPackTCdNm(): "", dataCStyle);
                }
                setText(row, col++, dto.getRemark(), dataCStyle);
            }
        } else {
            row++;
            setText(row, 0, "검색 결과 없음", dataCStyle);

            for (int i = 1; i < col; i++) {
                setText(row, i, "", dataCStyle);
            }

            sheet.addMergedRegion(new CellRangeAddress(row, row, 0, col - 1));
        }

        row += 3;
        col = 0;
        setText(row, col++, "비료재고", headerStyle);

        row++;
        col = 0;
        if(isManager) {
            setText(row, col++, "아이디", headerStyle);
            setText(row, col++, "사용자명", headerStyle);
        }
        setText(row, col++, "일자", headerStyle);
        setText(row, col++, "구분", headerStyle);
        setText(row, col++, "용도", headerStyle);
        setText(row, col++, "비료명", headerStyle);
        setText(row, col++, "비료상세", headerStyle);
        setText(row, col++, "수량", headerStyle);
        setText(row, col++, "구입금액", headerStyle);
        if(isAllTerm) {
            setText(row, col++, "잔여량", headerStyle);
        }
        setText(row, col++, "증감이유", headerStyle);

        if (list1.size() > 0) {
            for (UserManureStockDto dto : list2) {
                row++;
                col = 0;
                Long amt = (dto.getAmt() != null) ? dto.getAmt(): 0;
                if(isManager) {
                    setText(row, col++, dto.getUserId(), dataCStyle);
                    setText(row, col++, dto.getUserNm(), dataCStyle);
                }
                setText(row, col++, getFullDate(dto.getInoutDt()), dataCStyle);
                setText(row, col++, dto.getSupInoutCdNm(), dataCStyle);
                setText(row, col++, "양액", dataCStyle);
                setText(row, col++, dto.getManureNm(), dataCStyle);
                setText(row, col++, dto.getManureTCdNm2(), dataCStyle);
                setText(row, col++, comma(dto.getQuan())+dto.getPackTCdNm(), dataCStyle);
                setNumber(row, col++, amt);
                if(isAllTerm) {
                    setText(row, col++, (dto.getRemainingQuan() != null) ? comma(dto.getRemainingQuan())+dto.getPackTCdNm(): "", dataCStyle);
                }
                setText(row, col++, dto.getRemark(), dataCStyle);
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
