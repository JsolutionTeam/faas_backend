package zinsoft.faas.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.ActivityDto;
import zinsoft.faas.vo.PagingParam;

@Mapper
public interface ActivityMapper {

    void insert(ActivityDto dto);

    ActivityDto get(Long activitySeq);

    ActivityDto getByCropSeq(@Param("activitySeq") Long activitySeq, @Param("cropSeq") Long cropSeq);

    Long getActivitySeqByActNm(@Param("actNm") String actNm);

    List<ActivityDto> list(PagingParam pagingParam);

    List<ActivityDto> page(PagingParam param);

    int count(PagingParam pagingParam);

    List<ActivityDto> listByCropSeq(@Param("cropSeq") Long cropSeq, @Param("orderBy") String orderBy);

    List<ActivityDto> listByUserIdCropSeq(@Param("userId") String userId, @Param("cropSeq") Long cropSeq, @Param("userCropSeq") Long userCropSeq);

    List<ActivityDto> listByCropACd(@Param("cropACd") String cropACd, @Param("userCropSeq") Long userCropSeq);

    List<ActivityDto> listByPartTCd(@Param("partTCd") String partTCd);

    void update(ActivityDto dto);

    void delete(ActivityDto dto);

}
