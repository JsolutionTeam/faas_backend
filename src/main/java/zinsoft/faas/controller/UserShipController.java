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

import zinsoft.faas.dto.UserShipDto;
import zinsoft.faas.service.UserShipService;
import zinsoft.faas.view.UserShipExcelView;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.exception.CodeMessageException;

/**
 * 사용중인 API입니다.
 */

@Controller
@RequestMapping("${api.prefix}/user-ship")
public class UserShipController {

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @Resource
    UserShipService userShipService;

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public ModelAndView excel(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable) throws Exception {
        if(!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        DataTablesResponse<UserShipDto> page = userShipService.page(search, pageable);

        ModelAndView mv = new ModelAndView(new UserShipExcelView());

        mv.addObject("sheetName", "출하량관리");
        mv.addObject("cond", search);
        mv.addObject("list", page.getItems());

        return mv;
    }

    @PostMapping(value = "")
    @ResponseBody
    public Result insert(@Valid UserShipDto dto) throws Exception {
//        if(UserInfoUtil.isAdmin() || UserInfoUtil.isManager() ) {
//            return new Result(false, Result.FORBIDDEN);
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        dto.setUserId(farmerInfo.getUserId());

        userShipService.insert(dto);

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

        DataTablesResponse<UserShipDto> page = userShipService.page(search, pageable);

        if (search.get(pageSizeParameter) != null) {
            return new Result(true, Result.OK, page);
        } else {
            return new Result(true, Result.OK, page.getItems());
        }
    }

    @GetMapping(value = "/{userShipSeq}")
    @ResponseBody
    public Result get(@PathVariable("userShipSeq") Long userShipSeq) throws Exception {

        String userId = null;
        if(!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            userId = farmerInfo.getUserId();
        }

        UserShipDto dto = userShipService.get(userShipSeq, userId);

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

        List<Map<String, Object>> list = userShipService.chartByPlanTCd(search);

        return new Result(true, Result.OK, list);
    }

    @PutMapping(value = "/{userShipSeq}")
    @ResponseBody
    public Result update(@PathVariable("userShipSeq") Long userCropSeq, @Valid UserShipDto dto) throws Exception {
//        if(UserInfoUtil.isManager() ) {
//            return new Result(false, Result.FORBIDDEN);
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        if (!UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            dto.setUserId(farmerInfo.getUserId());
        }

        userShipService.update(dto);

        return new Result(true, Result.OK);
    }

    @DeleteMapping(value = "/{userShipSeq}")
    @ResponseBody
    public Result delete(@PathVariable("userShipSeq") Long userShipSeq) throws Exception {
//        if (UserInfoUtil.isManager()) {
            //            return new Result(false, Result.FORBIDDEN);
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        String userId = null;
        if (!UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            userId = farmerInfo.getUserId();
        }

        userShipService.delete(userId, userShipSeq);
        return new Result(true, Result.OK);
    }

    @DeleteMapping(value = "")
    @ResponseBody
    public Result delete(Long[] userShipSeqs) throws Exception {
        String userId = null;

        if (userShipSeqs == null || userShipSeqs.length == 0) {
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

        userShipService.delete(userId, userShipSeqs);
        return new Result(true, Result.OK);
    }

}
