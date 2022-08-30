package zinsoft.web.common.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.web.common.dto.RoleDto;

public interface RoleQueryRepository {

    Page<RoleDto> page(Map<String, Object> search, Pageable pageable);

    List<RoleDto> list(Map<String, Object> search);

}
