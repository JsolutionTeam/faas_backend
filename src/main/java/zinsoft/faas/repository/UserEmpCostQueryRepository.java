package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserEmpCostDto;

public interface UserEmpCostQueryRepository {

    UserEmpCostDto get(String userId, Long userEmpCostSeq);

    Page<UserEmpCostDto> page(Map<String, Object> search, Pageable pageable);

    List<UserEmpCostDto> list(Map<String, Object> search);
    
    List<String> yearList(String userId);
    
}
