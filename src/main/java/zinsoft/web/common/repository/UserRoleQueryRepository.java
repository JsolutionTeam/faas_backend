package zinsoft.web.common.repository;

import java.util.List;

import zinsoft.web.common.dto.UserRoleDto;

public interface UserRoleQueryRepository {

    List<UserRoleDto> listByUserId(String userId);

    void deleteByUserId(String userId);

    void deleteByRoleId(String roleId);

}
