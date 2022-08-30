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

import zinsoft.faas.dto.UserManureDto;
import zinsoft.faas.service.UserManureService;
import zinsoft.faas.view.UserManureExcelView;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;

@Controller
@RequestMapping("${api.prefix}/user-manure")
public class UserManureController {

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @Resource
    UserManureService userManureService;

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public ModelAndView excel(@RequestParam Map<String, Object> search) throws Exception {
        if(!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        List<UserManureDto> list = userManureService.list(search);
        ModelAndView mv = new ModelAndView(new UserManureExcelView());

        mv.addObject("sheetName", "비료관리");
        mv.addObject("cond", search);
        mv.addObject("list", list);

        return mv;
    }

    @PostMapping(value = "")
    @ResponseBody
    public Result insert(@Valid UserManureDto dto) throws Exception {
        if(UserInfoUtil.isAdmin() || UserInfoUtil.isManager()) {
            return new Result(false, Result.FORBIDDEN);
        }

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        dto.setUserId(farmerInfo.getUserId());

        userManureService.insert(dto);

        return new Result(true, Result.OK, dto.getUserManureSeq());
    }

    @GetMapping(value = "")
    @ResponseBody
    public Result page(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable) throws Exception {
        if(!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            search.put("userId", farmerInfo.getUserId());
        }
        search.put("orderBy", "DESC");

        DataTablesResponse<UserManureDto> page = userManureService.page(search, pageable);

        if (search.get(pageSizeParameter) != null) {
            return new Result(true, Result.OK, page);
        } else {
            return new Result(true, Result.OK, page.getItems());
        }
    }

    @GetMapping(value = "/{userManureSeq}")
    @ResponseBody
    public Result get(@PathVariable("userManureSeq") Long userManureSeq) throws Exception {
        UserManureDto dto = new UserManureDto();
        dto.setUserManureSeq(userManureSeq);

        if(!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            dto.setUserId(farmerInfo.getUserId());
        }

        return new Result(true, Result.OK, userManureService.get(dto));
    }

    @PutMapping(value = "/{userManureSeq}")
    @ResponseBody
    public Result update(@PathVariable("userManureSeq") Long userCropSeq, @Valid UserManureDto dto) throws Exception {
        if( UserInfoUtil.isManager()) {
            return new Result(false, Result.FORBIDDEN);
        }

        if (!UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            dto.setUserId(farmerInfo.getUserId());
        }

        userManureService.update(dto);

        return new Result(true, Result.OK);
    }

    @DeleteMapping(value = "/{userManureSeq}")
    @ResponseBody
    public Result delete(@PathVariable("userManureSeq") Long userManureSeq) throws Exception {

        if( UserInfoUtil.isManager()) {
            return new Result(false, Result.FORBIDDEN);
        }

        String userId = null;
        if (!UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            userId = farmerInfo.getUserId();
        }

        userManureService.delete(userId, userManureSeq);
        return new Result(true, Result.OK);
    }

}
