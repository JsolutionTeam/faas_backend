package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserDiaryDto;

public interface UserDiaryQueryRepository {

    UserDiaryDto get(UserDiaryDto dto);

    Page<UserDiaryDto> page(Map<String, Object> search, Pageable pageable);

    List<String> listYear(String userId, String diaryTCd, String year);

    List<Map<String, Object>> countByActSeq(String userId, String actDt);

}
