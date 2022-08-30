package zinsoft.faas.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.RoleUserMapper;
import zinsoft.faas.vo.RoleUser;
import zinsoft.web.common.dto.UserInfoDto;

@Service
public class RoleUserService extends EgovAbstractServiceImpl {

    @Resource
    RoleUserMapper roleUserMapper;

    public void insert(String roleId, String managerId, String[] userIds) {
        roleUserMapper.deleteByManagerId(roleId, managerId);

        RoleUser vo = new RoleUser();
        vo.setRoleId(roleId);
        vo.setManagerId(managerId);

        for (String userId : userIds) {
            vo.setUserId(userId);
            roleUserMapper.insert(vo);
        }
    }

    public List<UserInfoDto> listByManagerId(String roleId, String managerId) {
        return roleUserMapper.listByManagerId(roleId, managerId);
    }

    public void deleteByRoleId(String roleId) {
        roleUserMapper.deleteByRoleId(roleId);
    }

    public void deleteByManagerId(String roleId, String managerId) {
        roleUserMapper.deleteByManagerId(roleId, managerId);
    }

}
