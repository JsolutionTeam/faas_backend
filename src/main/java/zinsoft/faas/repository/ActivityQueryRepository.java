package zinsoft.faas.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.ActivityDto;

public interface ActivityQueryRepository {

    Page<ActivityDto> page(Map<String, Object> search, Pageable pageable);

}
