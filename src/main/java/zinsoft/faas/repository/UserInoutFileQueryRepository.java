package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import zinsoft.faas.dto.UserInoutFileDto;

public interface UserInoutFileQueryRepository {

    List<UserInoutFileDto> list(Map<String, Object> search);

}
