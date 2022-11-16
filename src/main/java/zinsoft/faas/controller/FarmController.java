package zinsoft.faas.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import zinsoft.faas.dto.UserChemicalDto;
import zinsoft.faas.dto.UserManureDto;
import zinsoft.faas.service.UserChemicalService;
import zinsoft.faas.service.UserChemicalStockService;
import zinsoft.faas.service.UserManureService;
import zinsoft.faas.service.UserManureStockService;
import zinsoft.faas.view.UserChemicalExcelView;
import zinsoft.faas.view.UserManureExcelView;
import zinsoft.faas.vo.PagingParam;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;

@Controller
public class FarmController {

    @Resource
    UserChemicalService userChemicalService;

    @Resource
    UserChemicalStockService userChemicalStockService;

    @Resource
    UserManureService userManureService;

    @Resource
    UserManureStockService userManureStockService;

    @RequestMapping(value = "/content/002005005/excel", method = RequestMethod.GET)
    public ModelAndView excel002005005(@RequestParam Map<String, Object> param, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();

        param.put("userId", farmerInfo.getUserId());
        param.put("orderBy", "DESC");

        /*if (UserInfoUtil.isManager(request) && userInfo.getUserId().equals(farmerInfo.getUserId())) {
            param.put("isAdmin", "Y");
        }*/

        List<UserChemicalDto> list = userChemicalService.list(param);
        ModelAndView mv = new ModelAndView(new UserChemicalExcelView());

        mv.addObject("sheetName", "농약");
        mv.addObject("cond", param);
        mv.addObject("list", list);

        if (param.get("outputType") != null) {
            mv.addObject("outputType", param.get("outputType"));
        }

        return mv;
    }

    @RequestMapping(value = "/api/002005005/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005005Insert(@Valid UserChemicalDto vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        vo.setUserId(farmerInfo.getUserId());

        userChemicalService.insert(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/002005005/page", method = RequestMethod.GET)
    @ResponseBody
    public Object api002005005Page(PagingParam pagingParam, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        Map<String, String> cond = pagingParam.getCond();
        cond.put("userId", farmerInfo.getUserId());
        cond.put("orderBy", "DESC");
        pagingParam.setCond(cond);
        pagingParam.setCondition(request);

        /* if (UserInfoUtil.isManager(request) && userInfo.getUserId().equals(farmerInfo.getUserId())) {
            cond.put("isAdmin", "Y");
        }*/

        return userChemicalService.page(pagingParam);
    }

    @RequestMapping(value = "/api/002005005/list", method = RequestMethod.GET)
    @ResponseBody
    public Object api002005005List(@RequestParam Map<String, Object> param, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();

        param.put("userId", farmerInfo.getUserId());
        param.put("orderBy", "DESC");

        /*if (UserInfoUtil.isManager(request) && userInfo.getUserId().equals(farmerInfo.getUserId())) {
            param.put("isAdmin", "Y");
        }*/

        return userChemicalService.list(param);
    }

    @RequestMapping(value = "/api/002005005/get", method = RequestMethod.GET)
    @ResponseBody
    public UserChemicalDto api002005005get(@Valid UserChemicalDto vo, HttpServletRequest request, HttpSession session, Model model) throws Exception {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        vo.setUserId(farmerInfo.getUserId());

        return userChemicalService.get(vo);
    }

    @RequestMapping(value = "/api/002005005/countUsed", method = RequestMethod.GET)
    @ResponseBody
    public Long api002005005countUsed(@Valid UserChemicalDto vo, HttpServletRequest request, HttpSession session, Model model) throws Exception {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        vo.setUserId(farmerInfo.getUserId());

        return userChemicalStockService.countUsed(farmerInfo.getUserId(), vo.getUserChemicalSeq());
    }

    @RequestMapping(value = "/api/002005005/update", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005005update(@Valid UserChemicalDto vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        vo.setUserId(farmerInfo.getUserId());

        userChemicalService.update(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/002005005/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005005Delete(Long[] userChemicalSeqs, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (userChemicalSeqs == null || userChemicalSeqs.length == 0) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        int usedCnt = 0;
        /*for (int i = 0, cnt = userChemicalSeqs.length; i < cnt; i++) {
            Long userChemicalSeq = userChemicalSeqs[i];
            usedCnt += userChemicalStockService.countUsed(farmerInfo.getUserId(), userChemicalSeq);
        }*/

        if (usedCnt > 0) {
            return new Result(false, "9995");
        }

        userChemicalService.delete(farmerInfo.getUserId(), userChemicalSeqs);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/content/002005006/excel", method = RequestMethod.GET)
    public ModelAndView excel002005006(@RequestParam Map<String, Object> param, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();

        param.put("userId", farmerInfo.getUserId());
        param.put("orderBy", "DESC");

        /* if (UserInfoUtil.isManager(request) && userInfo.getUserId().equals(farmerInfo.getUserId())) {
            param.put("isAdmin", "Y");
        }*/

        List<UserManureDto> list = userManureService.list(param);
        ModelAndView mv = new ModelAndView(new UserManureExcelView());

        mv.addObject("sheetName", "비료");
        mv.addObject("cond", param);
        mv.addObject("list", list);

        if (param.get("outputType") != null) {
            mv.addObject("outputType", param.get("outputType"));
        }

        return mv;
    }

    @RequestMapping(value = "/api/002005006/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005006Insert(@Valid UserManureDto vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        vo.setUserId(farmerInfo.getUserId());

        userManureService.insert(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/002005006/page", method = RequestMethod.GET)
    @ResponseBody
    public Object api002005006Page(PagingParam pagingParam, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
//        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
//            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
//        Map<String, String> cond = pagingParam.getCond();
//        cond.put("userId", farmerInfo.getUserId());
//        cond.put("orderBy", "DESC");
//        pagingParam.setCond(cond);
//        pagingParam.setCondition(request);
//
//        /* if (UserInfoUtil.isManager(request) && userInfo.getUserId().equals(farmerInfo.getUserId())) {
//            cond.put("isAdmin", "Y");
//        }*/
//
//        return userManureService.page(pagingParam);
        return null;
    }

    @RequestMapping(value = "/api/002005006/list", method = RequestMethod.GET)
    @ResponseBody
    public Object api002005006List(@RequestParam Map<String, Object> param, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();

        param.put("userId", farmerInfo.getUserId());
        param.put("orderBy", "DESC");

        /* if (UserInfoUtil.isManager(request) && userInfo.getUserId().equals(farmerInfo.getUserId())) {
            param.put("isAdmin", "Y");
        }*/

        return userManureService.list(param);
    }

    @RequestMapping(value = "/api/002005006/get", method = RequestMethod.GET)
    @ResponseBody
    public UserManureDto api002005006get(@Valid UserManureDto vo, HttpServletRequest request, HttpSession session, Model model) throws Exception {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        vo.setUserId(farmerInfo.getUserId());

        return userManureService.get(vo);
    }

    @RequestMapping(value = "/api/002005006/countUsed", method = RequestMethod.GET)
    @ResponseBody
    public Long api002005006countUsed(@Valid UserManureDto vo, HttpServletRequest request, HttpSession session, Model model) throws Exception {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        vo.setUserId(farmerInfo.getUserId());

        return userManureStockService.countUsed(farmerInfo.getUserId(), vo.getUserManureSeq());
    }

    @RequestMapping(value = "/api/002005006/update", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005006update(@Valid UserManureDto vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        vo.setUserId(farmerInfo.getUserId());

        userManureService.update(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/002005006/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005006Delete(Long[] userManureSeqs, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (userManureSeqs == null || userManureSeqs.length == 0) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//        UserInfoDto farmerInfo = UserInfoUtil.getUserInfo();
        int usedCnt = 0;
        /*for (int i = 0, cnt = userManureSeqs.length; i < cnt; i++) {
            Long userManureSeq = userManureSeqs[i];
            usedCnt += userManureStockService.countUsed(farmerInfo.getUserId(), userManureSeq);
        }*/

        if (usedCnt > 0) {
            return new Result(false, "9995");
        }
        userManureService.delete(farmerInfo.getUserId(), userManureSeqs);

        return new Result(true, "0000");
    }

}
