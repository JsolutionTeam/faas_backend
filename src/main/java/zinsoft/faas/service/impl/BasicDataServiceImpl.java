package zinsoft.faas.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zinsoft.faas.dao.mapper.FaasDataMapper;
import zinsoft.faas.dao.mapper.FaasDataRepository;
import zinsoft.faas.dto.AccountDto;
import zinsoft.faas.dto.CropDto;
import zinsoft.faas.dto.UserCropDto;
import zinsoft.faas.service.*;
import zinsoft.faas.vo.UserActivity;
import zinsoft.util.CommonUtil;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.service.AppPropertiesService;
import zinsoft.web.common.service.BasicDataService;
import zinsoft.web.common.service.CodeService;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BasicDataServiceImpl implements BasicDataService {

    @Autowired
    ServletContext servletContext;

    @Resource
    FaasDataMapper basicDataMapper;

    private final FaasDataRepository faasDataRepository;

    @Resource
    AppPropertiesService appPropertiesService;

    @Resource
    CodeService codeService;

    @Resource
    AccountService accountService;

    @Resource
    CropService cropService;

    @Resource
    UserCropService userCropService;

    @Resource
    UserActivityService userActivityService;

    @Resource
    UserInoutDetailService userInoutDetailService;

    @Override
    public void createBasicData() {
        String ver = CommonUtil.getToday("yyyyMMddHHmmss");
        String basePath = servletContext.getRealPath("/static/js");
        String jsFilename = basePath + "/basic-data.js";
        String jsonFilename = basePath + "/basic-data.json";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> search = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer();
        String json = null;
        List<CodeDto> ynTCd = new ArrayList<CodeDto>(2);
        List<CodeDto> nyTCd = new ArrayList<CodeDto>(2);
        List<CodeDto> successYn = new ArrayList<CodeDto>(2);

        ynTCd.add(new CodeDto("yn_t_cd", Constants.YN_YES, Constants.YN_YES));
        ynTCd.add(new CodeDto("yn_t_cd", Constants.YN_NO, Constants.YN_NO));

        nyTCd.add(new CodeDto("ny_t_cd", Constants.YN_NO, Constants.YN_NO));
        nyTCd.add(new CodeDto("ny_t_cd", Constants.YN_YES, Constants.YN_YES));

        successYn.add(new CodeDto("success_yn", Constants.YN_YES, "??????", "Success"));
        successYn.add(new CodeDto("success_yn", Constants.YN_NO, "??????", "Fail"));


        search.put("inputYn", "Y");
        search.put("orderBy", "NAME_ASC");
        List<AccountDto> accountList = accountService.list(search);
//        DHXGrid<Account> accountGrid = new DHXGrid<>(Account.class, "acId|acNm|costTCd,acId,acNm", accountList);
//        data.put("acIdGrid", accountGrid);
        data.put("accountList", accountList);

        search.put("exprYn", "Y");
        search.put("inputYn", "N");
        List<AccountDto> upAccountList = accountService.list(search);
        data.put("upAccountList", upAccountList);

//        List<CodeDto> bizTCdList = codeService.list("BIZ_T_CD");
//        DHXCombo bizTCdCombo = new DHXCombo("#name#", "#name#");
//        for (CodeDto dto : bizTCdList) {
//            bizTCdCombo.add(dto.getCodeVal(), dto.getCodeNm());
//        }
//        data.put("bizTCdCombo", bizTCdCombo);

//        List<CodeDto> gradeTCdList = codeService.list("GRADE_T_CD");
//        DHXCombo gradeTCdCombo = new DHXCombo("#name#", "#name#");
//        for (CodeDto dto : gradeTCdList) {
//            gradeTCdCombo.add(dto.getCodeVal(), dto.getCodeNm());
//        }
//        data.put("gradeTCdCombo", gradeTCdCombo);

//        List<CodeDto> packTCdList = codeService.list("PACK_T_CD");
//        DHXCombo packTCdCombo = new DHXCombo("#name#", "#name#");
//        for (CodeDto dto : packTCdList) {
//            packTCdCombo.add(dto.getCodeVal(), dto.getCodeNm());
//        }
//        data.put("packTCdCombo", packTCdCombo);

        //List<CodeDto> slipTCdList = codeService.list("SLIP_T_CD");
//        DHXCombo slipTCdCombo = new DHXCombo("#name#", "#name#");
        ////for (CodeDto dto : slipTCdList) {
        ////    slipTCdCombo.add(dto.getCodeVal(), dto.getCodeNm());
        ////}
        //// TODO: ?????? ????????????
//        slipTCdCombo.add("1", "??????");
//        slipTCdCombo.add("2", "??????");
//        //slipTCdCombo.add("5", "????????????", "N");
//        //slipTCdCombo.add("6", "????????????", "N");
//        data.put("slipTCdCombo", slipTCdCombo);

        /* TODO
        List<String> costAcIdList = costService.listAcId();
        List<Trade> tradeList = tradeService.list(new PagingParam());
        */

//        List<CodeDto> incomeTCdList = codeService.list("INCOME_T_CD");
//        DHXCombo incomeTCdCombo = new DHXCombo("#name#", "#name#");
//        for (CodeDto dto : incomeTCdList) {
//            incomeTCdCombo.add(dto.getCodeVal(), dto.getCodeNm());
//        }
//        data.put("incomeTCdCombo", incomeTCdCombo);

//        List<CodeDto> outgoingTCdList = codeService.list("OUTGOING_T_CD");
//        DHXCombo outgoingTCdCombo = new DHXCombo("#name#", "#name#");
//        for (CodeDto dto : outgoingTCdList) {
//            outgoingTCdCombo.add(dto.getCodeVal(), dto.getCodeNm());
//        }
//        data.put("outgoingTCdCombo", outgoingTCdCombo);

        search.put("bpTCd", "P");
        search.put("cdTCd", "D");
        search.put("exprYn", "Y");
        search.put("inputYn", "Y");
        List<AccountDto> incomeAccountList = accountService.list(search);
        data.put("incomeAccountList", incomeAccountList);

        search.put("bpTCd", "P");
        search.put("cdTCd", "C");
        search.put("exprYn", "Y");
        search.put("inputYn", "Y");
        List<AccountDto> outgoingAccountList = accountService.list(search);
        data.put("outgoingAccountList", outgoingAccountList);

        List<CropDto> cropList = cropService.list();
        data.put("cropList", cropList);

        // ??????
//        List<Asset> assetLandList = assetService.listSome(new String[] { "155" });
//        data.put("assetLandList", assetLandList);
//
//        // ??????
//        List<Asset> assetFacilityFList = assetService.listSome(new String[] { "156" });
//        data.put("assetFacilityFList", assetFacilityFList);
//
//        // ???????????????
//        List<Asset> assetFacilityAList = assetService.listSome(new String[] { "158" });
//        data.put("assetFacilityAList", assetFacilityAList);
//
//        // ????????????
//        List<Asset> assetMachineList = assetService.listSome(new String[] { "160" });
//        data.put("assetMachineList", assetMachineList);
//
//        // ????????????
//        List<Asset> assetLiassetList = assetService.listByUpAcId("101");
//        data.put("assetLiassetList", assetLiassetList);
//
//        // ??????
//        //List<Asset> assetFruitList = assetService.listSome(new String[] { "165" });
//        //data.put("assetFruitList", assetFruitList);

        List<CodeDto> upCropList = codeService.list("CROP_A_CD");
        data.put("upCropList", upCropList);

        // ???????????? ?????? ?????? ??????
//        String[] dAcIds = new String[8];
//        dAcIds[0] = "102"; // ??????
//        dAcIds[1] = "103"; // ????????????
//        dAcIds[2] = "202"; // ???????????????
//        dAcIds[3] = "203"; // ???????????????
//        dAcIds[4] = "251"; // ?????????????????????
//        dAcIds[5] = "252"; // ???????????????
//        dAcIds[6] = "301"; // ?????????
//        dAcIds[7] = "556"; // ???????????????
//        data.put("fixedAssetCreditAcList", accountService.listSome(dAcIds));
//
//        // ???????????? ?????? ?????? ??????
//        dAcIds = new String[6];
//        dAcIds[0] = "202"; // ???????????????
//        dAcIds[1] = "203"; // ???????????????
//        dAcIds[2] = "251"; // ?????????????????????
//        dAcIds[3] = "252"; // ???????????????
//        dAcIds[4] = "301"; // ?????????
//        dAcIds[5] = "556"; // ???????????????
//        data.put("currentAssetCreditAcList", accountService.listSome(dAcIds));
//
//        // ?????? ?????? ?????? ??????
//        dAcIds = new String[2];
//        dAcIds[0] = "102"; // ??????
//        dAcIds[1] = "103"; // ????????????
//        data.put("debtDebitAcList", accountService.listSome(dAcIds));

        try {
            data.put("ver", ver);
            data.put("yn_t_cd", ynTCd);
            data.put("ny_t_cd", nyTCd);
            data.put("success_yn", successYn);

            json = mapper.writeValueAsString(data);

            sb.append("var BASIC_DATA = ");
            sb.append(json);
            sb.append(';');

            FileUtils.writeStringToFile(new File(jsFilename), sb.toString(), "UTF-8");
            FileUtils.writeStringToFile(new File(jsonFilename), json, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        appPropertiesService.update(Constants.VERSION_BASIC_DATA, ver);
    }

    public Object getBasicData(String[] data, String acId, String upAcId, String acNm, String cropNm, String cropACd, String inputYn) {
        Map<String, Object> ret = new HashMap<String, Object>();
        int cnt = data != null ? data.length : 0;
        boolean all = cnt == 0;

        // ????????????
        if (all || ArrayUtils.contains(data, "accountList")) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("acId", acId);
            param.put("upAcId", upAcId);
            param.put("acNm", acNm);
            param.put("inputYn", inputYn);
            param.put("orderBy", "NAME_ASC");

            List<AccountDto> accountList = accountService.list(param);
            if (cnt == 1) {
                return accountList;
            } else {
                ret.put("accountList", accountList);
            }
        }

        // ??????????????????
        if (all || ArrayUtils.contains(data, "upAccountList")) {
            List<AccountDto> accountList = accountService.getRootAcIdList();
            if (cnt == 1) {
                return accountList;
            } else {
                ret.put("upAccountList", accountList);
            }
        }

        // ??????
        if (all || ArrayUtils.contains(data, "cropList")) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("cropNm", cropNm);
            param.put("cropACd", cropACd);

            List<CropDto> list = cropService.list(param);
            if (cnt == 1) {
                return list;
            } else {
                ret.put("cropList", list);
            }
        }

        return ret;
    }

    public Object getUserData(String userId, String[] data, String cropNm, String custNm, Long cropSeq, Long userCropSeq, String actNm, String inoutCd, String detail) {
        Map<String, Object> ret = new HashMap<String, Object>();
        int cnt = data != null ? data.length : 0;
        boolean all = cnt == 0;

        // ????????????
        if (all || ArrayUtils.contains(data, "userCropList")) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userId", userId);
            param.put("cropNm", cropNm);
            param.put("orderBy", "aliasNm");

            List<UserCropDto> userCropList = userCropService.list(param);
            if (cnt == 1) {
                return userCropList;
            } else {
                ret.put("userCropList", userCropList);
            }
        }

        // ????????????
        if (all || ArrayUtils.contains(data, "userActivityList")) {
            if (cropSeq != null) {
                List<UserActivity> list = userActivityService.list(userId, cropSeq, userCropSeq, actNm);
                if (cnt == 1) {
                    return list;
                } else {
                    ret.put("userActivityList", list);
                }
            } else {
                Map<Long, List<UserActivity>> map = new HashMap<Long, List<UserActivity>>();
                List<UserCropDto> userCropList = userCropService.list(userId, null, null);

                for (UserCropDto vo : userCropList) {
                    Long cseq = vo.getCropSeq();
                    List<UserActivity> list = userActivityService.list(userId, cseq, vo.getUserCropSeq(), actNm);
                    if (list != null && !list.isEmpty()) {
                        map.put(cseq, list);
                    }
                }

                if (cnt == 1) {
                    return map;
                } else {
                    ret.put("userActivityList", map);
                }
            }
        }

        // ???????????? ????????????
        /*if (all || ArrayUtils.contains(data, "userInoutDetailList")) {
            if (cropSeq != null && inoutCd != null) {
                List<UserInoutDetail> list = userInoutDetailService.list(userId, cropSeq, inoutCd, detail);
                if (cnt == 1) {
                    return list;
                } else {
                    ret.put("userInoutDetailList", list);
                }
            } else if (cropSeq != null && inoutCd == null) {
                Map<String, List<UserInoutDetail>> iomap = new HashMap<String, List<UserInoutDetail>>();
                List<UserInoutDetail> ilist = userInoutDetailService.list(userId, cropSeq, "I", detail);
                List<UserInoutDetail> olist = userInoutDetailService.list(userId, cropSeq, "O", detail);
                if (ilist != null && !ilist.isEmpty()) {
                    iomap.put("I", ilist);
                }
                if (olist != null && !olist.isEmpty()) {
                    iomap.put("O", olist);
                }
                if (cnt == 1) {
                    return iomap;
                } else {
                    ret.put("userInoutDetailList", iomap);
                }
            } else {
                Map<Long, Map<String, List<UserInoutDetail>>> map = new HashMap<Long, Map<String, List<UserInoutDetail>>>();
                List<UserCrop> userCropList = userCropService.list(userId, null);
                List<UserInoutDetail> ilist = null;
                List<UserInoutDetail> olist = null;

                for (UserCrop vo : userCropList) {
                    Map<String, List<UserInoutDetail>> iomap = new HashMap<String, List<UserInoutDetail>>();
                    Long cseq = vo.getCropSeq();
                    ilist = userInoutDetailService.list(userId, cseq, "I", detail);
                    olist = userInoutDetailService.list(userId, cseq, "O", detail);
                    if (ilist != null && !ilist.isEmpty()) {
                        iomap.put("I", ilist);
                    }
                    if (olist != null && !olist.isEmpty()) {
                        iomap.put("O", olist);
                    }
                    if (!iomap.isEmpty()) {
                        map.put(cseq, iomap);
                    }
                }

                if (cnt == 1) {
                    return map;
                } else {
                    ret.put("userInoutDetailList", map);
                }
            }
        }*/

        return ret;
    }

    public List<Map<String, Object>> getDataCount(String userId, String startDt, String endDt) {
        return basicDataMapper.getDataCount(userId, startDt, endDt);
    }

    public List<Map<String, Object>> getCalendarData(String userId, String startDt, String endDt) {
//        return basicDataMapper.getCalendarData(userId, startDt, endDt);
        return faasDataRepository.getCalendarData(userId, startDt, endDt);
    }

    public List<Map<String, Object>> getFarmingStatus(String userId) {
        return basicDataMapper.getFarmingStatus(userId);
    }

    public List<Map<String, Object>> getInoutStatus(String userId) {
        List<Map<String, Object>> list = basicDataMapper.getInoutStatus(userId);

        // ???????????? ??? ??????
        for (Map<String, Object> vo : list) {
            vo.remove("sort");
        }

        return list;
    }

    public List<Map<String, Object>> getInout(String userId) {
        return basicDataMapper.getInout(userId);
    }

    public List<CodeDto> getCodeData(String cdPrtId) {
        return codeService.list(cdPrtId);
    }

}
