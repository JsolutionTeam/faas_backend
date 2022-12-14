package zinsoft.faas.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.FileCopyUtils;

import net.coobird.thumbnailator.Thumbnails;
import zinsoft.faas.dto.UserChemicalStockDto;
import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.dto.UserManureStockDto;
import zinsoft.util.AppPropertyUtil;
import zinsoft.util.CommonUtil;
import zinsoft.util.Constants;
import zinsoft.web.common.service.FileInfoService;

public class DiaryExcelView extends AbstractExcelView {

    private int imageWidth = 800;

    @Resource
    FileInfoService fileInfoService;

    public DiaryExcelView() {
        super();

        String imageWidth = AppPropertyUtil.get(Constants.IMAGE_THUMB_WIDTH);

        try {
            this.imageWidth = Integer.parseInt(imageWidth, 10);
        } catch (Exception e) {
            this.imageWidth = 800;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void build(Map<String, Object> model, HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> cond = (Map<String, String>) model.get("cond");
        List<UserDiaryDto> list = (List<UserDiaryDto>) model.get("list");
        final int defRow = 39; //????????? ????????? ?????? ?????? (1page 2day 79->39)
        final int printPageRow = defRow * 2;
        int row = 0, col = 0;
        int pageStartRow = 0;
        int firstItemRow = 4; //????????????, ?????? ??????????????? ???????????? ??????
        int contentRow = 6; //????????? ?????????, ?????? ????????? ???????????? ??????
        int pictureRow = 10; //????????? ??????
        int workPictRow = 10; //?????????????????????
        int receiptPictRow = 39; //??????????????????
        int printRow = 0;
        String date = null;

        HSSFSheet sheet = wb.getSheetAt(0);
        sheet.setFitToPage(true);
        sheet.setAutobreaks(true);

        HSSFPrintSetup pts = sheet.getPrintSetup();
        pts.setFitWidth((short) 1);
        pts.setFitHeight((short) 0);

        for (col = 1; col < 19; col++) {
            sheet.setColumnWidth(col, (7 * 256));
        }

        //???????????? ?????? ????????? ????????? ??????????????? ??????
        if (list.size() > 0) {

            for (int i = 0, cnt = list.size(); i < cnt; i++) {
                UserDiaryDto vo = list.get(i);

                if (printRow > 0) {
                    sheet.setRowBreak(printRow - 1);
                }

                //??????????????? ?????? ????????? ????????? ???????????? ?????? ??????
                drawMenu(vo, cond, pageStartRow, firstItemRow, workPictRow, receiptPictRow);
                drawValue(vo, contentRow, wb, sheet, pictureRow); //????????? ?????? ?????? ??? ????????? ??????

//                try {
//                    drawSheet(sheet, wb, vo.getWorkingFileSeqs(), vo.getReceiptFileSeqs(), pictureRow); //????????? ??????
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                int outMenuRow = drawInMenu(vo, cond, pageStartRow, firstItemRow, contentRow);
                int usedRow = drawOutMenu(vo, cond, pageStartRow, firstItemRow, outMenuRow);

                drawUsedThing(vo, usedRow);

                pageStartRow += defRow;
                firstItemRow += defRow;
                contentRow += defRow;
                pictureRow += defRow;
                workPictRow += defRow;
                receiptPictRow += defRow;
                printRow += printPageRow; // (1page 2day)
            }
        } else {
            row++;
            setText(row, 0, "?????? ?????? ??????", dataCStyle);

            for (int i = 1; i < col; i++) {
                setText(row, i, "", dataCStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(row, row, 0, 20));
        }
    }

    private int drawSheet(HSSFSheet sheet, HSSFWorkbook wb, List<Long> workingFileSeqs, List<Long> receiptFileSeqs, int pictureRow) throws IOException {
        int photoCnt = 0;

        if (workingFileSeqs != null && !workingFileSeqs.isEmpty()) {
            photoCnt += workingFileSeqs.size();
            drawPict(workingFileSeqs, wb, "workingPhoto", pictureRow);
        }
        /*
        if (receiptFileSeqs != null && !receiptFileSeqs.isEmpty()) {
            photoCnt += receiptFileSeqs.size();
            drawPict(receiptFileSeqs, wb, "receiptPhoto", pictureRow + 26); //????????? ????????? ???????????? ????????? ??????
        }*/

        return photoCnt;
    }

    private int drawPict(List<Long> fileSeqs, HSSFWorkbook wb, String photoType, int pictureRow) {
        int pictureIdx = -1;
        int col = 1;

        for (int i = 0, cnt = fileSeqs.size(); i < cnt; i++) {
            if (i >= 4) {
                break;
            }

            try {
                Long fileSeq = fileSeqs.get(i);
                File file = CommonUtil.getUploadFile();
                String thumbName = file.getAbsoluteFile() + "_" + imageWidth;
                File img = new File(thumbName);

                if (file.exists() && !img.exists()) {
                    OutputStream os = new FileOutputStream(new File(thumbName));
                    Thumbnails.of(file).size(imageWidth, imageWidth).toOutputStream(os);
                    os.close();
                }

                if (img.length() > 0) {
                    pictureIdx = loadPicture(thumbName, wb); //C:\\upload\\SmartFarm\\
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (pictureIdx >= 0) {
                HSSFPatriarch drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = new HSSFClientAnchor();
                //pictureRow = 13
                if (photoType.equals("workingPhoto")) {
                    anchor.setCol1(col);
                    anchor.setRow1(pictureRow);
                    anchor.setDy1(55);
                    anchor.setDy2(55);
                    col += 5;
                }

                HSSFPicture pict = drawing.createPicture(anchor, pictureIdx);
                pict.setLineStyle(1);
                pict.resize(4.60, 8.50);
            }
        }

        return pictureRow;
    }

    private int loadPicture(String path, HSSFWorkbook wb) throws IOException {
        int pictureIdx = -1;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(path);
            bos = new ByteArrayOutputStream();
            FileCopyUtils.copy(fis, bos);
            pictureIdx = wb.addPicture(bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG);
        } finally {
            if (fis != null)
                fis.close();
            if (bos != null)
                bos.close();
        }
        return pictureIdx;
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

    private void drawMenu(UserDiaryDto vo, Map<String, String> cond, int pageStartRow, int firstItemRow, int workPictRow, int receiptRow) {
        HSSFCell cell = null;
        HSSFRow r = null;
        int row = 0;
        int col = 0;
        String actDt = vo.getActDt();
        String skyTCdNm = "";
        String tMn = "";
        String tMx = "";
        String rnF = "";
        String reh = "";

        if (vo.getSkyTCdNm() != null) {
            skyTCdNm = vo.getSkyTCdNm();
        }

        if (vo.getTmn() != null) {
            tMn = vo.getTmn().toString();
        }

        if (vo.getTmx() != null) {
            tMx = vo.getTmx().toString();
        }

        if (vo.getRnf() != null) {
            rnF = vo.getRnf().toString();
        }

        if (vo.getReh() != null) {
            reh = vo.getReh().toString();
        }

        //???????????? ??????
        sheet.addMergedRegion(new CellRangeAddress(pageStartRow, pageStartRow, 0, 20));
        cell = getCell(sheet, pageStartRow++, 0);
        cell.setCellStyle(titleStyle);
        setText(cell, "????????????");

        //?????????
        sheet.addMergedRegion(new CellRangeAddress(pageStartRow, pageStartRow, 0, 20));
        cell = getCell(sheet, pageStartRow++, 0);
        cell.setCellStyle(subTitleStyle);
        setText(cell, "[" + actDt.substring(0, 4) + "??? " + actDt.subSequence(4, 6) + "??? " + actDt.subSequence(6, 8) + "???" + "]");

        //??????
        sheet.addMergedRegion(new CellRangeAddress(pageStartRow, pageStartRow, 0, 20));
        cell = getCell(sheet, pageStartRow, 0);
        cell.setCellStyle(subTitleStyle);
        setText(cell, "??????: " + skyTCdNm + ", ????????????: " + tMn + "(???), ????????????: " + tMx + "(???), ?????????: " + rnF + "(???), ??????: "+ reh + "(%)");

        //???????????? 13?????? ??????, ????????? 39?????? ??????, ???????????? ?????? ???????????? ??????.
        setCellStyle(firstItemRow, firstItemRow + 5, 0, 20, dataLStyle); //???????????? Header
        setCellStyle(firstItemRow, firstItemRow + 14, 0, 0, dataLStyle); //????????????, ????????? Header
        setCellStyle(firstItemRow + 6, firstItemRow + 14, 20, 20, cellRStyle); //????????? right border
        setCellStyle(firstItemRow + 14, firstItemRow + 14, 1, 20, cellBStyle); //???????????? bottom border
        //setCellStyle(firstItemRow + 60, firstItemRow + 60, 1, 16, cellBStyle);   //????????? bottom border
        setCellStyle(firstItemRow + 14, firstItemRow + 14, 20, 20, cellRBStyle); //???????????? ????????? ?????????  border
        //setCellStyle(firstItemRow + 60, firstItemRow + 60, 16, 16, cellRBStyle); //????????? ????????? ????????? border

        //Header??? ???????????? ?????? ????????? ??? ?????? ????????? ???????????? ??????.
        setText(firstItemRow, col, "??????", headerStyle);
        col += 3;
        setText(firstItemRow, col, "????????????", headerStyle);
        col += 2;
        setText(firstItemRow, col, "????????????", headerStyle);
        col += 3;
        setText(firstItemRow, col, "??????(???)", headerStyle);
        col += 3;
        setText(firstItemRow, col, "??????(???)", headerStyle);
        col += 3;
        setText(firstItemRow, col, "??????(???)", headerStyle);
        col += 3;
        setText(firstItemRow, col, "??????(???)", headerStyle);
        col += 3;
        setText(firstItemRow, col, "?????????", headerStyle);
        //sheet.setColumnWidth(16, (13 * 256)); //! ???????????? ?????? ??? ?????? ?????? ??????

        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow + 1, 0, 2)); //??????
        //  sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow + 1, 3, 4)); //????????????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow + 1, 3, 7)); //????????????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow + 1, 20, 20)); //?????????

        firstItemRow++;
        col = 8;
        setText(firstItemRow, col++, "?????????", headerStyle);
        setText(firstItemRow, col++, "??????", headerStyle);
        setText(firstItemRow, col++, "???", headerStyle);
        setText(firstItemRow, col++, "?????????", headerStyle);
        setText(firstItemRow, col++, "??????", headerStyle);
        setText(firstItemRow, col++, "???", headerStyle);
        setText(firstItemRow, col++, "?????????", headerStyle);
        setText(firstItemRow, col++, "??????", headerStyle);
        setText(firstItemRow, col++, "???", headerStyle);
        setText(firstItemRow, col++, "?????????", headerStyle);
        setText(firstItemRow, col++, "??????", headerStyle);
        setText(firstItemRow, col++, "???", headerStyle);

        firstItemRow--;
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 8, 10)); //??????(???) Header
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 11, 13)); //??????(???)
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 14, 16)); //??????(???)
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 17, 19)); //??????(???)
        firstItemRow++;

        col = 0;
        setText(firstItemRow + 2, col, "????????????", headerStyle); // 5
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow + 2, firstItemRow + 4, 0, 0)); //???????????? Header
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow + 2, firstItemRow + 4, 1, 20)); //????????????

        //????????? ???????????? ????????? ????????? ??????
        row = firstItemRow + 1;
        for (int i = 0; i <= 3; i++) {
            r = sheet.getRow(row++);
            r.setHeight((short) (10 * 40));
        }

        col = 0;
        setText(workPictRow, col, "????????????", headerStyle);

        sheet.addMergedRegion(new CellRangeAddress(workPictRow, workPictRow += 8, 0, 0)); //????????????

        //?????? ????????? ????????? ????????? ??????.
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow + 1, firstItemRow + 1, 0, 2)); //??????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow + 1, firstItemRow + 1, 3, 7)); //????????????

    }

    private int drawInMenu(UserDiaryDto vo, Map<String, String> cond, int pageStartRow, int firstItemRow, int contentRow) {
        int col = 0;
        firstItemRow += 16; //????????????
        contentRow += 16; //???????????? ?????????
        List<UserInoutDto> inList = vo.getUserInList();

        setText(firstItemRow++, col, "??????", headerStyle);
        setText(firstItemRow, col++, "??????", headerStyle);
        col += 2;
        setText(firstItemRow, col++, "????????????", headerStyle);
        col += 5;
        setText(firstItemRow, col++, "??????", headerStyle);
        setText(firstItemRow, col++, "??????", headerStyle);
        setText(firstItemRow, col++, "??????", headerStyle);
        setText(firstItemRow, col++, "??????", headerStyle);
        col += 1;
        setText(firstItemRow, col++, "??????", headerStyle);
        col += 1;
        setText(firstItemRow, col++, "?????????", headerStyle);
        col += 2;
        setText(firstItemRow, col++, "??????", headerStyle);

        //?????? ??????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 0, 2)); //??????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 3, 8)); //????????????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 12, 13)); //??????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 14, 15)); //??????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 16, 18)); //?????????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 19, 20)); //??????

        setCellStyle(firstItemRow, firstItemRow, 0, 2, headerStyle);
        setCellStyle(firstItemRow, firstItemRow, 3, 8, headerStyle);
        setCellStyle(firstItemRow, firstItemRow, 12, 13, headerStyle);
        setCellStyle(firstItemRow, firstItemRow, 14, 15, headerStyle);
        setCellStyle(firstItemRow, firstItemRow, 16, 18, headerStyle);
        setCellStyle(firstItemRow, firstItemRow, 19, 20, headerStyle);

        if (inList.size() > 0) {
            UserInoutDto inVo = null;
            for (int i = 0; i < inList.size(); i++) {
                col = 0;
                inVo = inList.get(i);

                setText(contentRow, col++, inVo.getCropNm(), dataLStyle); //??????
                col += 2;
                setText(contentRow, col++, inVo.getInoutContent(), dataLStyle); //????????????
                col += 5;
                setText(contentRow, col++, inVo.getGradeTCdNm(), dataCStyle); //??????
                setText(contentRow, col++, inVo.getPackTCdNm(), dataCStyle); //????????????
                setNumber(contentRow, col++, inVo.getQuan()); //??????
                setNumber(contentRow, col++, inVo.getUnitAmt()); //??????
                col += 1;
                setNumber(contentRow, col++, inVo.getAmt()); //??????
                col += 1;
                setText(contentRow, col++, inVo.getCustNm(), dataLStyle); //?????????
                col += 2;
                setText(contentRow, col++, inVo.getInoutTCdNm(), dataLStyle); //??????

                //?????? ????????? ????????? ????????? ??????.
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, 0, 2)); //??????
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, 3, 8)); //????????????
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, 12, 13)); //??????
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, 14, 15)); //??????
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, 16, 18)); //?????????
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, 19, 20)); //??????

                //????????? ??? ?????? ???
                setCellStyle(contentRow, contentRow, 0, 2, dataLVTStyle);
                setCellStyle(contentRow, contentRow, 3, 8, dataLVTStyle);
                setCellStyle(contentRow, contentRow, 12, 13, dataNStyle);
                setCellStyle(contentRow, contentRow, 14, 15, dataNStyle);
                setCellStyle(contentRow, contentRow, 16, 18, dataLVTStyle);
                setCellStyle(contentRow, contentRow, 19, 20, dataLVTStyle);

                contentRow++;
            }
        }

        return contentRow;
    }

    private int drawOutMenu(UserDiaryDto vo, Map<String, String> cond, int pageStartRow, int firstItemRow, int contentRow) {
        int col = 0;
        firstItemRow = contentRow + 1;
        contentRow += 3;
        List<UserInoutDto> outList = vo.getUserOutList();

        setText(firstItemRow++, col, "??????", headerStyle);
        setText(firstItemRow, col++, "??????", headerStyle);
        col += 2;
        setText(firstItemRow, col++, "????????????", headerStyle);
        col += 5;
        setText(firstItemRow, col++, "??????", headerStyle);
        setText(firstItemRow, col++, "??????", headerStyle);
        setText(firstItemRow, col++, "??????", headerStyle);
        setText(firstItemRow, col++, "??????", headerStyle);
        col += 1;
        setText(firstItemRow, col++, "??????", headerStyle);
        col += 1;
        setText(firstItemRow, col++, "?????????", headerStyle);
        col += 2;
        setText(firstItemRow, col++, "??????", headerStyle);

        //?????? ??????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 0, 2)); //??????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 3, 8)); //????????????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 12, 13)); //??????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 14, 15)); //??????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 16, 18)); //?????????
        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, 19, 20)); //??????

        setCellStyle(firstItemRow, firstItemRow, 0, 2, headerStyle);
        setCellStyle(firstItemRow, firstItemRow, 3, 8, headerStyle);
        setCellStyle(firstItemRow, firstItemRow, 12, 13, headerStyle);
        setCellStyle(firstItemRow, firstItemRow, 14, 15, headerStyle);
        setCellStyle(firstItemRow, firstItemRow, 16, 18, headerStyle);
        setCellStyle(firstItemRow, firstItemRow, 19, 20, headerStyle);

        if (outList.size() > 0) {
            UserInoutDto inVo = null;
            for (int i = 0; i < outList.size(); i++) {
                col = 0;
                inVo = outList.get(i);

                setText(contentRow, col++, inVo.getCropNm(), dataLStyle); //??????
                col += 2;
                setText(contentRow, col++, inVo.getInoutContent(), dataLStyle); //????????????
                col += 5;
                setText(contentRow, col++, inVo.getGradeTCdNm(), dataCStyle); //??????
                setText(contentRow, col++, inVo.getPackTCdNm(), dataCStyle); //????????????
                setNumber(contentRow, col++, inVo.getQuan()); //??????
                setNumber(contentRow, col++, inVo.getUnitAmt()); //??????
                col += 1;
                setNumber(contentRow, col++, inVo.getAmt()); //??????
                col += 1;
                setText(contentRow, col++, inVo.getCustNm(), dataLStyle); //?????????
                col += 2;
                setText(contentRow, col++, inVo.getInoutTCdNm(), dataLStyle); //??????

                //?????? ????????? ????????? ????????? ??????.
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, 0, 2)); //??????
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, 3, 8)); //????????????
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, 12, 13)); //??????
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, 14, 15)); //??????
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, 16, 18)); //?????????
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, 19, 20)); //??????

                setCellStyle(contentRow, contentRow, 0, 2, dataLVTStyle); //??????
                setCellStyle(contentRow, contentRow, 3, 8, dataLVTStyle); //????????????
                setCellStyle(contentRow, contentRow, 12, 13, dataNStyle); //??????
                setCellStyle(contentRow, contentRow, 14, 15, dataNStyle); //??????
                setCellStyle(contentRow, contentRow, 16, 18, dataLVTStyle); //?????????
                setCellStyle(contentRow, contentRow, 19, 20, dataLVTStyle); //??????

                contentRow++;
            }
        }

        return contentRow;
    }

    private int drawUsedThing(UserDiaryDto vo, int contentRow) {
        int col = 0, firstItemRow = 0;
        firstItemRow = contentRow + 1;
        //UserChemicalStock usedChemi = vo.getUsedChemicalStock();
        //UserManureStock usedManure = vo.getUsedManureStock();
        //UserDiaryMachine usedMachine = vo.getUsedDiaryMachine();
        List<UserChemicalStockDto> chemiList = vo.getChemicalList();
        List<UserManureStockDto> manureList = vo.getManureList();

        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, col, col + 1));
        setText(firstItemRow, col, "??????", headerStyle);
        col += 2;

        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, col, col + 2));
        setText(firstItemRow, col, "?????????", headerStyle);
        col += 3;

        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, col, col + 2));
        setText(firstItemRow, col, "?????????", headerStyle);
        col += 3;

        sheet.addMergedRegion(new CellRangeAddress(firstItemRow, firstItemRow, col, col + 1));
        setText(firstItemRow, col, "?????????(??????)", headerStyle);
        col += 2;

        col = 0;
        contentRow = firstItemRow + 1;

        if ((chemiList == null || chemiList.size() == 0 ) && (manureList == null || manureList.size() == 0)) {
            sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, col, col + 1));
            setCellStyle(contentRow, contentRow, col, col + 1, dataLVTStyle);
            col += 2;
            sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, col, col + 2));
            setCellStyle(contentRow, contentRow, col, col + 2, dataLVTStyle);
            col += 3;
            sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, col, col + 2));
            setCellStyle(contentRow, contentRow, col, col + 2, dataLVTStyle);
            col += 3;
            sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, col, col + 1));
            setCellStyle(contentRow, contentRow, col, col + 1, dataLVTStyle);
        }

        if (chemiList != null && chemiList.size() > 0 ) {
            String quan = "";
            for(UserChemicalStockDto usedChemi : chemiList) {
                col = 0;
                quan = "";
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, col, col + 1));
                setCellStyle(contentRow, contentRow, col, col + 1, dataLVTStyle);
                setText(contentRow, col, "??????", dataLStyle);
                col += 2;

                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, col, col + 2));
                setCellStyle(contentRow, contentRow, col, col + 2, dataLVTStyle);
                setText(contentRow, col, usedChemi.getChemicalNm(), dataLStyle);
                col += 3;

                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, col, col + 2));
                setCellStyle(contentRow, contentRow, col, col + 2, dataLVTStyle);
                setText(contentRow, col, usedChemi.getMakerNm() != null ? usedChemi.getMakerNm() : "", dataLStyle);
                col += 3;

                quan += (usedChemi.getQuan() != null ? usedChemi.getQuan() : "0");
                quan += (usedChemi.getPackTCdNm() != null ? " " + usedChemi.getPackTCdNm() : "");
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, col, col + 1));
                setCellStyle(contentRow, contentRow, col, col + 1, dataLVTStyle);
                setText(contentRow, col, quan, dataRStyle);

                contentRow++;
            }
        }

        col = 0;
        if (manureList != null && manureList.size() > 0) {
            String quan = "";
            for(UserManureStockDto usedManure : manureList) {
                col = 0;
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, col, col + 1));
                setCellStyle(contentRow, contentRow, col, col + 1, dataLVTStyle);
                setText(contentRow, col, "??????", dataLStyle);
                col += 2;
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, col, col + 2));
                setCellStyle(contentRow, contentRow, col, col + 2, dataLVTStyle);
                setText(contentRow, col, usedManure.getManureNm(), dataLStyle);
                col += 3;
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, col, col + 2));
                setCellStyle(contentRow, contentRow, col, col + 2, dataLVTStyle);
                setText(contentRow, col, usedManure.getMakerNm() != null ? usedManure.getMakerNm() : "", dataLStyle);
                col += 3;
                quan += (usedManure.getQuan() != null ? usedManure.getQuan() : "0");
                quan += (usedManure.getPackTCdNm() != null ? " " + usedManure.getPackTCdNm() : "");
                sheet.addMergedRegion(new CellRangeAddress(contentRow, contentRow, col, col + 1));
                setCellStyle(contentRow, contentRow, col, col + 1, dataLVTStyle);
                setText(contentRow, col, quan, dataRStyle);
                contentRow++;
            }
        }

        // ?????????, ?????????, ?????????(??????)
        return contentRow;
    }

    private void drawValue(UserDiaryDto vo, int contentRow, HSSFWorkbook wb, HSSFSheet sheet, int pictureRow) {
        int col = 0;
        String cropNm = "";

        cropNm = vo.getCropCdNm() != null ? vo.getCropCdNm()  : "";
        if (vo.getUserCropAliasNm() != null && vo.getUserCropAliasNm().isEmpty() == false) {
            cropNm += (" (" + vo.getUserCropAliasNm() + ")");
        }
        setText(contentRow, col++, cropNm, dataLStyle);
        col += 2;
        setText(contentRow, col++, vo.getFmwrkCdNm(), dataLStyle);
        col += 2;
        setNumber2(contentRow, col++, vo.getManSelf() != null && vo.getManSelf() != 0 ? vo.getManSelf() : "");
        setNumber2(contentRow, col++, vo.getManSelfTm() != null && vo.getManSelfTm() != 0 ? vo.getManSelfTm() : "");
        setNumber2(contentRow, col++, vo.getManSelfTmm() != null && vo.getManSelfTmm() != 0 ? vo.getManSelfTmm() : "");
        setNumber2(contentRow, col++, vo.getWomanSelf() != null && vo.getWomanSelf() != 0 ? vo.getWomanSelf() : "");
        setNumber2(contentRow, col++, vo.getWomanSelfTm() != null && vo.getWomanSelfTm() != 0 ? vo.getWomanSelfTm() : "");
        setNumber2(contentRow, col++, vo.getWomanSelfTmm() != null && vo.getWomanSelfTmm() != 0 ? vo.getWomanSelfTmm() : "");
        setNumber2(contentRow, col++, vo.getManHire() != null && vo.getManHire() != 0 ? vo.getManHire() : "");
        setNumber2(contentRow, col++, vo.getManHireTm() != null && vo.getManHireTm() != 0 ? vo.getManHireTm() : "");
        setNumber2(contentRow, col++, vo.getManHireTmm() != null && vo.getManHireTmm() != 0 ? vo.getManHireTmm() : "");
        setNumber2(contentRow, col++, vo.getWomanHire() != null && vo.getWomanHire() != 0 ? vo.getWomanHire() : "");
        setNumber2(contentRow, col++, vo.getWomanHireTm() != null && vo.getWomanHireTm() != 0 ? vo.getWomanHireTm() : "");
        setNumber2(contentRow, col++, vo.getWomanHireTmm() != null && vo.getWomanHireTmm() != 0 ? vo.getWomanHireTmm() : "");
        setText(contentRow, col++, comma(vo.getQuan()) + (vo.getPackTCdNm() != null ? vo.getPackTCdNm() : "") + (vo.getGradeTCdNm() != null ? "("+vo.getGradeTCdNm()+")" : ""), dataRStyle);
        contentRow++;
        col = 1;
        setText(contentRow, col, vo.getRemark(), dataLVTStyle);
    }

}
