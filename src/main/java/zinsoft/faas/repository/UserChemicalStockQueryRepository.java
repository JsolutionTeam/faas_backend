package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserChemicalStockDto;

public interface UserChemicalStockQueryRepository {

    UserChemicalStockDto get(String userId, Long userChemicalStockSeq);

    Page<UserChemicalStockDto> page(Map<String, Object> search, Pageable pageable);

    List<UserChemicalStockDto> list(Map<String, Object> search);
}
