package zinsoft.web.common.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.web.common.repository.UserInfoHistRepository;
import zinsoft.web.common.service.UserInfoHistService;

@Service("userInfoHistService")
@Transactional
public class UserInfoHistServiceImpl extends EgovAbstractServiceImpl implements UserInfoHistService {

    @Resource
    UserInfoHistRepository userInfoHistRepository;

    @Override
    public void insert(String workerId, String workCd, String userId) {
        userInfoHistRepository.insertByUserId(workerId, workCd, userId);
    }

}
