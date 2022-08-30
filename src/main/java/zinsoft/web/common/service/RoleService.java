package zinsoft.web.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.util.DataTablesResponse;
import zinsoft.web.common.dto.RoleDto;

public interface RoleService {

    void insert(String workerId, RoleDto dto, String[] listMenuIds, String[] insertMenuIds, String[] exprMenuIds);

    DataTablesResponse<RoleDto> page(Map<String, Object> search, Pageable pageable);

    List<RoleDto> list();

    List<RoleDto> list(Map<String, Object> search);

    RoleDto get(String roleId);

    void update(String workerId, RoleDto dto, String[] listMenuIds, String[] insertMenuIds, String[] exprMenuIds);

    int delete(String workerId, String roleId);

    int delete(String workerId, String[] roleIds);

}