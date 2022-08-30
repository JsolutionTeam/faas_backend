package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.SelfLaborCostDto;

public interface SelfLaborCostQueryRepository {

    Page<SelfLaborCostDto> page(Map<String, Object> search, Pageable pageable);

    List<SelfLaborCostDto> list(Map<String, Object> search);
    
    List<String> yearList();

}
