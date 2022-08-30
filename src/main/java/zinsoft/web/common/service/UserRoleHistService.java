package zinsoft.web.common.service;

import zinsoft.web.common.dto.UserRoleDto;

public interface UserRoleHistService {

    void insert(String workerId, String workCd, UserRoleDto userRole);

    void insert(String workerId, String workCd, String userId);

    void insertByRoleId(String workerId, String workCd, String roleId);

}