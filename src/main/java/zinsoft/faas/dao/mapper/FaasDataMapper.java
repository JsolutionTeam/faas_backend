package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("FaasDataMapper")
public interface FaasDataMapper {

    List<Map<String, Object>> getDataCount(@Param("userId") String userId, @Param("startDt") String startDt, @Param("endDt") String endDt);

    List<Map<String, Object>> getFarmingStatus(String userId);

    List<Map<String, Object>> getInoutStatus(String userId);

    List<Map<String, Object>> getInout(String userId);
}
