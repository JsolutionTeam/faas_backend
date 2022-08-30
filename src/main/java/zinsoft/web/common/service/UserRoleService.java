package zinsoft.web.common.service;

import java.util.List;

import zinsoft.web.common.dto.UserRoleDto;

public interface UserRoleService {

    void insert(String workerId, UserRoleDto dto);

    void insert(String workerId, String userId, String roleId);

    void insert(String workerId, String userId, List<UserRoleDto> list);

    List<UserRoleDto> list(String userId);

    void update(String workerId, UserRoleDto dto);

    void update(String workerId, String userId, String roleId);

    void update(String workerId, String userId, List<UserRoleDto> list);

    void delete(String workerId, UserRoleDto dto);

    void delete(String workerId, String userId, String roleId);

    void deleteByUserId(String workerId, String userId);

    void deleteByRoleId(String workerId, String roleId);

}