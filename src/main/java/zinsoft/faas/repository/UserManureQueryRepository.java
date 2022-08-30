package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserManureDto;

public interface UserManureQueryRepository {

    UserManureDto get(UserManureDto dto);

    Page<UserManureDto> page(Map<String, Object> search, Pageable pageable);

    List<UserManureDto> list(Map<String, Object> search);


}
