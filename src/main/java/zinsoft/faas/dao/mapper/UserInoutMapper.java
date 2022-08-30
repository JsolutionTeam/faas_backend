package zinsoft.faas.dao.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.vo.UserInoutAggregate;
import zinsoft.faas.vo.UserInoutAggregateDetail;
import zinsoft.util.DataTablesParam;

@Mapper
public interface UserInoutMapper {

    void insert(UserInoutDto dto);

    UserInoutDto get(UserInoutDto dto);

    List<UserInoutDto> list(Map<String, Object> param);

    List<UserInoutDto> page(DataTablesParam param);

    Map<String, BigDecimal> count(DataTablesParam param);

    Map<String, BigDecimal> sum(Map<String, Object> param);

    List<UserInoutAggregate> getAggregate(Map<String, Object> param);

    UserInoutAggregateDetail getAggregateDetail(Map<String, Object> param);

    List<UserInoutDto> listIncomeStat(@Param("userId") String userId, @Param("mgmtRegNum") String mgmtRegNum, @Param("year") String year);

    void update(UserInoutDto dto);

    void updateReversing(UserInoutDto dto);

    int delete(UserInoutDto dto);

    int deleteByUserId(String userId);

    void deleteExtra(UserInoutDto dto);

}
