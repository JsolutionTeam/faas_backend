package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserProductionDto;

public interface UserProductionQueryRepository {

    UserProductionDto get(Long userProductionSeq, String userId);

    Page<UserProductionDto> page(Map<String, Object> search, Pageable pageable);

    List<UserProductionDto> totalByPlanTCd(Map<String, Object> search);

    List<Map<String, Object>> chartByPlanTCd(Map<String, Object> search);


}
