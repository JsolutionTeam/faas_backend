package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.UserEmpCostDto;
import zinsoft.faas.vo.PagingParam;

@Mapper
public interface UserEmpCostMapper {

    void insert(UserEmpCostDto vo);

    UserEmpCostDto get(@Param("userEmpCostSeq") Long userEmpCostSeq);

    UserEmpCostDto getByUserId(String userId);

    UserEmpCostDto getByYear(@Param("userId")String userId, @Param("year") String year);

    int checkValidYear(@Param("year") String year, @Param("userId") String userId);

    List<UserEmpCostDto> page(PagingParam pagingParam);

    int count(PagingParam pagingParam);

    List<UserEmpCostDto> list(Map<String, Object> param);
    
    void update(UserEmpCostDto vo);

    void delete(@Param("userEmpCostSeq") Long userEmpCostSeq);

    int deleteByUserId(String userId);

}
