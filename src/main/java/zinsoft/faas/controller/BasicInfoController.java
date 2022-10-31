package zinsoft.faas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import zinsoft.faas.dto.AccountDto;
import zinsoft.faas.dto.ActivityDto;
import zinsoft.faas.dto.CropDto;
import zinsoft.faas.dto.UserEmpCostDto;
import zinsoft.faas.service.AccountService;
import zinsoft.faas.service.ActivityService;
import zinsoft.faas.service.AdmZipcodeService;
import zinsoft.faas.service.CropActivityService;
import zinsoft.faas.service.CropService;
import zinsoft.faas.service.RoleUserService;
import zinsoft.faas.service.SelfLaborCostService;
import zinsoft.faas.service.UserEmpCostService;
import zinsoft.faas.view.AccountListExcelView;
import zinsoft.faas.view.ActivityExcelView;
import zinsoft.faas.view.CropListExcelView;
import zinsoft.faas.view.UserEmpCostExcelView;
import zinsoft.faas.vo.Page;
import zinsoft.faas.vo.PagingParam;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.BasicDataService;
import zinsoft.web.common.service.CodeService;
import zinsoft.web.common.service.MenuRoleService;
import zinsoft.web.common.service.MenuService;
import zinsoft.web.common.service.RoleService;
import zinsoft.web.common.service.UserInfoService;

@Controller
@Slf4j
public class BasicInfoController {

    @Resource
    CropService cropService;

    @Resource
    AccountService accountService;

    @Resource
    ActivityService activityService;

    @Resource
    CodeService codeService;

    @Resource
    UserEmpCostService userEmpCostService;


}
