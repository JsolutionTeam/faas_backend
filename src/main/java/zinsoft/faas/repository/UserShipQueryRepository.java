package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserShipDto;

public interface UserShipQueryRepository {

    UserShipDto get(Long userShipSeq, String userId);

    Page<UserShipDto> page(Map<String, Object> search, Pageable pageable);

    List<UserShipDto> totalByPlanTCd(Map<String, Object> search);

    List<Map<String, Object>> chartByPlanTCd(Map<String, Object> search);


}
