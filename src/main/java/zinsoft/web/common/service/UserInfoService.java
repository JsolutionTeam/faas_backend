package zinsoft.web.common.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Pageable;

import zinsoft.util.DataTablesResponse;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.exception.CodeMessageException;

public interface UserInfoService {

    void insert(String workerId, UserInfoDto dto) throws CodeMessageException;

    void insert(String workerId, UserInfoDto dto, String baseUri) throws CodeMessageException;

    DataTablesResponse<UserInfoDto> page(Map<String, Object> search, Pageable pageable);

    List<UserInfoDto> list(Map<String, Object> search);

    List<String> listUserId(Map<String, Object> search);

    List<UserInfoDto> listByRoleId(String roleId);

    UserInfoDto get(String userId);

    boolean findPwd(String userId, String userNm, String mobileNum, String baseUri) throws CodeMessageException;

    void login(String userId, HttpSession session, String remoteAddr);

    void update(String workerId, UserInfoDto dto) throws CodeMessageException;

    void updatePwd(String workerId, UserInfoDto dto) throws CodeMessageException;

    String resetPwd(String workerId, String userId) throws CodeMessageException;

    void updateStatusCd(UserInfoDto dto);

    void updateStatusCd(String userId, String statusCd);

    void updateStatusCd(String[] userIds, String statusCd);

    void updateApproval(String workerId, String[] userIds, String roleId, String approval, String baseUri);

    void updateLastLoginDtm(String userId);

    void delete(String workerId, String userId);

    void delete(String workerId, String[] userIds);

}