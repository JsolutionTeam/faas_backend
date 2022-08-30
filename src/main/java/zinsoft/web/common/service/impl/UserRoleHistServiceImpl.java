package zinsoft.web.common.service.impl;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.web.common.dto.UserRoleDto;
import zinsoft.web.common.entity.UserRoleHist;
import zinsoft.web.common.repository.UserRoleHistRepository;
import zinsoft.web.common.service.UserRoleHistService;

@Service("userRoleHistService")
@Transactional
public class UserRoleHistServiceImpl extends EgovAbstractServiceImpl implements UserRoleHistService {

    @Autowired
    ModelMapper modelMapper;

    @Resource
    UserRoleHistRepository userRoleHistRepository;

    @Override
    public void insert(String workerId, String workCd, UserRoleDto userRoleDto) {
        UserRoleHist userRoleHist = modelMapper.map(userRoleDto, UserRoleHist.class);

        userRoleHist.setWorkerId(workerId);
        userRoleHist.setWorkCd(workCd);
        userRoleHistRepository.save(userRoleHist);
    }

    @Override
    public void insert(String workerId, String workCd, String userId) {
        userRoleHistRepository.insertByUserId(workerId, workCd, userId);
    }

    @Override
    public void insertByRoleId(String workerId, String workCd, String roleId) {
        userRoleHistRepository.insertByRoleId(workerId, workCd, roleId);
    }

}
