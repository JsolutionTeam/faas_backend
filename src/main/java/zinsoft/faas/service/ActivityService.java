package zinsoft.faas.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.ActivityDto;
import zinsoft.faas.vo.PagingParam;
import zinsoft.util.DataTablesResponse;

public interface ActivityService {

    void insert(ActivityDto dto);

    ActivityDto get(Long activitySeq);

    ActivityDto get(Long activitySeq, Long cropSeq);

    Long getActivitySeqByActNm(String actNm);

    boolean isExistActivityId(Long activitySeq);

    List<ActivityDto> listByCropSeq(Long cropSeq, String orderBy);

    List<ActivityDto> listByPartTCd(String partTCd);

    List<ActivityDto> listByUserIdCropSeq(String userId, Long cropSeq, Long userCropSeq);

    List<ActivityDto> listByCropACd(String cropACd, Long userCropSeq);

    List<ActivityDto> list(PagingParam pagingParam);

    DataTablesResponse<ActivityDto> page(Map<String, Object> search, Pageable pageable);

    void update(ActivityDto dto);

    void updateExprSeq(List<ActivityDto> list);

    void delete(ActivityDto dto);

    void delete(Long[] activitySeqs);

}