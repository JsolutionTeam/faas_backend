package zinsoft.faas.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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

import zinsoft.faas.dto.UserEmpCostDto;
import zinsoft.faas.service.UserEmpCostService;
import zinsoft.faas.view.UserEmpCostExcelView;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.exception.CodeMessageException;

@Controller
@RequestMapping("${api.prefix}/user-emp-cost")
public class UserEmpCostController {

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @Resource
    UserEmpCostService userEmpCostService;

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public ModelAndView excel(@RequestParam Map<String, Object> search) throws Exception {
        if(!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        List<UserEmpCostDto> list = userEmpCostService.list(search);
        ModelAndView mv = new ModelAndView(new UserEmpCostExcelView());

        mv.addObject("sheetName", "고용노력비관리");
        mv.addObject("cond", search);
        mv.addObject("list", list);

        return mv;
    }

    @GetMapping(value = "/{userEmpCostSeq}")
    @ResponseBody
    public UserEmpCostDto get(@PathVariable("userEmpCostSeq") Long userEmpCostSeq) throws Exception {
        String userId = null;

        if(!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            userId = farmerInfo.getUserId();
        }

        return userEmpCostService.get(userId, userEmpCostSeq);
    }

    @PostMapping(value = "")
    @ResponseBody
    public Result insert(@Valid UserEmpCostDto dto, HttpServletResponse response) throws Exception {
        if(UserInfoUtil.isManager() || UserInfoUtil.isAdmin() ) {
            return new Result(false, Result.FORBIDDEN);
        }

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        dto.setUserId(farmerInfo.getUserId());

        int yearCnt = userEmpCostService.checkValidYear(dto.getYear(), dto.getUserId());
        if(yearCnt > 0) {
            throw new CodeMessageException(Result.ALREADY_COST);
        }

        userEmpCostService.insert(dto);

        return new Result(true, Result.OK);
    }

    @GetMapping(value = "")
    @ResponseBody
    public Result page(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable, HttpSession session) throws Exception {
        if(!UserInfoUtil.isManager() && !UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            search.put("userId", farmerInfo.getUserId());
        }

        DataTablesResponse<UserEmpCostDto> page = userEmpCostService.page(search, pageable);

        if (search.get(pageSizeParameter) != null) {
            return new Result(true, Result.OK, page);
        } else {
            return new Result(true, Result.OK, page.getItems());
        }
    }

    @PutMapping(value = "/{userEmpCostSeq}")
    @ResponseBody
    public Result put( @Valid UserEmpCostDto dto) throws Exception {
        if(UserInfoUtil.isManager() ) {
            return new Result(false, Result.FORBIDDEN);
        }

        if(!UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            dto.setUserId(farmerInfo.getUserId());
        }

        int yearCnt = userEmpCostService.checkValidYear(dto.getUserEmpCostSeq(), dto.getYear(), dto.getUserId());
        if(yearCnt > 0) {
            throw new CodeMessageException(Result.ALREADY_COST);
        }
        userEmpCostService.update(dto);

        return new Result(true, "0000");
    }

    @DeleteMapping(value = "/{userEmpCostSeq}")
    @ResponseBody
    public Result delete(@PathVariable("userEmpCostSeq")Long userEmpCostSeq) throws Exception {
        if(UserInfoUtil.isManager() ) {
            return new Result(false, Result.FORBIDDEN);
        }
        String userId = null;
        if(!UserInfoUtil.isAdmin()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            userId = farmerInfo.getUserId();
        }

        userEmpCostService.delete(userId, userEmpCostSeq);

        return new Result(true, "0000");
    }

    @GetMapping(value = "/year")
    @ResponseBody
    public List<Integer> year() throws Exception {
    	 UserInfoDto userInfo = UserInfoUtil.getFarmerInfo();

        return userEmpCostService.yearList(userInfo.getUserId());
    }
}
