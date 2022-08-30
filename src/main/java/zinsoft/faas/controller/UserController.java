package zinsoft.faas.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import zinsoft.faas.service.AccountService;
import zinsoft.faas.service.CropService;
import zinsoft.faas.vo.PagingParam;
import zinsoft.web.common.service.CodeService;
import zinsoft.web.common.service.EmailService;
import zinsoft.web.common.service.UserInfoService;

@Controller
public class UserController {

    @Resource
    AccountService accountService;

    @Resource
    UserInfoService userInfoService;

    @Resource
    CodeService codeService;

    @Resource
    CropService cropService;

    @Resource
    EmailService emailService;

    //@Resource
    //PushTokenService pushTokenService;

    /*@RequestMapping(value = "/api/000000001/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result api000000001insert(@Valid UserInfoDto dto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        dto.setStatusCd(UserInfoDto.STATUS_CD_NORMAL);
        dto.setRoleId("ROLE_0004");

         try {
            userInfoService.insert(dto.getUserId(), dto, CommonUtil.getBaseURI(request), (Date) request.getAttribute("NOW"));
        } catch (DuplicateKeyException dke) {
            return new Result(false, "9006");
        }

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/000000001/joindataInsert", method = RequestMethod.GET)
    @ResponseBody
    public Result api000000001joindataInsert(String userId, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto dto = UserInfoUtil.getFarmerInfo();
        if (userId == null || userId.equals("")) {
            userId = dto.getUserId();
            dto.setStatusCd(UserInfoDto.STATUS_CD_NORMAL);
        }
        //userInfoService.updateStatus(dto);
        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/000000001/idCheck", method = RequestMethod.POST)
    @ResponseBody
    public boolean api000000001idCheck(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        boolean flag = true;
        List<UserInfoDto> list = null;
        String userId = request.getParameter("userId");

      //  list = userInfoService.idCheck(userId);
        if (list.size() >= 1) {
            flag = false;
        }
        return flag;
    }

    @RequestMapping(value = "/api/000000001/searchId", method = RequestMethod.POST)
    @ResponseBody
    public Result api000000001searchId(String userNm, String mobileNum, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        String userId = userInfoService.getUserIdByUserNmMobileNum(userNm, mobileNum);

        if (userId != null && !userId.isEmpty()) {
            return new Result(true, "0000", userId, null);
        } else {
            return new Result(true, "9990");
        }
        return new Result(true, "9990");
    }

    @RequestMapping(value = "/api/000000001/searchPwd", method = RequestMethod.POST)
    @ResponseBody
    public Result api000000001searchPwd(String userId, String userNm, String mobileNum, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto userInfo = userInfoService.get(userId);

        if (userInfo != null && userInfo.getUserNm().equals(userNm) && userInfo.getMobileNum().replaceAll("\\D", "").equals(mobileNum.replaceAll("\\D", ""))) {
            Random rnd = new Random();
            StringBuffer buf = new StringBuffer();

            for (int i = 0; i < 5; i++) {
                if (rnd.nextBoolean()) {
                    buf.append((char) ((rnd.nextInt(26)) + 97));
                } else {
                    buf.append((rnd.nextInt(10)));
                }
            } // Password Create

            String rndPwd = buf.toString();
            UserInfoPwd pwdVo = new UserInfoPwd();
            pwdVo.setUserId(userId);
            pwdVo.setUserPwd(rndPwd);
            //userInfoService.updatePwd(userId, pwdVo); // Change password to DB

            userInfo.setUserPwd(rndPwd);
            emailService.send("search.html", SiteConfig.getProperty(SiteProperty.MAIL_SUBJECT_SEARCH), userInfo, CommonUtil.getBaseURI(request), (Date) request.getAttribute("NOW"));
            return new Result(true, "0000");
        } else {
            return new Result(true, "9990");
        }
    }*/

    @RequestMapping(value = "/content/000000001/form", method = RequestMethod.GET)
    public String form000000001(PagingParam pagingParam, HttpServletRequest request, HttpSession session, Model model) throws Exception {
//        //Land
//        String[] acIds = new String[1];
//        acIds[0] = "155"; // 토지
//        model.addAttribute("assetListL", assetService.listSome(acIds));
//        model.addAttribute("landTCd", codeService.list("LAND_T_CD"));
//
//        //Facility
//        String[] acIds2 = new String[1];
//        acIds2[0] = "156"; // 건물
//        model.addAttribute("assetListF", assetService.listSome(acIds2));
//
//        //Machine
//        String[] acIds3 = new String[1];
//        acIds3[0] = "160"; // 대농기구
//        model.addAttribute("assetListM", assetService.listSome(acIds3));
//
//        //Lia
//        List<Asset> list = assetService.listByUpAcId("101");
//        List<Asset> listEtcLiq = assetService.listByUpAcId("168");
//        model.addAttribute("assetListLia", ListUtils.union(list, listEtcLiq));

        //Crop
        model.addAttribute("cropList", cropService.list());
        model.addAttribute("cropSCdList", codeService.list("CROP_S_CD"));

        model.addAttribute("acList", accountService.list());

        return "MAIN/000000001/form";
    }

    /*
    @RequestMapping(value = "/api/push/register", method = RequestMethod.POST)
    @ResponseBody
    public Result register(@Valid PushToken vo, BindingResult bindingResult, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return new Result(false, "9998");
        }

        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        vo.setUserId(userInfo.getUserId());
        pushTokenService.insert(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/push/unregister", method = RequestMethod.POST)
    @ResponseBody
    public Result unregister(@Validated(DeviceUnregister.class) PushToken vo, BindingResult bindingResult, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return new Result(false, "9998");
        }

        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        vo.setUserId(userInfo.getUserId());
        pushTokenService.delete(vo, PushToken.UNREG_T_CD_USER);

        return new Result(true, "0000");
    }
    */

}
