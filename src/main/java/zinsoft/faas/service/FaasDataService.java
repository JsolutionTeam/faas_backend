package zinsoft.faas.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zinsoft.faas.dao.mapper.FaasDataMapper;
import zinsoft.faas.dto.AccountDto;
import zinsoft.faas.dto.CropDto;
import zinsoft.faas.dto.UserCropDto;
import zinsoft.faas.repository.FaasDataRepository;
import zinsoft.faas.vo.UserActivity;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.service.AppPropertiesService;
import zinsoft.web.common.service.CodeService;

@Service
@RequiredArgsConstructor
public class FaasDataService {

    @Autowired
    ServletContext servletContext;

    @Resource
    FaasDataMapper faasDataMapper;


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

    public Object getBasicData(String[] data, String acId, String upAcId, String acNm, String cropNm, String cropACd, String inputYn) {
        Map<String, Object> ret = new HashMap<String, Object>();
        int cnt = data != null ? data.length : 0;
        boolean all = cnt == 0;

        // 계정과목
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

        // 상위계정과목
        if (all || ArrayUtils.contains(data, "upAccountList")) {
            List<AccountDto> accountList = accountService.getRootAcIdList();
            if (cnt == 1) {
                return accountList;
            } else {
                ret.put("upAccountList", accountList);
            }
        }

        // 품목
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

        // 재배품목
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

        // 작업단계
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

        // 수입지출 상세항목
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
        return faasDataMapper.getDataCount(userId, startDt, endDt);
    }

    public List<Map<String, Object>> getCalendarData(String userId, String startDt, String endDt) {
        return faasDataRepository.getCalendarData(userId, startDt, endDt);
    }

    public List<Map<String, Object>> getFarmingStatus(String userId) {
        return faasDataMapper.getFarmingStatus(userId);
    }

    public List<Map<String, Object>> getInoutStatus(String userId) {
        List<Map<String, Object>> list = faasDataMapper.getInoutStatus(userId);

        // 불필요한 키 삭제
        for (Map<String, Object> vo : list) {
            vo.remove("sort");
        }

        return list;
    }

    public List<Map<String, Object>> getInout(String userId) {
        return faasDataMapper.getInout(userId);
    }

    public List<CodeDto> getCodeData(String cdPrtId) {
        return codeService.list(cdPrtId);
    }

}
