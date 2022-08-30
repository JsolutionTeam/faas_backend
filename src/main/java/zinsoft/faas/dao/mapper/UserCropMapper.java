package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.UserCropDto;
import zinsoft.faas.vo.PagingParam;

@Mapper
public interface UserCropMapper {

    void insert(UserCropDto vo);

    int copy(String userId);

    UserCropDto get(UserCropDto vo);

    Long getCropSeqByCropNm(@Param("userId") String userId, @Param("cropNm") String cropNm);
    
    List<UserCropDto> list(Map<String, Object> param);
    
    List<UserCropDto> listByUserId(Map<String, Object> param);
    
    List<UserCropDto> listCrop(Map<String, Object> param);

    List<UserCropDto> page(PagingParam pagingParam);

    int count(PagingParam pagingParam);

    int countByAliasNm(@Param("userCropSeq") Long userCropSeq, @Param("userId") String userId, @Param("aliasNm") String aliasNm);
    
    void update(UserCropDto vo);

    void updateExpr(UserCropDto vo);

    int delete(UserCropDto vo);

    int deleteByUserId(String userId);

}
