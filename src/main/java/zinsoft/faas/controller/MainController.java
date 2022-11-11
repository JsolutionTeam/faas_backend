package zinsoft.faas.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import zinsoft.faas.dto.ActivityDto;
import zinsoft.faas.dto.CalDto;
import zinsoft.faas.dto.UserCropDto;
import zinsoft.faas.service.ActivityService;
import zinsoft.faas.service.CalService;
import zinsoft.faas.service.FaasDataService;
import zinsoft.faas.service.UserCropService;
import zinsoft.faas.service.impl.BasicDataServiceImpl;
import zinsoft.util.AppPropertyUtil;
import zinsoft.util.Constants;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.dto.FileInfoDto;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.FileInfoService;
import zinsoft.web.common.service.UserInfoService;
import zinsoft.web.common.view.DownloadView;
import zinsoft.web.exception.CodeMessageException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    @Resource
    FaasDataService customDataService;
//    private final BasicDataServiceImpl customDataService;

    @Resource
    UserInfoService userInfoService;

    @Resource
    FileInfoService fileInfoService;

    @Resource
    UserCropService userCropService;

    @Resource
    ActivityService activityService;


    @Resource
    CalService calService;

    @RequestMapping(value = "/loginForm")
    public String loginForm(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        String xRequestedWith = request.getHeader("X-Requested-With");

        if (xRequestedWith == null || !xRequestedWith.equals("XMLHttpRequest")) {
            return "/loginForm";
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.addAttribute("result", new Result(false, "9002"));
            return "/result";
        }
    }

    @RequestMapping(value = "/api/selectFarmer", method = RequestMethod.POST)
    @ResponseBody
    public Result selectFarmer(String userId, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        session.setAttribute(Constants.SESSION_FARMER_INFO, userInfoService.get(userId));
        return new Result(true, "0000");
    }

    @GetMapping(value = "/api/v1/*/download/{fileSeq}")
    public ModelAndView download(@PathVariable("fileSeq") Long fileSeq) throws Exception {
        if (fileSeq == null || fileSeq.longValue() == 0) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        FileInfoDto dto = fileInfoService.get(fileSeq);
        if (dto == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        File file = new File(AppPropertyUtil.get(Constants.UPLOAD_DIR) + dto.getSavedNm());
        if (file == null || !file.exists()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("file", file);
//        param.put("fileName", dto.getFileNm());
//        param.put("contentType", dto.getContentType());

        ModelAndView mv = new ModelAndView(new DownloadView());
        mv.addObject("file", file);
        mv.addObject("fileName", dto.getFileNm());
        mv.addObject("contentType", dto.getContentType());

        return mv;//new ModelAndView("downloadView", param);
    }

    @RequestMapping(value = "/api/*/listUserCrop", method = RequestMethod.GET)
    @ResponseBody
    public List<UserCropDto> listUserCrop(String sYear, String eYear, String year, String userId, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        log.info("api/*/listUserCrop!!!");
        log.info("request.isUserInRole(\"ROLE_0000\") : {}", request.isUserInRole("ROLE_0000"));
        log.info("request.isUserInRole(\"ROLE_0001\") : {}", request.isUserInRole("ROLE_0001"));
        log.info("request.isUserInRole(\"ROLE_0008\") : {}", request.isUserInRole("ROLE_0008"));
        if (request.isUserInRole("ROLE_0000") || request.isUserInRole("ROLE_0001") || request.isUserInRole("ROLE_0008")) {
            log.info("userId : {}", userId);
            if (userId != null && !userId.isEmpty()) {
                return userCropService.list(userId, year, request.getParameter("exprYN"));
            } else {
                return userCropService.list(farmerInfo.getUserId(), year, request.getParameter("exprYN"));
            }
        } else {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", farmerInfo.getUserId());
            param.put("sYear", sYear);
            param.put("eYear", eYear);
            param.put("exprYN", request.getParameter("exprYN"));

            return userCropService.list(param);
        }
    }

    @RequestMapping(value = "/api/*/listUserCropSort", method = RequestMethod.GET)
    @ResponseBody
    public List<UserCropDto> listUserCropSort(String sYear, String eYear, String year, String userId, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        /*if (request.isUserInRole("ROLE_0000") || request.isUserInRole("ROLE_0001") || request.isUserInRole("ROLE_0008")) {
            if (userId != null && !userId.isEmpty()) {
                return userCropService.listSort(userId, year, "aliasNm", request.getParameter("exprYN"));
            } else {
                return userCropService.listSort(farmerInfo.getUserId(), year, "aliasNm", request.getParameter("exprYN"));
            }
        } else */{
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", farmerInfo.getUserId());
            param.put("sYear", sYear);
            param.put("eYear", eYear);
            param.put("exprYN", request.getParameter("exprYN"));
            param.put("orderBy", "aliasNm");

            return userCropService.list(param);
        }
    }

    @RequestMapping(value = "/api/*/listActivity", method = RequestMethod.GET)
    @ResponseBody
    public List<ActivityDto> listActivity(Long cropSeq, Long userCropSeq, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        return activityService.listByUserIdCropSeq(farmerInfo.getUserId(), cropSeq, userCropSeq);
    }

    @RequestMapping(value = "/api/v1/basicData/get", method = RequestMethod.GET)
    @ResponseBody
    public Object getBasicData(String[] data, String acId, String upAcId, String acNm, String cropNm, String cropACd, String inputYn, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        return customDataService.getBasicData(data, acId, upAcId, acNm, cropNm, cropACd, inputYn);
    }

    @RequestMapping(value = "/api/v1/basicData/getCal", method = RequestMethod.GET)
    @ResponseBody
    public List<CalDto> getCal(String startDt, String endDt, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        return calService.list(startDt, endDt);
    }

    @RequestMapping(value = "/api/v1/basicData/getLunarDay", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getLunarDay(String dt, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        Map<String, String> ret = new HashMap<String, String>();
        ret.put("lunarDay", calService.get(dt, CalDto.CAL_T_CD_LUNAR_DAY));
        return ret;
    }

    @RequestMapping(value = "/api/userData/get", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserData(String[] data, String cropNm, String custNm, Long cropSeq, Long userCropSeq, String actNm, String inoutCd, String detail, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        return customDataService.getUserData(farmerInfo.getUserId(), data, cropNm, custNm, cropSeq, userCropSeq, actNm, inoutCd, detail);
    }

    @RequestMapping(value = "/api/v1/userData/getDataCount", method = RequestMethod.GET)
    @ResponseBody
    public Object getDataCount(String searchYM, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        String startDt = null, endDt = null;
        SimpleDateFormat f =  new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();

        if(searchYM != null && searchYM.length() == 6) {
            int year = Integer.valueOf(searchYM.substring(0, 4));
            int month = Integer.valueOf(searchYM.substring(4, 6)) - 1;

            cal.set(year, month, 1);
            startDt = f.format(cal.getTime());

            cal.set(year, month, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDt = f.format(cal.getTime());
        } else { // 조회 일자가 입력 되지 않은 경우 현재 날짜로 ..
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDt = f.format(cal.getTime());
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            endDt = f.format(cal.getTime());
        }
        return customDataService.getDataCount(farmerInfo.getUserId(), startDt, endDt);
    }

    @RequestMapping(value = "/api/v1/userData/getCalendarData", method = RequestMethod.GET)
    @ResponseBody
    public Object getCalendarData(String startDt, String endDt, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        String userId = null;
        if(!UserInfoUtil.isAdmin() && !UserInfoUtil.isManager()) {
            UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
            userId = farmerInfo.getUserId();
        }
        return customDataService.getCalendarData(userId, startDt, endDt);
    }

    @RequestMapping(value = "/api/userData/getFarmingStatus", method = RequestMethod.GET)
    @ResponseBody
    public Object getFarmingStatus(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        return customDataService.getFarmingStatus(farmerInfo.getUserId());
    }

    @RequestMapping(value = "/api/userData/getInoutStatus", method = RequestMethod.GET)
    @ResponseBody
    public Object getInoutStatus(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        return customDataService.getInoutStatus(farmerInfo.getUserId());
    }

    @RequestMapping(value = "/api/userData/getInout", method = RequestMethod.GET)
    @ResponseBody
    public Object getInout(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        return customDataService.getInout(farmerInfo.getUserId());
    }

    @RequestMapping(value = "/api/codeData/get", method = RequestMethod.GET)
    @ResponseBody
    public List<CodeDto> getCodeData(String cdPrtId, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        //UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        return customDataService.getCodeData(cdPrtId);
    }

}
