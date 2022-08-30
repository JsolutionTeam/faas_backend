package zinsoft.web.common.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.web.common.dto.UserInfoDto;

public interface UserInfoQueryRepository {

    Page<UserInfoDto> page(Map<String, Object> search, Pageable pageable);

    List<UserInfoDto> list(Map<String, Object> search);

    List<String> listUserId(Map<String, Object> search);

}
