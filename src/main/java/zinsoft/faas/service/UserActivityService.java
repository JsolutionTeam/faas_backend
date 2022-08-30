package zinsoft.faas.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.UserActivityMapper;
import zinsoft.faas.dto.ActivityDto;
import zinsoft.faas.dto.UserCropDto;
import zinsoft.faas.vo.UserActivity;

@Service
public class UserActivityService extends EgovAbstractServiceImpl {

    @Resource
    UserActivityMapper userActivityMapper;

    @Resource
    ActivityService activityService;

    @Resource
    UserCropService userCropService;

    public void insert(UserActivity vo) {
        UserCropDto userCrop = userCropService.get(vo.getUserId(), vo.getCropSeq(), vo.getUserCropSeq());

        if (userCrop != null) {
            UserActivity userActivity = userActivityMapper.get(vo);
            if (userActivity == null) {
                //Activity activity = activityService.get(vo.getActivitySeq(), vo.getCropSeq());
                //if (activity != null) {
                    userActivityMapper.insert(vo);
                //}
            }
        }
    }

    public void insert(String userId, Long cropSeq, Long userCropSeq, List<ActivityDto> activityList) {
        UserActivity vo = new UserActivity(userId, cropSeq, userCropSeq);

        for (ActivityDto activity : activityList) {
            vo.setActivitySeq(activity.getActivitySeq());
            vo.setExprSeq(activity.getExprSeq());
            insert(vo);
        }
    }

    public List<UserActivity> list(String userId, Long cropSeq, Long userCropSeq) {
        return list(userId, cropSeq, userCropSeq, null);
    }

    public List<UserActivity> list(String userId, Long cropSeq, Long userCropSeq, String actNm) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("cropSeq", cropSeq);
        param.put("userCropSeq", userCropSeq);

        // tb_user_activity에 없는 경우 추가
        List<UserActivity> list = userActivityMapper.list(param);
        if (list == null || list.isEmpty()) {
            UserCropDto userCrop = userCropService.get(userId, cropSeq, userCropSeq);
            if (userCrop != null) {
                List<ActivityDto> activityList = null;
                if("1".equals(userCrop.getPartTCd()) == true) { // 사업유형 - 생산
                    activityList = activityService.listByCropSeq(cropSeq, "seq");
                } else {
                    activityList = activityService.listByPartTCd(userCrop.getPartTCd());
                }
                insert(userId, cropSeq, userCropSeq, activityList);
            }
        }

        param.put("actNm", actNm);

        return userActivityMapper.list(param);
    }

    public void delete(UserActivity vo) {
        userActivityMapper.delete(vo);
    }

    public void delete(String userId, Long[] userActivitySeqs) {
        UserActivity vo = new UserActivity();

        vo.setUserId(userId);

        for (Long userActivitySeq : userActivitySeqs) {
            vo.setUserActivitySeq(userActivitySeq);
            delete(vo);
        }
    }

    public void deleteByUserId(String userId) {
        userActivityMapper.deleteByUserId(userId);
    }
}
