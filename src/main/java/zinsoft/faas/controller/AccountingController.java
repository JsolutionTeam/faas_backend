package zinsoft.faas.controller;

import java.util.HashMap;
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

import zinsoft.faas.dto.UserCropDto;
import zinsoft.faas.service.AccountService;
import zinsoft.faas.service.CropService;
import zinsoft.faas.service.UserCropService;
import zinsoft.faas.service.UserDiaryService;
import zinsoft.faas.view.UserCropExcelView;
import zinsoft.faas.vo.Page;
import zinsoft.faas.vo.PagingParam;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.CodeService;

@Controller
public class AccountingController {

    @Resource
    AccountService accountService;

    @Resource
    CodeService codeService;

    @Resource
    UserDiaryService userDiaryService;

    @Resource
    UserCropService userCropService;

    @Resource
    CropService cropService;


//    @RequestMapping(value = "/api/002001005/grid1", method = RequestMethod.GET)
//    @ResponseBody
//    public DHXGrid<Slip> api002001005Grid1(String dhxFields, String dhxUserDataFields, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session, Model model) throws Exception {
//        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//
//        params.put("userId", farmerInfo.getUserId());
//
//        return slipService.list002001005Grid1(dhxFields, dhxUserDataFields, params);
//    }
//
//    @RequestMapping(value = "/api/002001005/gridSlip", method = RequestMethod.GET)
//    @ResponseBody
//    public DHXGrid<Slip> api002001005GridSlip(String dhxFields, String dhxUserDataFields, @RequestParam Map<String, String> params, HttpServletRequest request, HttpSession session, Model model) throws Exception {
//        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
//
//        params.put("userId", farmerInfo.getUserId());
//
//        return slipService.list002001005GridSlip(dhxFields, dhxUserDataFields, params);
//    }
//
//    @RequestMapping(value = "/api/002001005/process", method = RequestMethod.POST)
//    @ResponseBody
//    public List<DHXDataProcessorComplexResponse> api002001005Process(@DHXGridData DHXGridDataList<Slip> list, HttpServletRequest request, HttpSession session, Model model) throws Exception {
//        List<Slip> insertList = list.getInsertList();
//
//        for (Slip vo : insertList) {
//            vo.setUpdateYn("N");
//            vo.setSettleTCd("S");
//            vo.setClosingSlip(true);
//        }
//
//        return slipService.update(list);
//    }


    @RequestMapping(value = "/content/002005002/excel", method = RequestMethod.GET)
    public ModelAndView excel002005002(@RequestParam Map<String, Object> param, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        param.put("userId", farmerInfo.getUserId());

        /*if (UserInfoUtil.isManager(request) && userInfo.getUserId().equals(farmerInfo.getUserId())) {
            param.put("userId", "");
        } else {
            param.put("userId", farmerInfo.getUserId());
        }*/

        param.put("orderBy", "DESC");
        param.put("cropCd", "1");

        List<UserCropDto> list = userCropService.list(param);
        ModelAndView mv = new ModelAndView(new UserCropExcelView());

        mv.addObject("sheetName", "품목재배관리");
        mv.addObject("cond", param);
        mv.addObject("list", list);

        return mv;
    }

    @RequestMapping(value = "/api/002005002/insert", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005002insert(@Valid UserCropDto vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        if (farmerInfo != null && (vo.getUserId() == null || vo.getUserId().isEmpty())) {
            vo.setUserId(farmerInfo.getUserId());
        }

        if (userCropService.countByAliasNm(vo.getUserCropSeq(), vo.getUserId(), vo.getAliasNm()) > 0) {
            return new Result(false, "9011");
        }

        userCropService.insert(vo);

        if (farmerInfo != null) {
            farmerInfo.setCropCnt(farmerInfo.getCropCnt() + 1);
        }

        return new Result(true, "{\"userCropSeq\":" + vo.getUserCropSeq() + ", \"cropSeq\":" + vo.getCropSeq() + "}");
    }

    @RequestMapping(value = "/api/002005002/insertCopy", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005002insertCopy(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        int cnt = userCropService.insertCopy(userInfo.getUserId(), farmerInfo.getUserId());

        if (cnt <= 0) {
            return new Result(false, "1001");
        }

        farmerInfo.setCropCnt(cnt);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/content/002005002/list", method = RequestMethod.GET)
    public String list002005002(PagingParam pagingParam, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        Map<String, String> cond = pagingParam.getCond();
        cond.put("userId", farmerInfo.getUserId());
        pagingParam.setCond(cond);
        pagingParam.setCondition(request);

        String[] acIds = new String[3];
        acIds[0] = "117"; // 미수확작물
        acIds[1] = "165"; // 성숙-생물자산
        acIds[2] = "166"; // 미성숙-생물자산
        model.addAttribute("acList", accountService.listSome(acIds));

        model.addAttribute("cropList", cropService.list());
        model.addAttribute("cropSCdList", codeService.list("CROP_S_CD"));

        if (UserInfoUtil.isManager(request) && userInfo.getUserId().equals(farmerInfo.getUserId())) {
            return "MAIN/002005002/list_admin";
        }

        return "MAIN/002005002/list";
    }

    @RequestMapping(value = "/api/002005002/get", method = RequestMethod.GET)
    @ResponseBody
    public UserCropDto api002005002get(@Valid UserCropDto vo, String userId, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        if (farmerInfo != null && farmerInfo.getUserId() != null) {
            vo.setUserId(farmerInfo.getUserId());
        } else {
            if (userId != null && userId.isEmpty() == false) {
                vo.setUserId(userId);
            } else {
                return null;
            }
        }
        return userCropService.get(vo);
    }

    @RequestMapping(value = "/api/002005002/page", method = RequestMethod.GET)
    @ResponseBody
    public Page<UserCropDto> api002005002page(PagingParam pagingParam, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        Map<String, String> cond = pagingParam.getCond();
        cond.put("userId", farmerInfo.getUserId());
        cond.put("cropCd", "1");
        pagingParam.setCond(cond);
        pagingParam.setCondition(request);

        /*if (UserInfoUtil.isManager(request) && userInfo.getUserId().equals(farmerInfo.getUserId())) {
            cond.put("isAdmin", "Y");
        }*/

        //return userCropService.page(pagingParam);
        return null;
    }

    @RequestMapping(value = "/api/002005002/listUserCropUserId", method = RequestMethod.GET)
    @ResponseBody
    public List<UserCropDto> listUserCropUserId(String userId, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        Map<String, Object> param = new HashMap<String, Object>();

        if (farmerInfo != null && farmerInfo.getUserId() != null) {
            param.put("userId", farmerInfo.getUserId());
        } else {
            if (userId != null && userId.isEmpty() == false) {
                param.put("userId", userId);
            } else {
                return null;
            }
        }

        return userCropService.listByUserId(param);
    }

    @RequestMapping(value = "/api/002005002/update", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005002update(@Valid UserCropDto vo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        if (farmerInfo != null && farmerInfo.getUserId() != null) {
            vo.setUserId(farmerInfo.getUserId());
        } else {
            if (vo.getUserId() != null && vo.getUserId().isEmpty() == false) {
                vo.setUserId(vo.getUserId());
            } else {
                return new Result(false, "9998");
            }
        }

        if (userCropService.countByAliasNm(vo.getUserCropSeq(), vo.getUserId(), vo.getAliasNm()) > 0) {
            return new Result(false, "9011");
        }

        userCropService.update(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/002005002/updateExpr", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005002updateExpr(Long userCropSeq, String exprYN, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        UserCropDto vo = new UserCropDto();
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();

        vo.setUserId(farmerInfo.getUserId());
        vo.setUserCropSeq(userCropSeq);
        vo.setExprYN(exprYN);

        userCropService.updateExpr(vo);

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/002005002/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005002delete(Long[] userCropSeqs, String userId, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        if (userCropSeqs == null || userCropSeqs.length == 0) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return new Result(false, "9998");
        }

        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        String deleteUserId = null;
        if (farmerInfo != null && farmerInfo.getUserId() != null) {
            deleteUserId = farmerInfo.getUserId();
        } else {
            if (userId != null && userId.isEmpty() == false) {
                deleteUserId = userId;
            } else {
                return new Result(false, "9998");
            }
        }

        int slipCnt = 0;

        for (int i = 0, cnt = userCropSeqs.length; i < cnt; i++) {
            Long userCropSeq = userCropSeqs[i];
            slipCnt += userDiaryService.countUsedCrop(deleteUserId, userCropSeq);
        }

        if (slipCnt > 0) {
            return new Result(false, "9010");
        }

        int cnt = userCropService.delete(deleteUserId, userCropSeqs);
        if (farmerInfo != null) {
            farmerInfo.setCropCnt(farmerInfo.getCropCnt() - cnt);
        }

        return new Result(true, "0000");
    }

    @RequestMapping(value = "/api/002005002/deleteAll", method = RequestMethod.POST)
    @ResponseBody
    public Result api002005002deleteAll(String userId, HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) throws Exception {
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        String deleteUserId = null;
        Map<String, Object> param = new HashMap<String, Object>();

        if (farmerInfo != null && farmerInfo.getUserId() != null) {
            deleteUserId = farmerInfo.getUserId();
        } else {
            if (userId != null && userId.isEmpty() == false) {
                deleteUserId = userId;
            } else {
                return new Result(false, "9998");
            }
        }
        List<UserCropDto> userCropList = userCropService.listByUserId(param);
        int slipCnt = 0;

        for (UserCropDto userCrop : userCropList) {
            Long userCropSeq = userCrop.getUserCropSeq();
            slipCnt += userDiaryService.countUsedCrop(deleteUserId, userCropSeq);
        }

        if (slipCnt > 0) {
            return new Result(false, "9010");
        }

        userCropService.deleteByUserId(deleteUserId);
        if (farmerInfo != null) {
            farmerInfo.setCropCnt(0);
        }

        return new Result(true, "0000");
    }

}
