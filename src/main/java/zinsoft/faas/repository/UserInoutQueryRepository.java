package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserInoutDto;

public interface UserInoutQueryRepository {

    UserInoutDto get(UserInoutDto dto);

    Page<UserInoutDto> page(Map<String, Object> search, Pageable pageable);

    public List<Map<String, Object>> countByAcId(String userId, String trdDt );

}
