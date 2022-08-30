package zinsoft.web.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.web.common.dto.UserRoleDto;
import zinsoft.web.common.repository.UserRoleRepository;
import zinsoft.web.common.service.UserRoleHistService;
import zinsoft.web.common.service.UserRoleService;

@Service("userRoleService")
@Transactional
public class UserRoleServiceImpl extends EgovAbstractServiceImpl implements UserRoleService {

    @Autowired
    ModelMapper modelMapper;

    @Resource
    UserRoleRepository userRoleRepository;

    @Resource
    UserRoleHistService userRoleHistService;

    @Override
    public void insert(String workerId, UserRoleDto dto) {
        /*
        userRoleRepository.save(modelMapper.map(dto, UserRole.class));
        userRoleHistService.insert(workerId, Constants.WORK_CD_INSERT, dto);
        */
    }

    @Override
    public void insert(String workerId, String userId, String roleId) {
        insert(workerId, new UserRoleDto(userId, roleId));
    }

    @Override
    public void insert(String workerId, String userId, List<UserRoleDto> list) {
        for (UserRoleDto dto : list) {
            dto.setUserId(userId);
            insert(workerId, dto);
        }
    }

    @Override
    public List<UserRoleDto> list(String userId) {
        return userRoleRepository.listByUserId(userId);
    }

    @Override
    public void update(String workerId, UserRoleDto dto) {
        /*
        userRoleRepository.save(modelMapper.map(dto, UserRole.class));
        userRoleHistService.insert(workerId, Constants.WORK_CD_UPDATE, dto);
        */
    }

    @Override
    public void update(String workerId, String userId, String roleId) {
        update(workerId, new UserRoleDto(userId, roleId));
    }

    @Override
    public void update(String workerId, String userId, List<UserRoleDto> list) {
        /*
        userRoleRepository.deleteByUserId(userId);

        for (UserRoleDto dto : list) {
            dto.setUserId(userId);
            update(workerId, dto);
        }
        */
    }

    @Override
    public void delete(String workerId, UserRoleDto dto) {
        /*
        userRoleHistService.insert(workerId, Constants.WORK_CD_DELETE, dto);
        userRoleRepository.deleteById(modelMapper.map(dto, UserRole.class));
        */
    }

    @Override
    public void delete(String workerId, String userId, String roleId) {
        delete(workerId, new UserRoleDto(userId, roleId));
    }

    @Override
    public void deleteByUserId(String workerId, String userId) {
        /*
        userRoleHistService.insert(workerId, Constants.WORK_CD_DELETE, userId);
        userRoleRepository.deleteByUserId(userId);
        */
    }

    @Override
    public void deleteByRoleId(String workerId, String roleId) {
        /*
        userRoleHistService.insertByRoleId(workerId, Constants.WORK_CD_DELETE, roleId);
        userRoleRepository.deleteByRoleId(roleId);
        */
    }

}
