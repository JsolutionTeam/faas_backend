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
    UserInfoService userInfoService;

    @Resource
    CropService cropService;

    @Resource
    RoleService roleService;

    @Resource
    AccountService accountService;

    @Resource
    MenuService menuService;

    @Resource
    MenuRoleService menuRoleService;

    @Resource
    ActivityService activityService;

    @Resource
    CodeService codeService;

    @Resource
    SelfLaborCostService selfLaborCostService;

    @Resource
    UserEmpCostService userEmpCostService;

    @Resource
    BasicDataService basicDataService;

    @Resource
    CropActivityService cropActivityService;

    @Resource
    AdmZipcodeService admZipcodeService;

    @Resource
    RoleUserService roleUserService;

    @RequestMapping(value = "/content/004002001/excel", method = RequestMethod.GET)
    public ModelAndView excel004002001(@RequestParam Map<String, Object> param, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        List<CropDto> list = cropService.list(param);
        ModelAndView mv = new ModelAndView(new CropListExcelView());

        mv.addObject("sheetName", "품목관리");
        mv.addObject("cond", param);
        mv.addObject("list", list);

        return mv;
    }

    @RequestMapping(value = "/api/004002001/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002001insert(@Valid CropDto vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        cropService.insert(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/content/004002001/list", method = RequestMethod.GET)
    public String list004002001(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        //model.addAttribute("activityTCdList", cropActivityService.listActivityTCd());
        return "MAIN/004002001/list";
    }

    @RequestMapping(value = "/api/004002001/get", method = RequestMethod.GET)
    @ResponseBody
    public CropDto api004002001get(Long cropSeq, HttpServletRequest request, HttpSession session, Model model) throws Exception {

        return cropService.get(cropSeq);
    }

    @RequestMapping(value = "/api/004002001/get/list", method = RequestMethod.GET)
    @ResponseBody
    public List<CropDto> api004002001getList(@RequestParam Map<String, Object> param) throws Exception {
        log.info("품목 list 조회 param : {}", param);
        String statusCD = (String)param.get("statusCD");
        // statusCD가 안들어오면 기본값 N으로 지정함
        if(StringUtils.isBlank(statusCD)) {
            param.put("statusCD", "N");
        }
        return cropService.list(param);
    }

    @RequestMapping(value = "/api/004002001/getCropBCd", method = RequestMethod.GET)
    @ResponseBody
    public List<CodeDto> api004002001getCropBCd(String cropACd, HttpServletRequest request, HttpSession session, Model model) throws Exception {

        return codeService.listStartsWithCodeVal("CROP_B_CD", cropACd);
    }

//    @RequestMapping(value = "/api/004002001/page", method = RequestMethod.GET)
//    @ResponseBody
//    public Page<CropDto> api004002001page(PagingParam pagingParam, HttpServletRequest request, HttpSession session, Model model) throws Exception {
//        pagingParam.setCondition(request);
//        return cropService.page(pagingParam);
//    }

    @RequestMapping(value = "/api/004002001/update", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002001update(@Valid CropDto vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        cropService.update(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/004002001/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002001delete(Long[] cropSeqs, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (cropSeqs == null || cropSeqs.length == 0) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        cropService.delete(cropSeqs);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/content/004002002/excel", method = RequestMethod.GET)
    public ModelAndView excel004002002(PagingParam pagingParam, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        pagingParam.setCondition(request);

        List<ActivityDto> list = activityService.list(pagingParam);
        ModelAndView mv = new ModelAndView(new ActivityExcelView());

        mv.addObject("sheetName", "작업단계관리");
        mv.addObject("cond", pagingParam.getCond());
        mv.addObject("list", list);

        return mv;
    }

    @RequestMapping(value = "/content/004002003/excel", method = RequestMethod.GET)
    public ModelAndView excel004002003(PagingParam pagingParam, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        pagingParam.setCondition(request);

        List<AccountDto> list = accountService.list2(pagingParam);
        ModelAndView mv = new ModelAndView(new AccountListExcelView());

        mv.addObject("sheetName", "계정코드관리");
        mv.addObject("cond", pagingParam.getCond());
        mv.addObject("list", list);

        return mv;
    }

    @RequestMapping(value = "/api/004002003/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002003insert(@Valid AccountDto vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        String existAcId = accountService.getAcIdByAcNm(vo.getAcNm());
        if(existAcId != null && Integer.parseInt(existAcId) > 0) {
            return new Result(false, "9011");
        }

        accountService.insert(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/content/004002003/list", method = RequestMethod.GET)
    public String list004002003(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        model.addAttribute("acList", accountService.getRootAcIdList());
        model.addAttribute("bpTCdList", codeService.list("BP_T_CD"));
        model.addAttribute("cdTCdList", codeService.list("CD_T_CD"));
        model.addAttribute("costTCdList", codeService.list("COST_T_CD"));
        model.addAttribute("psTCdList", codeService.list("PS_T_CD"));

        return "MAIN/004002003/list";
    }

    @RequestMapping(value = "/api/004002003/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> get004002003(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("acList", accountService.getRootAcIdList());
        ret.put("bpTCdList", codeService.list("BP_T_CD"));
        ret.put("cdTCdList", codeService.list("CD_T_CD"));
        ret.put("costTCdList", codeService.list("COST_T_CD"));
        ret.put("psTCdList", codeService.list("PS_T_CD"));

        return ret;
    }

    @RequestMapping(value = "/api/004002003/get", method = RequestMethod.GET)
    @ResponseBody
    public AccountDto api004002003get(String acId, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        return accountService.get(acId);
    }

    @RequestMapping(value = "/api/004002003/page", method = RequestMethod.GET)
    @ResponseBody
    public Page<AccountDto> api004002003page(PagingParam pagingParam, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        pagingParam.setCondition(request);
        //return accountService.page(pagingParam);
        return null;
    }

    @RequestMapping(value = "/api/004002003/getAcList2", method = RequestMethod.GET)
    @ResponseBody
    public List<AccountDto> api004002003getAcList2(HttpServletRequest request, HttpSession session) throws Exception {
        String rootAcId = request.getParameter("rootAcId");
        List<AccountDto> list = accountService.getlvl2AcIdList(rootAcId);

        return list;
    }

    @RequestMapping(value = "/api/004002003/update", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002003update(@Valid AccountDto vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        String existAcId = accountService.getAcIdByAcNm(vo.getAcNm());
        if(existAcId != null && Integer.parseInt(existAcId) > 0 && vo.getAcId().equals(existAcId) == false) {
            return new Result(false, "9011");
        }

        accountService.update(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/004002003/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002003delete(String[] acIds, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (acIds == null || acIds.length == 0) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        accountService.delete(acIds);
        return new Result(true, "0000");
    }

    @RequestMapping(value = "/content/004002008/excel", method = RequestMethod.GET)
    public ModelAndView excel004002008(PagingParam pagingParam, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        pagingParam.setCondition(request);

        List<ActivityDto> list = activityService.listByCropACd(null, null);
        ModelAndView mv = new ModelAndView(new ActivityExcelView());

        mv.addObject("sheetName", "작업단계관리");
        mv.addObject("cond", pagingParam.getCond());
        mv.addObject("list", list);

        return mv;
    }

    @RequestMapping(value = "/api/004002008/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002008insert(@Valid ActivityDto dto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        Long existActivitySeq = activityService.getActivitySeqByActNm(dto.getActNm());
        if(existActivitySeq != null && existActivitySeq > 0) {
            return new Result(false, "9011");
        }

        activityService.insert(dto);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/004002008/get", method = RequestMethod.GET)
    @ResponseBody
    public ActivityDto api004002008get(Long activitySeq, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        return activityService.get(activitySeq);
    }

    @RequestMapping(value = "/api/004002008/page", method = RequestMethod.GET)
    @ResponseBody
    public Result api004002008list(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable) throws Exception {
        DataTablesResponse<ActivityDto> page = activityService.page(search, pageable);
        return new Result(true, Result.OK, page);
    }

    @RequestMapping(value = "/api/004002008/update", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002008update(@Valid ActivityDto dto, String cropIdUpdate, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        Long existActivitySeq = activityService.getActivitySeqByActNm(dto.getActNm());
        if(existActivitySeq != null && existActivitySeq > 0 && existActivitySeq.equals(dto.getActivitySeq()) == false) {
            return new Result(false, "9011");
        }
        activityService.update(dto);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/004002008/updateExprSeq", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002008updateExprSeq(@RequestBody List<ActivityDto> list, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        activityService.updateExprSeq(list);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/004002008/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002008delete(Long[] activitySeqs, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (activitySeqs == null || activitySeqs.length == 0) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        activityService.delete(activitySeqs);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/content/004002009/excel", method = RequestMethod.GET)
    public ModelAndView excel004002009(@RequestParam Map<String, Object> param, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        param.put("userId", farmerInfo.getUserId());

        List<UserEmpCostDto> list = userEmpCostService.list(param);
        ModelAndView mv = new ModelAndView(new UserEmpCostExcelView());

        mv.addObject("sheetName", "고용노력비관리");
        mv.addObject("cond", param);
        mv.addObject("list", list);

        if (param.get("outputType") != null) {
            mv.addObject("outputType", param.get("outputType"));
        }

        return mv;
    }

    @RequestMapping(value = "/api/004002009/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002009insert(@Valid UserEmpCostDto vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        vo.setUserId(farmerInfo.getUserId());
        userEmpCostService.insert(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/004002009/get", method = RequestMethod.GET)
    @ResponseBody
    public UserEmpCostDto api004002009list(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        return userEmpCostService.getByYear(farmerInfo.getUserId(), null);
        // return userEmpCostService.getByUserId(farmerInfo.getUserId());
    }

    @RequestMapping(value = "/api/004002009/getBySeq", method = RequestMethod.GET)
    @ResponseBody
    public UserEmpCostDto api004002009GetBySeq(Long userEmpCostSeq, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        //UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        return userEmpCostService.get(null, userEmpCostSeq);
        //return userEmpCostService.getByUserId(farmerInfo.getUserId());
    }

    @RequestMapping(value = "/api/004002009/chkValidYear", method = RequestMethod.GET)
    @ResponseBody
    public int api004002009checkValidYear(String year, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        return userEmpCostService.checkValidYear(year, farmerInfo.getUserId());
    }

    @RequestMapping(value = "/api/004002009/page", method = RequestMethod.GET)
    @ResponseBody
    public Page<UserEmpCostDto> api004002009page(PagingParam pagingParam, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        Map<String, String> cond = pagingParam.getCond();
        cond.put("userId", farmerInfo.getUserId());

        /*if (UserInfoUtil.isManager(request) && userInfo.getUserId().equals(farmerInfo.getUserId())) {
            cond.put("isAdmin", "Y");
        }*/

        pagingParam.setCond(cond);
        pagingParam.setCondition(request);
        return null;//userEmpCostService.page(pagingParam);
    }

    @RequestMapping(value = "/api/004002009/update", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002009update(@Valid UserEmpCostDto vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        vo.setUserId(farmerInfo.getUserId());
        userEmpCostService.update(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/004002009/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result api004002009delete(Long userEmpCostSeq, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        userEmpCostService.delete(null, userEmpCostSeq);
        return new Result(true, "0000");
    }

}
