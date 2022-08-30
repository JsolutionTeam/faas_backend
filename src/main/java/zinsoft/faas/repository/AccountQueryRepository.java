package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.AccountDto;

public interface AccountQueryRepository {

    String nextAcId(String upAcId);

    AccountDto get(String acId);

    Page<AccountDto> page(Map<String, Object> search, Pageable pageable);

    List<AccountDto> list(Map<String, Object> search);


}
