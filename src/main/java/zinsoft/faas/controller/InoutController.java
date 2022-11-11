package zinsoft.faas.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
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

import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.service.UserInoutService;
import zinsoft.faas.view.InoutExcelView;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.exception.CodeMessageException;

@Controller
@RequestMapping("${api.prefix}/user-inout")
public class InoutController {

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @Autowired
    ServletContext servletContext;

    @Resource
    UserInoutService userInoutService;

//    @Resource
//    UserCropService userCropService;

    @GetMapping(value = "/excel")
    public ModelAndView excel(@RequestParam Map<String, Object> param, @PageableDefault Pageable pageable) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        if (!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
            param.put("userId", farmerInfo.getUserId());
        } else {
            param.put("adminYn", "Y");
        }

        List<UserInoutDto> list1 = null;
        List<UserInoutDto> list2 = null;
        String inoutCd = (String) param.get("inoutCd");

        param.put("orderBy", "ASC");

        param.put("inoutCd", UserInoutDto.INOUT_CD_INCOME);
        DataTablesResponse<UserInoutDto> page1 = userInoutService.page(param, pageable);
        list1 = page1.getItems();

        param.put("inoutCd", UserInoutDto.INOUT_CD_OUTGOING);
        DataTablesResponse<UserInoutDto> page2 = userInoutService.page(param, pageable);
        list2 = page2.getItems();
        ModelAndView mv = new ModelAndView(new InoutExcelView());

        mv.addObject("sheetName", "입출금관리");
        mv.addObject("cond", param);
        mv.addObject("list1", list1);
        mv.addObject("list2", list2);

        if (param.get("outputType") != null) {
            mv.addObject("outputType", param.get("outputType"));
        }

        return mv;
    }

    @PostMapping(value = "")
    @ResponseBody
    public Result insert(@Valid UserInoutDto dto) throws Exception {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        if (dto.getTrdDt() == null) {
            new CodeMessageException(Result.BAD_REQUEST);
        }
        String trdDt = dto.getTrdDt().replaceAll("[^0-9]", "");
        Date trdDate = null;
        try {
            trdDate = transFormat.parse(trdDt);
        } catch (ParseException e) {
            new CodeMessageException(Result.BAD_REQUEST, "날짜 오류");
        }

        dto.setTrdDt(trdDt);
        dto.setUserId(farmerInfo.getUserId());

//        if (dto.getUserCropSeq() > 0) {
//            boolean res = userCropService.isExistUserCropId(dto.getUserId(), dto.getUserCropSeq());
//            if (!res) {
//                throw new CodeMessageException(Result.NOT_FOUND_USER_CROP);
//            }
//        }

        userInoutService.insert(dto);

        return new Result(true, Result.OK);
    }

    @GetMapping(value = "/{userInoutSeq}")
    @ResponseBody
    public Result get(@PathVariable("userInoutSeq") Long userInoutSeq) throws Exception {
        Result result = new Result(true, Result.OK);
        String userId = null;
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        if (userInoutSeq == null || userInoutSeq == 0) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        if (!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
            userId = farmerInfo.getUserId();
        }

        result.setBody(userInoutService.get(userId, userInoutSeq));

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

        DataTablesResponse<UserInoutDto> page = userInoutService.page(search, pageable);

        if (search.get(pageSizeParameter) != null) {
            return new Result(true, Result.OK, page);
        } else {
            return new Result(true, Result.OK, page.getItems());
        }
    }

    @PutMapping(value = "/{userInoutSeq}")
    @ResponseBody
    public Result update(@PathVariable("userInoutSeq") Long userInoutSeq, @Valid UserInoutDto dto) throws Exception {

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

//        if (UserInfoUtil.isManager()) {
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        if (!UserInfoUtil.isAdmin()) {
            dto.setUserId(farmerInfo.getUserId());
        }

        userInoutService.update(dto);

        return new Result(true, Result.OK);
    }

    @DeleteMapping(value = "/{userInoutSeq}")
    @ResponseBody
    public Result delete(@PathVariable("userInoutSeq") Long userInoutSeq) throws Exception {
        String userId = null;
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

//        if (UserInfoUtil.isManager()) {
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        if (!UserInfoUtil.isAdmin()) {
            userId = farmerInfo.getUserId();
            if (StringUtils.isBlank(userId)) {
                throw new CodeMessageException(Result.UNAUTHORIZED);
            }
        }

        userInoutService.delete(userId, userInoutSeq);
        return new Result(true, Result.OK);
    }

    @DeleteMapping(value = "")
    @ResponseBody
    public Result delete(Long[] userInoutSeqs) throws Exception {
        if (userInoutSeqs == null || userInoutSeqs.length == 0) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

//        if (UserInfoUtil.isManager()) {
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        String userId = null;
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        if (!UserInfoUtil.isAdmin()) {
            userId = farmerInfo.getUserId();
            if (StringUtils.isBlank(userId)) {
                throw new CodeMessageException(Result.UNAUTHORIZED);
            }
        }

        userInoutService.delete(userId, userInoutSeqs);

        return new Result(true, "0000");
    }

    @GetMapping(value = "/countByAcId")
    @ResponseBody
    public Result countByActNm(String trdDt) throws Exception {
        String userId = null;
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        if (!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
            userId = farmerInfo.getUserId();
        }

        List<Map<String, Object>> list = userInoutService.countByAcId(userId, trdDt);
        return new Result(true, Result.OK, list);
    }

    @PutMapping(value = "/r/{userInoutSeq}")
    @ResponseBody
    public Result insertReversing(@PathVariable("userInoutSeq") Long userInoutSeq, UserInoutDto rDto) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

//        if (UserInfoUtil.isManager()) {
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        if (!UserInfoUtil.isAdmin()) {
            rDto.setUserId(farmerInfo.getUserId());
        }

        if(rDto.getRTrdDt() == null) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        if(rDto.getRInoutTCd() == null) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        userInoutService.updateReversing(userInoutSeq, rDto);

        return new Result(true, "0000");
    }

    @DeleteMapping(value = "/r/{userInoutSeq}")
    @ResponseBody
    public Result deleteReversing(@PathVariable("userInoutSeq") Long userInoutSeq) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        String userId = null;

//        if (UserInfoUtil.isManager()) {
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        if (!UserInfoUtil.isAdmin()) {
            userId = farmerInfo.getUserId();
        }

        userInoutService.deleteReversing(userId, userInoutSeq);

        return new Result(true, "0000");
    }

}
