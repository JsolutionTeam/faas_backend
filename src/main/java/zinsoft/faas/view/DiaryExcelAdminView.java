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

import zinsoft.faas.dto.UserDiaryDto;

public class DiaryExcelAdminView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    public void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserDiaryDto> list = (List<UserDiaryDto>) model.get("list");
        HSSFCell cell = null;
        int row = 0, col = 0;

        sheet.getPrintSetup().setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setAutobreaks(true);

        HSSFPrintSetup pts = sheet.getPrintSetup();
        pts.setFitWidth((short) 1);
        pts.setFitHeight((short) 0);

        sheet.setColumnWidth(col++, (25 * 256)); // ID
        sheet.setColumnWidth(col++, (9 * 256)); // 이름
        sheet.setColumnWidth(col++, (11 * 256)); // 일
        sheet.setColumnWidth(col++, (5 * 256)); // 일지/계획
        sheet.setColumnWidth(col++, (20 * 256)); // 품목
        sheet.setColumnWidth(col++, (20 * 256)); // 작업단계
        sheet.setColumnWidth(col++, (30 * 256)); // 작업내역
        sheet.setColumnWidth(col++, (15 * 256)); // 날씨
        sheet.setColumnWidth(col++, (7 * 256)); // 최저온도
        sheet.setColumnWidth(col++, (7 * 256)); // 최고온도
        sheet.setColumnWidth(col++, (7 * 256)); // 강수량
        sheet.setColumnWidth(col++, (7 * 256)); // 인력수
        sheet.setColumnWidth(col++, (5 * 256)); // 시간
        sheet.setColumnWidth(col++, (7 * 256)); // 인력수
        sheet.setColumnWidth(col++, (5 * 256)); // 시간
        sheet.setColumnWidth(col++, (7 * 256)); // 인력수
        sheet.setColumnWidth(col++, (5 * 256)); // 시간
        sheet.setColumnWidth(col++, (7 * 256)); // 인력수
        sheet.setColumnWidth(col++, (5 * 256)); // 시간
        sheet.setColumnWidth(col++, (9 * 256)); // 포장단위
        sheet.setColumnWidth(col++, (9 * 256)); // 수량

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

     //   setText(row++, 0, "일지/계획 : " + cond.get("diaryTCdNm"), summaryStyle);

        String f = cond.get("field");
        String k = cond.get("keyword");
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

        row++;
        col = 0;

        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
        setText(row, col++, "ID", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
        setText(row, col++, "이름", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
        setText(row, col++, "일자", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
        setText(row, col++, "일지\n계획", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
        setText(row, col++, "품목", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
        setText(row, col++, "작업단계", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
        setText(row, col++, "작업내역", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
        setText(row, col++, "날씨", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
        setText(row, col++, "최저\n온도", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
        setText(row, col++, "최고\n온도", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
        setText(row, col++, "강수량", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row, col, col + 1));
        setText(row, col++, "자가(남)", headerStyle);
        setText(row, col++, "", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row, col, col + 1));
        setText(row, col++, "자가(여)", headerStyle);
        setText(row, col++, "", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row, col, col + 1));
        setText(row, col++, "교용(남)", headerStyle);
        setText(row, col++, "", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row, col, col + 1));
        setText(row, col++, "고용(여)", headerStyle);
        setText(row, col++, "", headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(row, row + 1, col, col));
        setText(row, col++, "수확량", headerStyle);

        row++;
        col = 0;

        setText(row, col++, "", headerStyle);
        setText(row, col++, "", headerStyle);
        setText(row, col++, "", headerStyle);
        setText(row, col++, "", headerStyle);
        setText(row, col++, "", headerStyle);
        setText(row, col++, "", headerStyle);
        setText(row, col++, "", headerStyle);
        setText(row, col++, "", headerStyle);
        setText(row, col++, "", headerStyle);
        setText(row, col++, "", headerStyle);
        setText(row, col++, "", headerStyle);
        setText(row, col++, "인력수", headerStyle);
        setText(row, col++, "시간", headerStyle);
        setText(row, col++, "인력수", headerStyle);
        setText(row, col++, "시간", headerStyle);
        setText(row, col++, "인력수", headerStyle);
        setText(row, col++, "시간", headerStyle);
        setText(row, col++, "인력수", headerStyle);
        setText(row, col++, "시간", headerStyle);
        setText(row, col++, "", headerStyle);

        if (list.size() > 0) {
            for (UserDiaryDto vo : list) {
                row++;
                col = 0;

                setText(row, col++, vo.getUserId(), dataLStyle);
                setText(row, col++, vo.getUserNm(), dataCStyle);
                setText(row, col++, getFullDate(vo.getActDt()), dataCStyle);
                setText(row, col++, vo.getDiaryTCdNm(), dataCStyle);
                setText(row, col++, vo.getCropBCdNm(), dataLStyle);
                setText(row, col++, vo.getActNm(), dataLStyle);
                setText(row, col++, vo.getRemark(), dataLStyle);
                setText(row, col++, vo.getSkyTCdNm(), dataLStyle);
                setNumber(row, col++, vo.getTmn());
                setNumber(row, col++, vo.getTmx());
                setNumber(row, col++, vo.getRnf());
                setNumber(row, col++, vo.getManSelf());
                setNumber(row, col++, vo.getManSelfTm());
                setNumber(row, col++, vo.getWomanSelf());
                setNumber(row, col++, vo.getWomanSelfTm());
                setNumber(row, col++, vo.getManHire());
                setNumber(row, col++, vo.getManHireTm());
                setNumber(row, col++, vo.getWomanHire());
                setNumber(row, col++, vo.getWomanHireTm());
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

}
