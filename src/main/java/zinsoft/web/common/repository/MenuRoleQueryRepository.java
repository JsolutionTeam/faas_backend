package zinsoft.web.common.repository;

import java.util.List;

import zinsoft.web.common.dto.MenuRoleDto;

public interface MenuRoleQueryRepository {

    List<MenuRoleDto> list(MenuRoleDto dto);

    void deleteByRoleId(String roleId);

}
