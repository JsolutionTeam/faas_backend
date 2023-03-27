package zinsoft.faas.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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

import zinsoft.faas.dto.UserChemicalStockDto;
import zinsoft.faas.dto.UserManureStockDto;
import zinsoft.faas.service.UserChemicalStockService;
import zinsoft.faas.service.UserManureStockService;
import zinsoft.faas.view.UserStockExcelView;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.exception.CodeMessageException;

/**
 * 사용중인 API입니다.
 */

@Controller
@RequestMapping("${api.prefix}/user-stock")
public class UserStockController {

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @Resource
    UserChemicalStockService userChemicalStockService;

    @Resource
    UserManureStockService userManureStockService;



    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public ModelAndView excel(@RequestParam Map<String, Object> search) throws Exception {
        if(!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            search.put("userId", farmerInfo.getUserId());
        }
        search.put("orderBy", "ASC");

        List<UserChemicalStockDto> chemicalList = userChemicalStockService.list(search);
        List<UserManureStockDto> manureList = userManureStockService.list(search);

        ModelAndView mv = new ModelAndView(new UserStockExcelView());

        mv.addObject("sheetName", "농자재재고관리");
        mv.addObject("cond", search);
        mv.addObject("chemicalList", chemicalList);
        mv.addObject("manureList", manureList);

        return mv;
    }

    @GetMapping(value = "/chemical/{userChemicalStockSeq}")
    @ResponseBody
    public Result getChemical(@PathVariable("userChemicalStockSeq") Long userChemicalStockSeq) throws Exception {

            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        return new Result(true, Result.OK, userChemicalStockService.get(farmerInfo.getUserId(), userChemicalStockSeq));
    }

    @PostMapping(value = "/chemical")
    @ResponseBody
    public Result insertChemical(@Valid UserChemicalStockDto dto) throws Exception {
//        if(UserInfoUtil.isManager() || UserInfoUtil.isAdmin() ) {
            //            return new Result(false, Result.FORBIDDEN);
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        dto.setUserId( farmerInfo.getUserId());

        userChemicalStockService.insertWith(dto);

        return new Result(true, Result.OK);
    }

    @GetMapping(value = "/chemical")
    @ResponseBody
    public Result pageChemical(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable, HttpSession session) throws Exception {
        if(!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        DataTablesResponse<UserChemicalStockDto> page = userChemicalStockService.page(search, pageable);

        if (search.get(pageSizeParameter) != null) {
            return new Result(true, Result.OK, page);
        } else {
            return new Result(true, Result.OK, page.getItems());
        }
    }

    @GetMapping(value = "/chemical/list")
    @ResponseBody
    public List<UserChemicalStockDto> listChemical(@RequestParam Map<String, Object> search) throws Exception {
        if(!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        return userChemicalStockService.list(search);
    }

    @PutMapping(value = "/chemical/{userChemicalStockSeq}")
    @ResponseBody
    public Result putChemical(@PathVariable("userChemicalStockSeq") Long userChemicalStockSeq, @Valid UserChemicalStockDto dto) throws Exception {
//        if(UserInfoUtil.isManager() ) {
            //            return new Result(false, Result.FORBIDDEN);
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        if(!UserInfoUtil.isAdmin() ) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            dto.setUserId(farmerInfo.getUserId());
        }

        userChemicalStockService.updateWith(dto);

        return new Result(true, "0000");
    }

    @DeleteMapping(value = "/chemical/{userChemicalStockSeq}")
    @ResponseBody
    public Result deleteChemical(@PathVariable("userChemicalStockSeq") Long userChemicalStockSeq) throws Exception {
        if(UserInfoUtil.isManager() ) {
            //            return new Result(false, Result.FORBIDDEN);
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        String userId = null;
        if(!UserInfoUtil.isAdmin() ) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            userId = farmerInfo.getUserId();
        }

        userChemicalStockService.deleteWith(userId, userChemicalStockSeq);
        return new Result(true, "0000");
    }

    @DeleteMapping(value = "/chemical")
    @ResponseBody
    public Result deleteChemicalByUserId(String userId) throws Exception {
        userChemicalStockService.deleteByUserId(userId);
        return new Result(true, "0000");
    }

    @GetMapping(value = "/manure/{userManureStockSeq}")
    @ResponseBody
    public Result getManure(@PathVariable("userManureStockSeq") Long userManureStockSeq) throws Exception {

            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        return new Result(true, "0000", userManureStockService.get(farmerInfo.getUserId(), userManureStockSeq));
    }

    @PostMapping(value = "/manure")
    @ResponseBody
    public Result insertManure(@Valid UserManureStockDto dto) throws Exception {
//        if(UserInfoUtil.isManager() ||  UserInfoUtil.isAdmin()) {
            //            return new Result(false, Result.FORBIDDEN);
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        dto.setUserId( farmerInfo.getUserId());

        userManureStockService.insertWith(dto);

        return new Result(true, Result.OK);
    }

    @GetMapping(value = "/manure")
    @ResponseBody
    public Result pageManure(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable, HttpSession session) throws Exception {
        if(!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        DataTablesResponse<UserManureStockDto> page = userManureStockService.page(search, pageable);

        if (search.get(pageSizeParameter) != null) {
            return new Result(true, Result.OK, page);
        } else {
            return new Result(true, Result.OK, page.getItems());
        }
    }

    @GetMapping(value = "/manure/list")
    @ResponseBody
    public List<UserManureStockDto> listManure(@RequestParam Map<String, Object> search) throws Exception {
        if(!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        return userManureStockService.list(search);
    }

    @PutMapping(value = "/manure/{userManureStockSeq}")
    @ResponseBody
    public Result putManure(@PathVariable("userManureStockSeq") Long userManureStockSeq, @Valid UserManureStockDto dto) throws Exception {
//        if(UserInfoUtil.isManager()) {
            //            return new Result(false, Result.FORBIDDEN);
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        if(!UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            dto.setUserId(farmerInfo.getUserId());
        }

        userManureStockService.updateWith(dto);

        return new Result(true, "0000");
    }

    @DeleteMapping(value = "/manure/{userManureStockSeq}")
    @ResponseBody
    public Result deleteManure(@PathVariable("userManureStockSeq") Long userManureStockSeq) throws Exception {

//        if(UserInfoUtil.isManager()) {
            //            return new Result(false, Result.FORBIDDEN);
//            throw new CodeMessageException(Result.FORBIDDEN);
//        }

        String userId = null;
        if(!UserInfoUtil.isAdmin()) {
                UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
            userId =farmerInfo.getUserId();
        }

        userManureStockService.deleteWith(userId, userManureStockSeq);
        return new Result(true, "0000");
    }

}