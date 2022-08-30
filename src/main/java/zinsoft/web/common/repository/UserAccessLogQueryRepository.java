package zinsoft.web.common.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.web.common.dto.UserAccessLogDto;

public interface UserAccessLogQueryRepository {

    Page<UserAccessLogDto> page(Map<String, Object> search, Pageable pageable);

    List<UserAccessLogDto> list(Map<String, Object> search);

}
