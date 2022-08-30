package zinsoft.faas.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import zinsoft.faas.dto.CropActivityDto;
import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.service.CropActivityService;
import zinsoft.faas.service.UserDiaryService;
import zinsoft.faas.service.UserInoutService;
import zinsoft.faas.view.DiarySimpleExcelAdminView;
import zinsoft.faas.view.DiarySimpleExcelView;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.FileInfoService;
import zinsoft.web.exception.CodeMessageException;

/**
 * @apiNote  영농일지 관리 컨트롤러 
 */
@Controller
@RequestMapping("${api.prefix}/user-diary")
public class DiaryController {

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @Autowired
    ServletContext servletContext;

    // 임플 파일 => UserDiaryServiceImpl
    @Resource
    UserDiaryService userDiaryService;

    @Resource
    UserInoutService userInoutService;

    //    @Resource
    //    UserCropService userCropService;

    @Resource
    FileInfoService fileInfoService;

    @Resource
    CropActivityService cropActivityService;

    @GetMapping(value = "/excel")
    public ModelAndView excel(@RequestParam Map<String, Object> param, @PageableDefault Pageable pageable) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        View view = null;
        if (!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
            param.put("userId", farmerInfo.getUserId());
        }
        param.put("orderBy", "DESC");
        DataTablesResponse<UserDiaryDto> dataTablesList = userDiaryService.page(param, pageable);
        List<UserDiaryDto> list = dataTablesList.getItems();

        //if (param.get("isSimple").equals("Y")) {
            view = ((!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager())) ? new DiarySimpleExcelView() : new DiarySimpleExcelAdminView();
//        } else {
//            view = (!UserInfoUtil.isManager(request)) ? new DiaryExcelView() : new DiaryExcelAdminView();
//        }

        ModelAndView mv = new ModelAndView(view);

        //수입, 지출내역 리스트
//        param.put("sTrdDt", param.get("sActDt"));
//        param.put("eTrdDt", param.get("eActDt"));
//        param.put("inoutCd", "I");
//        DataTablesResponse<UserInoutDto> page = userInoutService.page(param, null);
//        List<UserInoutDto> inList = page.getItems();
//
//        List<UserInoutDto> inListByDate = null;
//        for (UserDiaryDto vo : list) {
//            UserInoutDto sumVo = new UserInoutDto();
//            long sumAmt = 0;
//            inListByDate = new ArrayList<>();
//            for (UserInoutDto inVo : inList) {
//                if (vo.getActDt().equals(inVo.getTrdDt())) {
//                    inListByDate.add(inVo);
//                    sumAmt += inVo.getAmt();
//                }
//            }
//            sumVo.setCropNm("합계");
//            sumVo.setAmt(sumAmt);
//            inListByDate.add(sumVo);
//
//            vo.setUserInList(inListByDate);
//        }
//
//        param.put("inoutCd", "O");
//        page = userInoutService.page(param, null);
//        List<UserInoutDto> outList = page.getItems();
//
//        List<UserInoutDto> outListByDate = null;
//        for (UserDiaryDto vo : list) {
//            UserInoutDto sumVo = new UserInoutDto();
//            long sumAmt = 0;
//            outListByDate = new ArrayList<>();
//            for (UserInoutDto inVo : outList) {
//                if (vo.getActDt().equals(inVo.getTrdDt())) {
//                    outListByDate.add(inVo);
//                    sumAmt += inVo.getAmt();
//                }
//            }
//            sumVo.setCropNm("합계");
//            sumVo.setAmt(sumAmt);
//            outListByDate.add(sumVo);
//
//            vo.setUserOutList(outListByDate);
//        }

        mv.addObject("sheetName", "영농일지관리");
        mv.addObject("cond", param);
        mv.addObject("list", list);

        if (param.get("outputType") != null) {
            mv.addObject("outputType", param.get("outputType"));
        }

        return mv;
    }

    /**
     * 영농일지 등록 매핑
     * @param actDt 영농일지 폼의 시작(왼쪽)일자
     * @param actEdDt 영농일지 폼의 끝(오른쪽)일자
     * @param dto DTO
     * @return 결과
     * @throws Exception
     */
    @PostMapping(value = "")
    @ResponseBody
    public Result insert(String actDt, String actEdDt, @Valid UserDiaryDto dto) throws Exception {

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        if (actDt == null || actEdDt == null) {
            new CodeMessageException(Result.BAD_REQUEST);
        }
        // 일자 데이터에서 숫자 외에 제거한다.
        actDt = actDt.replaceAll("[^0-9]", "");
        actEdDt = actEdDt.replaceAll("[^0-9]", "");

        Date actSt = null;
        Date actEd = null;
        try {
            // String(20220830) -> Date(2022-08-30...)로 변경
            actSt = transFormat.parse(actDt);
            actEd = transFormat.parse(actEdDt);
        } catch (ParseException e) {
            throw new CodeMessageException(Result.BAD_REQUEST, "날짜 오류");
        }

        if ((actEd.getTime() - actSt.getTime()) > 14 * 24 * 60 * 60 * 1000) { // 2주
            throw new CodeMessageException(Result.TOO_LONG_TERM);
        }

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        dto.setUserId(farmerInfo.getUserId());

        //        if (vo.getUserCropSeq() > 0) {
        //            boolean res = userCropService.isExistUserCropId(vo.getUserId(), vo.getUserCropSeq());
        //            if (!res) {
        //                throw new CodeMessageException(Result.NOT_FOUND_USER_CROP);
        //            }
        //        }

        userDiaryService.insert(actDt, actEdDt, dto);

        return new Result(true, Result.OK);
    }

    @GetMapping(value = "/{userDiarySeq}")
    @ResponseBody
    public Result get(@PathVariable("userDiarySeq") Long userDiarySeq, HttpServletResponse response) throws Exception {
        Result result = new Result(true, Result.OK);
        String userId = null;

        if (userDiarySeq == null || userDiarySeq == 0) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        if (!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            userId = farmerInfo.getUserId();
        }

        result.setBody(userDiaryService.get(userId, userDiarySeq));

        return result;
    }

    @GetMapping(value = "")
    @ResponseBody
    public Result page(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        if (UserInfoUtil.isAdmin() || UserInfoUtil.isManager()) {
            search.put("orderBy", "DESC");
        } else {
            search.put("userId", farmerInfo.getUserId());
        }

        DataTablesResponse<UserDiaryDto> page = userDiaryService.page(search, pageable);

        if (search.get(pageSizeParameter) != null) {
            return new Result(true, Result.OK, page);
        } else {
            return new Result(true, Result.OK, page.getItems());
        }
    }

    @GetMapping(value = "/list-year")
    @ResponseBody
    public Result listYear(String diaryTCd, String year) throws Exception {
        String userId = null;

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        if (!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
            userId = farmerInfo.getUserId();
        }
        List<String> list = userDiaryService.listYear(userId, diaryTCd, year);

        return new Result(true, Result.OK, list);
    }

    @PutMapping(value = "/{userDiarySeq}")
    @ResponseBody
    public Result update(@PathVariable("userDiarySeq") Long userDiarySeq, @Valid UserDiaryDto dto) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        if (UserInfoUtil.isManager()) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        if (!UserInfoUtil.isAdmin()) {
            dto.setUserId(farmerInfo.getUserId());
        }
        userDiaryService.update(dto);

        return new Result(true, Result.OK);
    }

    @DeleteMapping(value = "/{userDiarySeq}")
    @ResponseBody
    public Result delete(@PathVariable("userDiarySeq") Long userDiarySeq) throws Exception {
        String userId = null;
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        if (UserInfoUtil.isManager()) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        if (!UserInfoUtil.isAdmin()) {
            userId = farmerInfo.getUserId();
            if (StringUtils.isBlank(userId)) {
                throw new CodeMessageException(Result.UNAUTHORIZED);
            }
        }

        userDiaryService.delete(userId, userDiarySeq);
        return new Result(true, Result.OK);
    }

    @DeleteMapping(value = "")
    @ResponseBody
    public Result deleteMultiple(Long[] userDiarySeqs) throws Exception {
        if (userDiarySeqs == null || userDiarySeqs.length == 0) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        if (UserInfoUtil.isManager()) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }
        String userId = null;
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        if (!UserInfoUtil.isAdmin()) {
            userId = farmerInfo.getUserId();
            if (StringUtils.isBlank(userId)) {
                throw new CodeMessageException(Result.UNAUTHORIZED);
            }
        }

        userDiaryService.delete(userId, userDiarySeqs);

        return new Result(true, Result.OK);
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping(value = "/activity")
    @ResponseBody
    public Result activities(@RequestParam(value = "activityTCd", defaultValue = "9") Long activityTCd, HttpServletResponse response) throws Exception {
        List<CropActivityDto> cropActivities = null;
        if (activityTCd == null || activityTCd == 0) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        cropActivities = cropActivityService.listByActivityTCd(activityTCd);

        return new Result(true, Result.OK, cropActivities);
    }

    @GetMapping(value = "/countByActSeq")
    @ResponseBody
    public Result countByActNm(String actDt) throws Exception {
        String userId = null;
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        if (!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
            userId = farmerInfo.getUserId();
        }

        List<Map<String, Object>> list = userDiaryService.countByActSeq(userId, actDt);
        return new Result(true, Result.OK, list);
    }

}
