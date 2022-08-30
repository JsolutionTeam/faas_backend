package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.CropActivityDto;
import zinsoft.faas.vo.PagingParam;

@Mapper
public interface CropActivityMapper {

    void insert(CropActivityDto vo);

    CropActivityDto get(Long cropActivitySeq);

    List<CropActivityDto> list(Long activityTCd);

    List<CropActivityDto> list2(Map<String, Object> param);

    List<CropActivityDto> listActivityTCd();

    List<CropActivityDto> pageActivityTCd(PagingParam pagingParam);

    int countActivityTCd(PagingParam pagingParam);

    void update(CropActivityDto vo);
    
    void updateCropActNm(@Param("cropActNm") String cropActNm, @Param("activityTCd") Long activityTCd);

    void delete(Long cropActivitySeq);

    void deleteActivityTCd(@Param("activityTCd")Long activityTCd);

}
