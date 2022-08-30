package zinsoft.web.common.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.web.common.dto.MenuRoleDto;
import zinsoft.web.common.entity.MenuRoleHist;
import zinsoft.web.common.repository.MenuRoleHistRepository;
import zinsoft.web.common.service.MenuRoleHistService;

@Service("menuRoleHistService")
@Transactional
public class MenuRoleHistServiceImpl extends EgovAbstractServiceImpl implements MenuRoleHistService {

    @Resource
    MenuRoleHistRepository menuRoleHistRepository;

    @Override
    public void insert(String workerId, String workCd, MenuRoleDto menuRoleDto) {
        menuRoleHistRepository.save(new MenuRoleHist(workerId, workCd, menuRoleDto.getMenuId(), menuRoleDto.getRoleId(), menuRoleDto.getActCd()));
    }

    @Override
    public void insert(String workerId, String workCd, String roleId) {
        menuRoleHistRepository.insertByRoleId(workerId, workCd, roleId);
    }

}
