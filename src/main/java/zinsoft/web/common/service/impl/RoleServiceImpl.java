package zinsoft.web.common.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.util.Constants;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.common.dto.RoleDto;
import zinsoft.web.common.entity.Role;
import zinsoft.web.common.repository.RoleRepository;
import zinsoft.web.common.service.CommonDataService;
import zinsoft.web.common.service.MenuRoleService;
import zinsoft.web.common.service.RoleService;
import zinsoft.web.common.service.UserRoleService;
import zinsoft.web.exception.CodeMessageException;

@Service("roleService")
@Transactional
public class RoleServiceImpl extends EgovAbstractServiceImpl implements RoleService {

    //private Type dtoListType = new TypeToken<List<RoleDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    RoleRepository roleRepository;

    @Resource
    UserRoleService userRoleService;

    @Resource
    MenuRoleService menuRoleService;

    @Resource
    CommonDataService commonDataService;

    @Override
    public void insert(String workerId, RoleDto dto, String[] listMenuIds, String[] insertMenuIds, String[] exprMenuIds) {
        roleRepository.save(modelMapper.map(dto, Role.class));
        menuRoleService.insert(workerId, dto.getRoleId(), listMenuIds, insertMenuIds, exprMenuIds);
        commonDataService.reload();
    }

    @Override
    public DataTablesResponse<RoleDto> page(Map<String, Object> search, Pageable pageable) {
        return DataTablesResponse.of(roleRepository.page(search, pageable));
    }

    @Override
    public List<RoleDto> list() {
        return list(new HashMap<>());
    }

    @Override
    public List<RoleDto> list(Map<String, Object> search) {
        return roleRepository.list(search);
    }

    @Override
    public RoleDto get(String roleId) {
        Role role = getEntity(roleId);
        return modelMapper.map(role, RoleDto.class);
    }

    @Override
    public void update(String workerId, RoleDto dto, String[] listMenuIds, String[] insertMenuIds, String[] exprMenuIds) {
        Role role = getEntity(dto.getRoleId());

        dto.setRegDtm(null);
        dto.setUpdateDtm(new Date());

        modelMapper.map(dto, role);

        menuRoleService.update(workerId, dto.getRoleId(), listMenuIds, insertMenuIds, exprMenuIds);
        commonDataService.reload();
    }

    @Override
    public int delete(String workerId, String roleId) {
        Role role = getEntity(roleId);

        userRoleService.deleteByRoleId(workerId, roleId);
        menuRoleService.deleteByRoleId(workerId, roleId);

        role.setRegDtm(null);
        role.setUpdateDtm(new Date());
        role.setStatusCd(Constants.STATUS_CD_DELETE);

        commonDataService.reload();

        return 1;
    }

    @Override
    public int delete(String workerId, String[] roleIds) {
        int cnt = 0;

        for (String roleId : roleIds) {
            cnt += delete(workerId, roleId);
        }

        return cnt;
    }

    private Role getEntity(String roleId) {
        Role entity = roleRepository.findByRoleIdAndStatusCd(roleId, Constants.STATUS_CD_NORMAL);

        if (entity == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return entity;
    }

}
