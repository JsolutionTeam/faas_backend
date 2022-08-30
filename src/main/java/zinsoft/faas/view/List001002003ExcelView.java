package zinsoft.faas.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.faas.dto.UserInoutDto;

public class List001002003ExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserInoutDto> list1 = (List<UserInoutDto>) model.get("list1");
        boolean isAdmin = "Y".equals(cond.get("adminYn"));
        HSSFCell cell = null;
        int row = 0, col = 0;

        sheet.getPrintSetup().setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setAutobreaks(true);

        if (isAdmin) {
            sheet.setColumnWidth(col++, (35 * 256)); // ID
            sheet.setColumnWidth(col++, (9 * 256)); // 이름
        }
        sheet.setColumnWidth(col++, (11 * 256));//일
        sheet.setColumnWidth(col++, (15 * 256));//품목
        sheet.setColumnWidth(col++, (15 * 256));//종류
        sheet.setColumnWidth(col++, (50 * 256));//수입내용
        sheet.setColumnWidth(col++, (15 * 256));//등급
        sheet.setColumnWidth(col++, (10 * 256));//포장단위
        sheet.setColumnWidth(col++, (10 * 256));//수량
        sheet.setColumnWidth(col++, (15 * 256));//단가
        sheet.setColumnWidth(col++, (15 * 256));//금액
        sheet.setColumnWidth(col++, (20 * 256));//판매처
        sheet.setColumnWidth(col++, (10 * 256));//결제
        sheet.setColumnWidth(col++, (15 * 256));//적요

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;

        String sy = cond.get("sTrdDtY");
        String sm = cond.get("sTrdDtM");
        String sd = cond.get("sTrdDtD");
        String ey = cond.get("eTrdDtY");
        String em = cond.get("eTrdDtM");
        String ed = cond.get("eTrdDtD");
        if (sy != null && !sy.isEmpty()) {
            setText(row++, 0, "기간 : " + sy + "년 " + sm + "월 " + sd + "일 ~ " + ey + "년 " + em + "월 " + ed + "일", summaryStyle);
        } else {
            setText(row++, 0, "기간 : 전체", summaryStyle);
        }
        if (!isAdmin) {
            setText(row++, 0, "품목 : " + cond.get("cropNm"), summaryStyle);
            setText(row++, 0, "거래처 : " + cond.get("custNm"), summaryStyle);
        }
        setText(row++, 0, "결제구분 : " + cond.get("inoutTCdNm"), summaryStyle);

        if (isAdmin) {
            String f = cond.get("field");
            String k = cond.get("keyword");
            switch (f) {
                case "userId":
                    setText(row++, 0, "ID : " + (k != null ? k : ""), summaryStyle);
                    break;
                case "userNm":
                    setText(row++, 0, "이름 : " + (k != null ? k : ""), summaryStyle);
                    break;
                case "cropNm":
                    setText(row++, 0, "품목 : " + (k != null ? k : ""), summaryStyle);
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

        if (isAdmin) {
            setText(row, col++, "ID", headerStyle);
            setText(row, col++, "이름", headerStyle);
        }
        setText(row, col++, "일자", headerStyle);
        setText(row, col++, "품목", headerStyle);
        setText(row, col++, "종류", headerStyle);
        setText(row, col++, "수입내용", headerStyle);
        setText(row, col++, "등급", headerStyle);
        setText(row, col++, "포장단위", headerStyle);
        setText(row, col++, "수량", headerStyle);
        setText(row, col++, "단가", headerStyle);
        setText(row, col++, "금액", headerStyle);
        setText(row, col++, "판매처", headerStyle);
        setText(row, col++, "결제", headerStyle);
        setText(row, col++, "적요", headerStyle);

        if (list1.size() > 0) {
            long sum = 0;
            for (UserInoutDto vo : list1) {
                row++;
                col = 0;

                if (isAdmin) {
                    setText(row, col++, vo.getUserId(), dataLStyle);
                    setText(row, col++, vo.getUserNm(), dataCStyle);
                }
                setText(row, col++, getFullDate(vo.getTrdDt()), dataCStyle);
                setText(row, col++, vo.getCropNm(), dataLStyle);
                setText(row, col++, vo.getAcNm(), dataLStyle);
                setText(row, col++, vo.getInoutContent(), dataLStyle);
                setText(row, col++, vo.getGradeTCdNm(), dataLStyle);
                setText(row, col++, comma(vo.getUnitPack()) + (vo.getPackTCdNm() != null ? vo.getPackTCdNm() : ""), dataRStyle);
                setNumber(row, col++, vo.getQuan());
                setNumber(row, col++, vo.getUnitAmt());
                setNumber(row, col++, vo.getAmt());
                setText(row, col++, vo.getCustNm(), dataLStyle);
                setText(row, col++, vo.getInoutTCdNm(), dataLStyle);
                setText(row, col++, vo.getRemark(), dataLStyle);
                sum += vo.getAmt();
            }
            if (!isAdmin) {
                row++;
                col = 0;
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "[합계]", dataRStyle);
                setText(row, col++, list1.size() + "건", dataRStyle);
                setNumber(row, col++, sum);
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "", dataCStyle);
                sheet.addMergedRegion(new CellRangeAddress(row, row, 0, 5));
                sheet.addMergedRegion(new CellRangeAddress(row, row, 9, 11));
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
