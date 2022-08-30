package zinsoft.web.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.util.DataTablesResponse;
import zinsoft.web.common.dto.UserAccessLogDto;

public interface UserAccessLogService {

    void insert(String userId, String inOut, String successYn, String note);

    void login(String userId);

    void login(String userId, String successYn, String note);

    void rememberMe(String userId);

    void logout(String userId);

    void logout(String userId, String successYn, String note);

    void sessionDestroyed(String userId);

    DataTablesResponse<UserAccessLogDto> page(Map<String, Object> search, Pageable pageable);

    List<UserAccessLogDto> list(Map<String, Object> search);

}