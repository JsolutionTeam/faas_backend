package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import zinsoft.faas.dto.UserDiaryFileDto;

public interface UserDiaryFileQueryRepository {

    List<UserDiaryFileDto> list(Map<String, Object> search);

}
