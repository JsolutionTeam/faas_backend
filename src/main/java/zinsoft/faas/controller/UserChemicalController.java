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

import zinsoft.faas.dto.UserChemicalDto;
import zinsoft.faas.service.UserChemicalService;
import zinsoft.faas.view.UserChemicalExcelView;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;

@Controller
@RequestMapping("${api.prefix}/user-chemical")
public class UserChemicalController {

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @Resource
    UserChemicalService userChemicalService;

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public ModelAndView excel(@RequestParam Map<String, Object> search) throws Exception {
        if(!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        List<UserChemicalDto> list = userChemicalService.list(search);
        ModelAndView mv = new ModelAndView(new UserChemicalExcelView());

        mv.addObject("sheetName", "농약관리");
        mv.addObject("cond", search);
        mv.addObject("list", list);

        return mv;
    }

    @GetMapping(value = "/{userChemicalSeq}")
    @ResponseBody
    public Result get(@PathVariable("userChemicalSeq") Long userChemicalSeq) throws Exception {

        UserChemicalDto dto = new UserChemicalDto();
        dto.setUserChemicalSeq(userChemicalSeq);
        return new Result(true, Result.OK, userChemicalService.get(dto));
    }

    @PostMapping(value = "")
    @ResponseBody
    public Result insert(@Valid UserChemicalDto dto) throws Exception {
        if(UserInfoUtil.isManager() || UserInfoUtil.isAdmin() ) {
            return new Result(false, Result.FORBIDDEN);
        }

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        dto.setUserId( farmerInfo.getUserId());

        userChemicalService.insert(dto);

        return new Result(true, Result.OK, dto.getUserChemicalSeq());
    }

    @GetMapping(value = "")
    @ResponseBody
    public Result page(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable, HttpSession session) throws Exception {
        if(!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            search.put("userId", farmerInfo.getUserId());
            search.put("orderBy", "DESC");
        }

        DataTablesResponse<UserChemicalDto> page = userChemicalService.page(search, pageable);

        if (search.get(pageSizeParameter) != null) {
            return new Result(true, Result.OK, page);
        } else {
            return new Result(true, Result.OK, page.getItems());
        }
    }

    @PutMapping(value = "/{userChemicalSeq}")
    @ResponseBody
    public Result put(@PathVariable("userChemicalSeq") Long userChemicalSeq, @Valid UserChemicalDto dto) throws Exception {
        if(UserInfoUtil.isManager() ) {
            return new Result(false, Result.FORBIDDEN);
        }

        if (!UserInfoUtil.isAdmin() ) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            dto.setUserId(farmerInfo.getUserId());
        }

        userChemicalService.update(dto);

        return new Result(true, "0000");
    }

    @DeleteMapping(value = "/{userChemicalSeq}")
    @ResponseBody
    public Result delete(@PathVariable("userChemicalSeq") Long userChemicalSeq, String userId) throws Exception {
        if(UserInfoUtil.isManager() ) {
            return new Result(false, Result.FORBIDDEN);
        }

        if (!UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            userId = farmerInfo.getUserId();
        }

        userChemicalService.delete(userId, userChemicalSeq);
        return new Result(true, "0000");
    }

    @DeleteMapping(value = "")
    @ResponseBody
    public Result deleteByUserId(String userId) throws Exception {
        userChemicalService.deleteByUserId(userId);
        return new Result(true, "0000");
    }

}








