package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserChemicalDto;

public interface UserChemicalQueryRepository {

    UserChemicalDto get(UserChemicalDto dto);

    Page<UserChemicalDto> page(Map<String, Object> search, Pageable pageable);

    List<UserChemicalDto> list(Map<String, Object> search);


}
