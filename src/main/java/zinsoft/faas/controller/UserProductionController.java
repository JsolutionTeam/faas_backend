package zinsoft.faas.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import zinsoft.faas.dto.UserProductionDto;
import zinsoft.faas.service.UserProductionService;
import zinsoft.faas.view.UserProductionExcelView;
//import zinsoft.faas.view.UserProductionExcelView;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.exception.CodeMessageException;

/**
 * 사용중인 API입니다.
 */

@Controller
@RequestMapping("${api.prefix}/user-production")
public class UserProductionController {

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @Resource
    UserProductionService userProductionService;

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public ModelAndView excel(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable) throws Exception {

        if(!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        DataTablesResponse<UserProductionDto> page = userProductionService.page(search, pageable);

        ModelAndView mv = new ModelAndView(new UserProductionExcelView());

        mv.addObject("sheetName", "생산량관리");
        mv.addObject("cond", search);
        mv.addObject("list", page.getItems());

        return mv;
    }

    @PostMapping(value = "")
    @ResponseBody
    public Result insert(@Valid UserProductionDto dto) throws Exception {
//        if(UserInfoUtil.isManager() || UserInfoUtil.isAdmin()) {
//            return new Result(false, Result.FORBIDDEN);
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        dto.setUserId(farmerInfo.getUserId());

        userProductionService.insert(dto);

        return new Result(true, Result.OK);
    }

    @GetMapping(value = "")
    @ResponseBody
    public Result page(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable) throws Exception {
        if(!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        DataTablesResponse<UserProductionDto> page = userProductionService.page(search, pageable);

        if (search.get(pageSizeParameter) != null) {
            return new Result(true, Result.OK, page);
        } else {
            return new Result(true, Result.OK, page.getItems());
        }
    }

    @GetMapping(value = "/{userProductionSeq}")
    @ResponseBody
    public Result get(@PathVariable("userProductionSeq") Long userProductionSeq) throws Exception {

        String userId = null;

        if(!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            userId = farmerInfo.getUserId();
        }

        UserProductionDto dto = userProductionService.get(userProductionSeq, userId);

        return new Result(true, Result.OK, dto);
    }

    @GetMapping(value = "/chart")
    @ResponseBody
    public Result chart(@RequestParam Map<String, Object> search) throws Exception {

        if(!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        List<Map<String, Object>> list = userProductionService.chartByPlanTCd(search);

        return new Result(true, Result.OK, list);
    }

    @PutMapping(value = "/{userProductionSeq}")
    @ResponseBody
    public Result update(@PathVariable("userProductionSeq") Long userCropSeq, @Valid UserProductionDto dto) throws Exception {

//        if(UserInfoUtil.isManager() ) {
            //            return new Result(false, Result.FORBIDDEN);
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        if (!UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            dto.setUserId(farmerInfo.getUserId());
        }

        userProductionService.update(dto);

        return new Result(true, Result.OK);
    }

    @DeleteMapping(value = "/{userProductionSeq}")
    @ResponseBody
    public Result delete(@PathVariable("userProductionSeq") Long userProductionSeq) throws Exception {
        String userId = null;

//        if(UserInfoUtil.isManager() ) {
            //            return new Result(false, Result.FORBIDDEN);
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        if (!UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            userId = farmerInfo.getUserId();
        }

        userProductionService.delete(userId, userProductionSeq);
        return new Result(true, Result.OK);
    }

    @DeleteMapping(value = "")
    @ResponseBody
    public Result delete(Long[] userProductionSeqs) throws Exception {
        String userId = null;

        if (userProductionSeqs == null || userProductionSeqs.length == 0) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

//        if(UserInfoUtil.isManager() ) {
            //            return new Result(false, Result.FORBIDDEN);
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        if (!UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            userId = farmerInfo.getUserId();
        }

        userProductionService.delete(userId, userProductionSeqs);
        return new Result(true, Result.OK);
    }

}
