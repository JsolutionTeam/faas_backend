package zinsoft.faas.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.ActivityMapper;
import zinsoft.faas.dto.ActivityDto;
import zinsoft.faas.dto.UserCropDto;
import zinsoft.faas.repository.ActivityRepository;
import zinsoft.faas.service.ActivityService;
import zinsoft.faas.service.UserActivityService;
import zinsoft.faas.service.UserCropService;
import zinsoft.faas.vo.PagingParam;
import zinsoft.util.DataTablesResponse;

@Service
public class ActivityServiceImpl extends EgovAbstractServiceImpl implements ActivityService {

    @Resource
    ActivityMapper activityMapper;

    @Resource
    ActivityRepository activityRepository;

    @Resource
    UserActivityService userActivityService;

    @Resource
    UserCropService userCropService;

    @Override
    public void insert(ActivityDto dto) {
        activityMapper.insert(dto);
    }

    @Override
    public ActivityDto get(Long activitySeq) {
        return activityMapper.get(activitySeq);
    }

    @Override
    public ActivityDto get(Long activitySeq, Long cropSeq) {
        return activityMapper.getByCropSeq(activitySeq, cropSeq);
    }

    @Override
    public Long getActivitySeqByActNm(String actNm) {
        return activityMapper.getActivitySeqByActNm(actNm);
    }

    @Override
    public boolean isExistActivityId(Long activitySeq) {
        return activityRepository.existsByActivitySeqAndStatusCd(activitySeq, "N");
    }

    @Override
    public List<ActivityDto> listByCropSeq(Long cropSeq, String orderBy) {
        return activityMapper.listByCropSeq(cropSeq, orderBy);
    }

    @Override
    public List<ActivityDto> listByPartTCd(String partTCd) {
        return activityMapper.listByPartTCd(partTCd);
    }

    @Override
    public List<ActivityDto> listByUserIdCropSeq(String userId, Long cropSeq, Long userCropSeq) {
        List<ActivityDto> list = activityMapper.listByUserIdCropSeq(userId, cropSeq, userCropSeq);

        if (list == null || list.isEmpty()) {
            UserCropDto userCrop = userCropService.get(userId, cropSeq, userCropSeq);
            if (userCrop != null) {
                if("1".equals(userCrop.getPartTCd()) == true) { // 사업유형 - 생산
                    list = activityMapper.listByCropSeq(cropSeq, "seq");
                } else {
                    list = activityMapper.listByPartTCd(userCrop.getPartTCd());
                }
                userActivityService.insert(userId, cropSeq, userCropSeq, list);
            }
        }

        return list;
    }

    @Override
    public List<ActivityDto> listByCropACd(String cropACd, Long userCropSeq) {
        return activityMapper.listByCropACd(cropACd, userCropSeq);
    }

    @Override
    public List<ActivityDto> list(PagingParam pagingParam) {
        return activityMapper.list(pagingParam);
    }

    @Override
    public DataTablesResponse<ActivityDto> page(Map<String, Object> search, Pageable pageable) {
        return DataTablesResponse.of(activityRepository.page(search, pageable));
    }

    @Override
    public void update(ActivityDto dto) {
        activityMapper.update(dto);
    }

    @Override
    public void updateExprSeq(List<ActivityDto> list) {
        for (ActivityDto dto : list) {
            update(dto);
        }
    }

    @Override
    public void delete(ActivityDto dto) {
        activityMapper.delete(dto);
    }

    @Override
    public void delete(Long[] activitySeqs) {
        ActivityDto dto = new ActivityDto();

        for (Long activitySeq : activitySeqs) {
            dto.setActivitySeq(activitySeq);
            delete(dto);
        }
    }

}
