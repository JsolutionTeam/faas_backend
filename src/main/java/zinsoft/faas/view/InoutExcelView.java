package zinsoft.faas.view;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import zinsoft.faas.dto.UserInoutDto;

public class InoutExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        String sheetName = (String) model.get("sheetName");
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserInoutDto> list1 = (List<UserInoutDto>) model.get("list1");
        List<UserInoutDto> list2 = (List<UserInoutDto>) model.get("list2");
        boolean isAdmin = "Y".equals(cond.get("adminYn"));
        String inoutCd = cond.get("inoutCd");
        HSSFCell cell = null;
        int row = 0, col = 0;
        SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat outDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        sheet.getPrintSetup().setLandscape(true);
        sheet.setFitToPage(true);
        sheet.setAutobreaks(true);

        HSSFPrintSetup pts = sheet.getPrintSetup();
        pts.setFitWidth((short) 1);
        pts.setFitHeight((short) 0);

        if (isAdmin) {
            sheet.setColumnWidth(col++, (35 * 256)); // ID
            sheet.setColumnWidth(col++, (9 * 256)); // 이름
        }
        sheet.setColumnWidth(col++, (11 * 256));//일
        sheet.setColumnWidth(col++, (15 * 256));//종류
        sheet.setColumnWidth(col++, (50 * 256));//수입내용
        sheet.setColumnWidth(col++, (15 * 256));//등급
        sheet.setColumnWidth(col++, (10 * 256));//단량
        sheet.setColumnWidth(col++, (10 * 256));//수량
        sheet.setColumnWidth(col++, (15 * 256));//단가
        sheet.setColumnWidth(col++, (15 * 256));//금액
        sheet.setColumnWidth(col++, (15 * 256));//결제
        sheet.setColumnWidth(col++, (15 * 256));//적요

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col - 1));
        cell = getCell(sheet, row++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, sheetName);

        row++;

        String sTrdDt = cond.get("sTrdDt");
        String eTrdDt = cond.get("eTrdDt");
        if ((sTrdDt != null && sTrdDt.isEmpty() == false) && (eTrdDt != null && eTrdDt.isEmpty() == false)) {
             setText(row++, 0, "기간 : " + getFullDate(sTrdDt) + "~" + getFullDate(eTrdDt), summaryStyle);
        } else {
            setText(row++, 0, "기간 : 전체", summaryStyle);
        }

        String costTCdNm = cond.get("costTCdNm");
        if(StringUtils.isNotEmpty(costTCdNm)) {
            setText(row++, 0, "종류 : " + (cond.get("costTCdNm") != null ? cond.get("costTCdNm") : "전체"), summaryStyle);
        }

        String inoutTCdNm = cond.get("inoutTCdNm");
        if(StringUtils.isNotEmpty(inoutTCdNm)) {
            setText(row++, 0, "결제 구분 : " + (cond.get("inoutTCdNm") != null ? cond.get("inoutTCdNm") : "전체"), summaryStyle);
        }

        if (isAdmin) {
            String f = cond.get("field");
            String k = cond.get("keyword");
            if(StringUtils.isNoneEmpty(k)) {
                switch (f) {
                    case "userId":
                        setText(row++, 0, "ID : " + (k != null ? k : ""), summaryStyle);
                        break;
                    case "userNm":
                        setText(row++, 0, "이름 : " + (k != null ? k : ""), summaryStyle);
                        break;
                    case "inoutContent":
                        setText(row++, 0, "내용 : " + (k != null ? k : ""), summaryStyle);
                        break;
                    default:
                        if (k != null && !k.isEmpty()) {
                            setText(row++, 0, "검색 : " + (k != null ? k : ""), summaryStyle);
                        }
                        break;
                }
            }
        } else {
            String k = cond.get("keyword");
            if(StringUtils.isNoneEmpty(k)) {
                setText(row++, 0, "내용 : " + (k != null ? k : ""), summaryStyle);
            }
        }

        row++;
        col = 0;
        setText(row, col++, "입금", headerStyle);

        row++;
        col = 0;

        if (isAdmin) {
            setText(row, col++, "ID", headerStyle);
            setText(row, col++, "이름", headerStyle);
        }
        setText(row, col++, "일자", headerStyle);
        setText(row, col++, "종류", headerStyle);
        setText(row, col++, "입금내용", headerStyle);
        setText(row, col++, "등급", headerStyle);
        setText(row, col++, "단량", headerStyle);
        setText(row, col++, "수량", headerStyle);
        setText(row, col++, "단가(원)", headerStyle);
        setText(row, col++, "금액(원)", headerStyle);
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
                setText(row, col++, vo.getAcNm(), dataLStyle);
                setText(row, col++, vo.getInoutContent(), dataLStyle);
                setText(row, col++, vo.getGradeTCdNm(), dataLStyle);
                setText(row, col++, comma(vo.getUnitPack() != null ? vo.getUnitPack() : 1) + (vo.getPackTCdNm() != null ? vo.getPackTCdNm() : ""), dataRStyle);
                setText(row, col++, comma(vo.getQuan()), dataRStyle);
                setNumber(row, col++, vo.getUnitAmt());
                setNumber(row, col++, vo.getAmt());
                setText(row, col++, vo.getInoutTCdNm(), dataLStyle);
                setText(row, col++, vo.getRemark(), dataLStyle);
                sum += (vo.getAmt() != null ? vo.getAmt() : 0);
            }
            if (!isAdmin) {
                row++;
                col = 0;
                sheet.addMergedRegion(new CellRangeAddress(row, row, 0, 4));
                sheet.addMergedRegion(new CellRangeAddress(row, row, 8, 9));
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
            }
        } else {
            row++;
            setText(row, 0, "검색 결과 없음", dataCStyle);

            for (int i = 1; i < col; i++) {
                setText(row, i, "", dataCStyle);
            }

            sheet.addMergedRegion(new CellRangeAddress(row, row, 0, col - 1));
        }

        row++;
        row++;

        row++;
        col = 0;
        setText(row, col++, "출금", headerStyle);

        row++;
        col = 0;

        if (isAdmin) {
            setText(row, col++, "ID", headerStyle);
            setText(row, col++, "이름", headerStyle);
        }
        setText(row, col++, "일자", headerStyle);
        setText(row, col++, "종류", headerStyle);
        setText(row, col++, "출금내용", headerStyle);
        setText(row, col++, "등급", headerStyle);
        setText(row, col++, "단량", headerStyle);
        setText(row, col++, "수량", headerStyle);
        setText(row, col++, "단가(원)", headerStyle);
        setText(row, col++, "금액(원)", headerStyle);
        setText(row, col++, "결제", headerStyle);
        setText(row, col++, "적요", headerStyle);

        if (list2.size() > 0) {
            long sum = 0;
            for (UserInoutDto vo : list2) {
                row++;
                col = 0;

                if (isAdmin) {
                    setText(row, col++, vo.getUserId(), dataLStyle);
                    setText(row, col++, vo.getUserNm(), dataCStyle);
                }
                setText(row, col++, getFullDate(vo.getTrdDt()), dataCStyle);
                setText(row, col++, vo.getAcNm(), dataLStyle);
                setText(row, col++, vo.getInoutContent(), dataLStyle);
                setText(row, col++, vo.getGradeTCdNm(), dataLStyle);
                setText(row, col++, comma(vo.getUnitPack() != null ? vo.getUnitPack() : 1) + (vo.getPackTCdNm() != null ? vo.getPackTCdNm() : ""), dataRStyle);
                setText(row, col++, comma(vo.getQuan()), dataRStyle);
                setNumber(row, col++, vo.getUnitAmt());
                setNumber(row, col++, vo.getAmt());
                setText(row, col++, vo.getInoutTCdNm(), dataLStyle);
                setText(row, col++, vo.getRemark(), dataLStyle);
                sum += (vo.getAmt() != null ? vo.getAmt() : 0);
            }
            if (!isAdmin) {
                row++;
                col = 0;
                sheet.addMergedRegion(new CellRangeAddress(row, row, 0, 4));
                sheet.addMergedRegion(new CellRangeAddress(row, row, 8, 9));
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "[합계]", dataRStyle);
                setText(row, col++, list2.size() + "건", dataRStyle);
                setNumber(row, col++, sum);
                setText(row, col++, "", dataCStyle);
                setText(row, col++, "", dataCStyle);
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
