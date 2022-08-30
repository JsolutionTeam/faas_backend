package zinsoft.web.common.service;

import zinsoft.web.common.dto.MenuRoleDto;

public interface MenuRoleHistService {

    void insert(String workerId, String workCd, MenuRoleDto menuRole);

    void insert(String workerId, String workCd, String roleId);

}