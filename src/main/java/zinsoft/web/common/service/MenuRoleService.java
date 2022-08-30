package zinsoft.web.common.service;

import java.util.List;

import zinsoft.web.common.dto.MenuRoleDto;

public interface MenuRoleService {

    void insert(String workerId, MenuRoleDto dto);

    void insert(String workerId, String menuId, String roleId, String actCd);

    void insert(String workerId, String roleId, String[] listMenuIds, String[] insertMenuIds, String[] exprMenuIds);

    List<MenuRoleDto> listByRoleId(String roleId);

    void update(String workerId, String roleId, String[] listMenuIds, String[] insertMenuIds, String[] exprMenuIds);

    void delete(String workerId, MenuRoleDto dto);

    void delete(String workerId, String menuId, String roleId, String actCd);

    void deleteByRoleId(String workerId, String roleId);

}