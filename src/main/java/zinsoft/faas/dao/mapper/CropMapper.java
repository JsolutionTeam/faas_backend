package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.CropDto;
import zinsoft.faas.vo.PagingParam;

@Mapper
public interface CropMapper {

    void insert(CropDto vo);

    CropDto get(Long cropSeq);

    Object isExistCropId(@Param("cropSeq") Long cropSeq);

    List<CropDto> list(Map<String, Object> param);

    List<CropDto> listSomeCrop(@Param("stCropId") String stCropId, @Param("edCropId") String edCropId);

    List<CropDto> page(PagingParam pagingParam);

    int count(PagingParam pagingParam);

    void update(CropDto vo);

    void delete(@Param("cropSeqs") Long[] cropSeqs);
}
