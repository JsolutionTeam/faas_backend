package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserManureStockDto;

public interface UserManureStockQueryRepository {

    UserManureStockDto get(String userId, Long userManureStockSeq);

    Page<UserManureStockDto> page(Map<String, Object> search, Pageable pageable);

    List<UserManureStockDto> list(Map<String, Object> search);
}
