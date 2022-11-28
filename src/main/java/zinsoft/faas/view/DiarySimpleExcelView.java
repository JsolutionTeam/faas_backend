package zinsoft.faas.view;

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

import zinsoft.faas.dto.UserChemicalStockDto;
import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserManureStockDto;

public class DiarySimpleExcelView extends AbstractExcelView {

    @SuppressWarnings({"unchecked", "unused"})
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserDiaryDto> list = (List<UserDiaryDto>) model.get("list");
        HSSFCell cell = null;
        int row = 0, col = 0;
        String userNm = "";
        String addr = "";
        String email = "";

        sheet.getPrintSetup().setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setAutobreaks(true);

        HSSFPrintSetup pts = sheet.getPrintSetup();
        pts.setFitWidth((short) 1);
        pts.setFitHeight((short) 0);

        sheet.setColumnWidth(col++, (15 * 256)); //일자
        sheet.setColumnWidth(col++, (5 * 256)); //일지계획

        // 2022 작업단계 분리하면서 생긴 변경사항
        sheet.setColumnWidth(col++, (10 * 256)); //작기명
        sheet.setColumnWidth(col++, (10 * 256)); //작업 대분류
        sheet.setColumnWidth(col++, (10 * 256)); //작업 중분류
        sheet.setColumnWidth(col++, (30 * 256)); //실작업
//        sheet.setColumnWidth(col++, (25 * 256)); //작업단계
//        sheet.setColumnWidth(col++, (30 * 256)); //작업내용


        sheet.setColumnWidth(col++, (15 * 256)); //날씨
        sheet.setColumnWidth(col++, (9 * 256)); //기온
        sheet.setColumnWidth(col++, (9 * 256)); //최저온도
        sheet.setColumnWidth(col++, (9 * 256)); //최고온도
        sheet.setColumnWidth(col++, (9 * 256)); //강수량
        sheet.setColumnWidth(col++, (9 * 256)); //습도
        sheet.setColumnWidth(col++, (7 * 256)); //자가남(명)
        sheet.setColumnWidth(col++, (5 * 256)); //자가남(시간)
        sheet.setColumnWidth(col++, (5 * 256)); //자가남(분)
        sheet.setColumnWidth(col++, (7 * 256)); //자가여(명)
        sheet.setColumnWidth(col++, (5 * 256)); //자가여(시간)
        sheet.setColumnWidth(col++, (5 * 256)); //자가여(분)
        sheet.setColumnWidth(col++, (7 * 256)); //고용남(명)
        sheet.setColumnWidth(col++, (5 * 256)); //고용남(시간)
        sheet.setColumnWidth(col++, (5 * 256)); //고용남(분)
        sheet.setColumnWidth(col++, (7 * 256)); //고용여(명)
        sheet.setColumnWidth(col++, (5 * 256)); //고용여(시간)
        sheet.setColumnWidth(col++, (5 * 256)); //고용여(분)
        sheet.setColumnWidth(col++, (10 * 256)); // 수확량

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;

        if (!list.isEmpty()) {
            userNm = list.get(0).getUserNm();
            setText(row++, 0, "성명 : " + userNm, summaryStyle);
        }

        String sActDt = cond.get("sActDt");
        String eActDt = cond.get("eActDt");
        if ((sActDt != null && sActDt.isEmpty() == false) && (eActDt != null && eActDt.isEmpty() == false)) {
            setText(row++, 0, "기간 : " + getFullDate(sActDt) + "~" + getFullDate(eActDt), summaryStyle);
        } else {
            setText(row++, 0, "기간 : 전체", summaryStyle);
        }

        String actNm = cond.get("actNm");
        if (StringUtils.isNotEmpty(actNm)) {
            setText(row++, 0, "작업단계 : " + (cond.get("actNm") != null ? cond.get("actNm") : "전체"), summaryStyle);
        }

        String searchRemark = cond.get("remark");
        if (StringUtils.isNotEmpty(searchRemark)) {
            setText(row++, 0, "작업내용 : " + cond.get("remark"), summaryStyle);
        }

        row++;
        col = 0;
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //일자
        setCellStyle(row, row + 1, col, col++, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //일지계획
        setCellStyle(row, row + 1, col, col++, headerStyle);


        // 2022 작업단계 분리하면서 생긴 변경사항
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //작기명
        setCellStyle(row, row + 1, col, col++, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //작업 대분류
        setCellStyle(row, row + 1, col, col++, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //작업 중분류
        setCellStyle(row, row + 1, col, col++, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //실작업
        setCellStyle(row, row + 1, col, col++, headerStyle);

//        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //작업단계
//        setCellStyle(row, row + 1, col, col++, headerStyle);
//        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //작업내용
//        setCellStyle(row, row + 1, col, col++, headerStyle);

        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //날씨
        setCellStyle(row, row + 1, col, col++, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //기온
        setCellStyle(row, row + 1, col, col++, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //최고온도
        setCellStyle(row, row + 1, col, col++, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //최저온도
        setCellStyle(row, row + 1, col, col++, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //강수량
        setCellStyle(row, row + 1, col, col++, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col)); //습도
        setCellStyle(row, row + 1, col, col++, headerStyle);

        sheet.addMergedRegion(new CellRangeAddress(row, row, col, col + 2)); //자가(남)
        setCellStyle(row, row, col, col += 2, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row, ++col, col + 2)); //자가(여)
        setCellStyle(row, row, col, col += 2, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row, ++col, col + 2)); //고용(남)
        setCellStyle(row, row, col, col += 2, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row, ++col, col + 2)); //고용(여)
        setCellStyle(row, row, col, col += 2, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, ++col, col)); //수확량
        setCellStyle(row, row + 1, col, col, headerStyle);

        col = 0;
        setText(row, col++, "일자", headerStyle);
        setText(row, col++, "일지\n계획", headerStyle);

        // 2022 작업단계 분리하면서 생긴 변경사항
        setText(row, col++, "작기명", headerStyle);
        setText(row, col++, "작업 대분류", headerStyle);
        setText(row, col++, "작업 중분류", headerStyle);
        setText(row, col++, "실작업", headerStyle);

        setText(row, col++, "날씨", headerStyle);
        setText(row, col++, "기온", headerStyle);
        setText(row, col++, "최저온도", headerStyle);
        setText(row, col++, "최고온도", headerStyle);
        setText(row, col++, "강수량", headerStyle);
        setText(row, col++, "습도(%)", headerStyle);
        setText(row, col, "자가(남)", headerStyle);
        setText(row, col += 3, "자가(여)", headerStyle);
        setText(row, col += 3, "고용(남)", headerStyle);
        setText(row, col += 3, "고용(여)", headerStyle);
        setText(row++, col += 3, "수확량", headerStyle);

        col = 10;

        setText(row, col++, "인력수", headerStyle);
        setText(row, col++, "시간", headerStyle);
        setText(row, col++, "분", headerStyle);
        setText(row, col++, "인력수", headerStyle);
        setText(row, col++, "시간", headerStyle);
        setText(row, col++, "분", headerStyle);
        setText(row, col++, "인력수", headerStyle);
        setText(row, col++, "시간", headerStyle);
        setText(row, col++, "분", headerStyle);
        setText(row, col++, "인력수", headerStyle);
        setText(row, col++, "시간", headerStyle);
        setText(row, col++, "분", headerStyle);
        col++;

        if (list.size() > 0) {
            for (UserDiaryDto vo : list) {
                String remark = vo.getRemark();
                row++;
                col = 0;

                if (vo.getChemicalList() != null && vo.getChemicalList().size() > 0) {
                    UserChemicalStockDto chemicalStockDto = vo.getChemicalList().get(0);
                    String userChemicalNm = chemicalStockDto.getUserChemicalNm();
                    String chemicalTCdNm = chemicalStockDto.getChemicalTCdNm();
                    Double quan = chemicalStockDto.getQuan();
                    String packTCdNm = chemicalStockDto.getPackTCdNm();
                    remark += (StringUtils.isNotBlank(remark) ? "\n\r" : "");
                    remark += (userChemicalNm + "(" + chemicalTCdNm + ") - " + quan + packTCdNm);
                }

                if (vo.getManureList() != null && vo.getManureList().size() > 0) {
                    UserManureStockDto manureStockDto = vo.getManureList().get(0);
                    String manureNm = manureStockDto.getManureNm();
                    String manureTCdNm2 = manureStockDto.getManureTCdNm2();
                    Long cpFerN = manureStockDto.getCpFerN();
                    Long cpFerP = manureStockDto.getCpFerP();
                    Long cpFerK = manureStockDto.getCpFerK();
                    Double quan = manureStockDto.getQuan();
                    String packTCdNm = manureStockDto.getPackTCdNm();
                    String cpFertilizer = "";
                    if (manureStockDto.getManureTCd2() != null && manureStockDto.getManureTCd2().equals("1")) {
                        cpFertilizer = (cpFerN != null ? cpFerN : "0") + "-" + (cpFerP != null ? cpFerP : "0") + "-" + (cpFerK != null ? cpFerK : "0");
                    }
                    remark += (StringUtils.isNotBlank(remark) ? "\n\r" : "");
                    remark += ((StringUtils.isNotBlank(manureNm) ? manureNm + (StringUtils.isNotBlank(cpFertilizer) ? "[" + cpFertilizer + "]" : "") + " - " : "") + quan + packTCdNm);
                }

                setText(row, col++, setDashDate(vo.getActDt()), dataCStyle);
                setText(row, col++, vo.getDiaryTCdNm(), dataCStyle);
//                if(vo.getActCnt() > 1) {
//                    setText(row, col++, vo.getFmwrkCdNm() + " " + vo.getInning() + "차", dataCStyle);
//                } else {
//                    setText(row, col++, vo.getFmwrkCdNm(), dataCStyle);
//                }
                // 작업단계 분리하면서 생긴 변경사항
                setText(row, col++, vo.getCropCdNm(), dataCStyle); // 작기명
                setText(row, col++, vo.getGrowStepNm(), dataCStyle); // 작업 중분류
                setText(row, col++, vo.getFmwrkCdNm(), dataCStyle); // 작업 중분류

                // 실작업
                setText(row, col++, remark != null ? (remark).replace(System.getProperty("line.separator"), " ") : "", dataLStyle);

                setText(row, col++, vo.getSkyTCdNm(), dataCStyle);
                setNumber2(row, col++, vo.getTemp() != null && vo.getTemp() != 0 ? vo.getTemp() : 0);
                setNumber2(row, col++, vo.getTmn() != null && vo.getTmn() != 0 ? vo.getTmn() : 0);
                setNumber2(row, col++, vo.getTmx() != null && vo.getTmx() != 0 ? vo.getTmx() : 0);
                setNumber2(row, col++, vo.getRnf() != null && vo.getRnf() != 0 ? vo.getRnf() : "");
                setNumber2(row, col++, vo.getReh() != null && vo.getReh() != 0 ? vo.getReh() : "");
                setNumber2(row, col++, vo.getManSelf() != null && vo.getManSelf() != 0 ? vo.getManSelf() : "");
                setNumber2(row, col++, vo.getManSelfTm() != null && vo.getManSelfTm() != 0 ? vo.getManSelfTm() : "");
                setNumber2(row, col++, vo.getManSelfTmm() != null && vo.getManSelfTmm() != 0 ? vo.getManSelfTmm() : "");
                setNumber2(row, col++, vo.getWomanSelf() != null && vo.getWomanSelf() != 0 ? vo.getWomanSelf() : "");
                setNumber2(row, col++, vo.getWomanSelfTm() != null && vo.getWomanSelfTm() != 0 ? vo.getWomanSelfTm() : "");
                setNumber2(row, col++, vo.getWomanSelfTmm() != null && vo.getWomanSelfTmm() != 0 ? vo.getWomanSelfTmm() : "");
                setNumber2(row, col++, vo.getManHire() != null && vo.getManHire() != 0 ? vo.getManHire() : "");
                setNumber2(row, col++, vo.getManHireTm() != null && vo.getManHireTm() != 0 ? vo.getManHireTm() : "");
                setNumber2(row, col++, vo.getManHireTmm() != null && vo.getManHireTmm() != 0 ? vo.getManHireTmm() : "");
                setNumber2(row, col++, vo.getWomanHire() != null && vo.getWomanHire() != 0 ? vo.getWomanHire() : "");
                setNumber2(row, col++, vo.getWomanHireTm() != null && vo.getWomanHireTm() != 0 ? vo.getWomanHireTm() : "");
                setNumber2(row, col++, vo.getWomanHireTmm() != null && vo.getWomanHireTmm() != 0 ? vo.getWomanHireTmm() : "");
                setText(row, col++, comma(vo.getQuan()) + (vo.getPackTCdNm() != null ? vo.getPackTCdNm() : "") + (vo.getGradeTCdNm() != null ? "(" + vo.getGradeTCdNm() + ")" : ""), dataRStyle);
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
