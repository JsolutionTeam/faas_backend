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

public class DiarySimpleExcelAdminView extends AbstractExcelView {

    @SuppressWarnings({ "unchecked" })
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserDiaryDto> list = (List<UserDiaryDto>) model.get("list");
        HSSFCell cell = null;
        int row = 0, col = 0, subCol = 0;

        sheet.getPrintSetup().setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setAutobreaks(true);

        HSSFPrintSetup pts = sheet.getPrintSetup();
        pts.setFitWidth((short) 1);
        pts.setFitHeight((short) 0);

        sheet.setColumnWidth(col++, (25 * 256)); //ID
        sheet.setColumnWidth(col++, (10 * 256)); //이름
        sheet.setColumnWidth(col++, (11 * 256)); //일자
        sheet.setColumnWidth(col++, (5 * 256)); //일지계획
        sheet.setColumnWidth(col++, (25 * 256)); //작업단계
        sheet.setColumnWidth(col++, (30 * 256)); //작업내용
        sheet.setColumnWidth(col++, (15 * 256)); //날씨
        sheet.setColumnWidth(col++, (9 * 256)); //기온
        sheet.setColumnWidth(col++, (9 * 256)); //최저온도
        sheet.setColumnWidth(col++, (9 * 256)); //최고온도
        sheet.setColumnWidth(col++, (7 * 256)); //강수량
        sheet.setColumnWidth(col++, (7 * 256)); //습도
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

        String st = cond.get("sActDt");
        String ed = cond.get("eActDt");
        if (st != null && !st.isEmpty() && ed != null && !ed.isEmpty()) {
            setText(row++, 0, "기간 : " + getFullDate(st) + " ~ " + getFullDate(ed), summaryStyle);
        } else {
            setText(row++, 0, "기간 : 전체", summaryStyle);
        }

        String actNm = cond.get("actNm");
        if(StringUtils.isEmpty(actNm) == false) {
            setText(row++, 0, "작업단계 : "+actNm, summaryStyle);
        }

        String f = cond.get("field");
        String k = cond.get("keyword");
        if(StringUtils.isEmpty(k) == false){
            switch (f) {
                case "userId":
                    setText(row++, 0, "ID : " + (k != null ? k : ""), summaryStyle);
                    break;
                case "userNm":
                    setText(row++, 0, "이름 : " + (k != null ? k : ""), summaryStyle);
                    break;
                case "remark":
                    setText(row++, 0, "작업내용 : " + (k != null ? k : ""), summaryStyle);
                    break;
                default:
                    if (k != null && !k.isEmpty()) {
                        setText(row++, 0, "검색 : " + (k != null ? k : ""), summaryStyle);
                    }
                    break;
            }
        }

        row++;
        col = 0;

        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 0, 0)); //ID
        setCellStyle(row, row+1, 0, 0, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 1, 1)); //이름
        setCellStyle(row, row+1, 1, 1, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 2, 2)); //일자
        setCellStyle(row, row+1, 2, 2, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 3, 3)); //일지계획
        setCellStyle(row, row+1, 3, 3, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 4, 4)); //작업단계
        setCellStyle(row, row+1, 4, 4, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 5, 5)); //작업내용
        setCellStyle(row, row+1, 5, 5, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 6, 6)); //날씨
        setCellStyle(row, row+1, 6, 6, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 7, 7)); //기온
        setCellStyle(row, row+1, 7, 7, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 8, 8)); //최고온도
        setCellStyle(row, row+1, 8, 8, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 9, 9)); //최저온도
        setCellStyle(row, row+1, 9, 9, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 10, 10)); //강수량
        setCellStyle(row, row+1, 10, 10, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 11, 11)); //습도
        setCellStyle(row, row+1, 11, 11, headerStyle);

        sheet.addMergedRegion(new CellRangeAddress(row, row, 12, 14)); //자가(남)
        setCellStyle(row, row, 12, 14, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 15, 17)); //자가(여)
        setCellStyle(row, row, 15, 17, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 18, 20)); //고용(남)
        setCellStyle(row, row, 18, 20, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row, 21, 23)); //고용(여)
        setCellStyle(row, row, 21, 23, headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row+1, 24, 24)); //수확량
        setCellStyle(row, row+1, 24, 24, headerStyle);


        col = 0;
        setText(row, col++, "ID", headerStyle);
        setText(row, col++, "이름", headerStyle);
        setText(row, col++, "일자", headerStyle);
        setText(row, col++, "일지\n계획", headerStyle);
        setText(row, col++, "작업단계", headerStyle);
        setText(row, col++, "작업내용", headerStyle);
        setText(row, col++, "날씨", headerStyle);
        setText(row, col++, "기온", headerStyle);
        setText(row, col++, "최저온도", headerStyle);
        setText(row, col++, "최고온도", headerStyle);
        setText(row, col++, "강수량", headerStyle);
        setText(row, col++, "습도", headerStyle);
        setText(row, col, "자가(남)", headerStyle);
        subCol = col;
        col+=3;
        setText(row, col, "자가(여)", headerStyle);
        col+=3;
        setText(row, col, "고용(남)", headerStyle);
        col+=3;
        setText(row, col, "고용(여)", headerStyle);
        col+=3;
        setText(row, col, "수확량", headerStyle);

        col = subCol;
        row += 1;

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
        //col++;

        if (list.size() > 0) {
            for (UserDiaryDto vo : list) {
                String remark = vo.getRemark();
                row++;
                col = 0;

                if(vo.getChemicalList() != null && vo.getChemicalList().size() > 0) {
                    UserChemicalStockDto chemicalStockDto = vo.getChemicalList().get(0);
                    String userChemicalNm = chemicalStockDto.getUserChemicalNm();
                    String chemicalTCdNm = chemicalStockDto.getChemicalTCdNm();
                    Double quan = chemicalStockDto.getQuan();
                    String packTCdNm = chemicalStockDto.getPackTCdNm();
                    remark += (StringUtils.isNotBlank(remark) ? "\n\r" : "");
                    remark += (userChemicalNm  + "("+ chemicalTCdNm +") - " + quan + packTCdNm);
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
                    if(manureStockDto.getManureTCd2() != null && manureStockDto.getManureTCd2().equals("1")){
                        cpFertilizer = (cpFerN != null ? cpFerN : "0") + "-"+ (cpFerP != null ? cpFerP : "0") + "-" + (cpFerK != null ? cpFerK : "0");
                    }
                    remark += (StringUtils.isNotBlank(remark) ? "\n\r" : "");
                    remark += ((StringUtils.isNotBlank(manureNm) ? manureNm + (StringUtils.isNotBlank(cpFertilizer) ? "["  + cpFertilizer + "]" : "") +  " - " : "")  + quan + packTCdNm);
                }

                setText(row, col++, vo.getUserId(), dataLStyle);
                setText(row, col++, vo.getUserNm(), dataCStyle);
                setText(row, col++, setDashDate(vo.getActDt()), dataCStyle);
                setText(row, col++, vo.getDiaryTCdNm(), dataCStyle);
                if(vo.getActCnt() > 1) {
                    setText(row, col++, vo.getActNm() + " " + vo.getInning() + "차", dataCStyle);
                } else {
                    setText(row, col++, vo.getActNm(), dataCStyle);
                }
                setText(row, col++, remark != null?(remark).replace(System.getProperty("line.separator"), " "): "", dataLStyle);
                setText(row, col++, vo.getSkyTCdNm(), dataCStyle);
                setNumber2(row, col++, vo.getTemp()!=null&&vo.getTemp()!=0?vo.getTemp():0);
                setNumber2(row, col++, vo.getTmn()!=null&&vo.getTmn()!=0?vo.getTmn():0);
                setNumber2(row, col++, vo.getTmx()!=null&&vo.getTmx()!=0?vo.getTmx():0);
                setNumber2(row, col++, vo.getRnf()!=null&&vo.getRnf()!=0?vo.getRnf():"");
                setNumber2(row, col++, vo.getReh()!=null&&vo.getReh()!=0?vo.getReh():"");
                setNumber2(row, col++, vo.getManSelf()!=null&&vo.getManSelf()!=0?vo.getManSelf():"");
                setNumber2(row, col++, vo.getManSelfTm()!=null&&vo.getManSelfTm()!=0?vo.getManSelfTm():"");
                setNumber2(row, col++, vo.getManSelfTmm()!=null&&vo.getManSelfTmm()!=0?vo.getManSelfTmm():"");
                setNumber2(row, col++, vo.getWomanSelf()!=null&&vo.getWomanSelf()!=0?vo.getWomanSelf():"");
                setNumber2(row, col++, vo.getWomanSelfTm()!=null&&vo.getWomanSelfTm()!=0?vo.getWomanSelfTm():"");
                setNumber2(row, col++, vo.getWomanSelfTmm()!=null&&vo.getWomanSelfTmm()!=0?vo.getWomanSelfTmm():"");
                setNumber2(row, col++, vo.getManHire()!=null&&vo.getManHire()!=0?vo.getManHire():"");
                setNumber2(row, col++, vo.getManHireTm()!=null&&vo.getManHireTm()!=0?vo.getManHireTm():"");
                setNumber2(row, col++, vo.getManHireTmm()!=null&&vo.getManHireTmm()!=0?vo.getManHireTmm():"");
                setNumber2(row, col++, vo.getWomanHire()!=null&&vo.getWomanHire()!=0?vo.getWomanHire():"");
                setNumber2(row, col++, vo.getWomanHireTm()!=null&&vo.getWomanHireTm()!=0?vo.getWomanHireTm():"");
                setNumber2(row, col++, vo.getWomanHireTmm()!=null&&vo.getWomanHireTmm()!=0?vo.getWomanHireTmm():"");
                setText(row, col++, comma(vo.getQuan()) + (vo.getPackTCdNm() != null ? vo.getPackTCdNm() : ""), dataRStyle);
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
