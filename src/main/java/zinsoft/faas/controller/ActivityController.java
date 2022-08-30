package zinsoft.faas.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import zinsoft.faas.service.ActivityService;
import zinsoft.faas.service.UserActivityService;
import zinsoft.faas.vo.UserActivity;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;

@Controller
@RequestMapping("${api.prefix}")
public class ActivityController {

    @Resource
    UserActivityService userActivityService;

    @Resource
    ActivityService activityService;

    /*
    @RequestMapping(value = "/content/002005004/excel", method = RequestMethod.GET)
    public ModelAndView excel002005004(@RequestParam Map<String, Object> param, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        param.put("userId", farmerInfo.getUserId());
        List<UserActivity> list = userActivityService.list(param);
        ModelAndView mv = new ModelAndView(new List002005004ExcelView());

        mv.addObject("sheetName", "교육");
        mv.addObject("cond", param);
        mv.addObject("list", list);

        return mv;
    }
    */

    @RequestMapping(value = "/activity/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005004Insert(@Valid UserActivity vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        vo.setUserId(farmerInfo.getUserId());

        userActivityService.insert(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/activity/list", method = RequestMethod.GET)
    @ResponseBody
    public Object api002005004List(Long cropSeq, Long userCropSeq, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (cropSeq == null || cropSeq == 0) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        return userActivityService.list(farmerInfo.getUserId(), cropSeq, userCropSeq);
    }

    @RequestMapping(value = "/activity/listAll", method = RequestMethod.GET)
    @ResponseBody
    public Object api002005004ListAll(Long cropSeq, String orderBy, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        return activityService.listByCropSeq(cropSeq, orderBy);
    }

    @RequestMapping(value = "/activity/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005004Delete(Long[] userActivitySeqs, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (userActivitySeqs == null || userActivitySeqs.length == 0) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        userActivityService.delete(farmerInfo.getUserId(), userActivitySeqs);

        return new Result(true, "0000");
    }

}
