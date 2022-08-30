package zinsoft.web.common.service.impl;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.web.common.dto.UserRememberMeDto;
import zinsoft.web.common.entity.UserRememberMe;
import zinsoft.web.common.repository.UserRememberMeRepository;
import zinsoft.web.common.service.UserRememberMeService;

@Service("userRememberMeService")
@Transactional
public class UserRememberMeServiceImpl extends EgovAbstractServiceImpl implements UserRememberMeService {

    @Autowired
    ModelMapper modelMapper;

    @Resource
    UserRememberMeRepository userRememberMeRepository;

    @Override
    public void insert(UserRememberMeDto dto) {
        userRememberMeRepository.save(modelMapper.map(dto, UserRememberMe.class));
    }

    @Override
    public UserRememberMeDto get(String series) {
        UserRememberMe userRememberMe = userRememberMeRepository.findBySeries(series);
        return modelMapper.map(userRememberMe, UserRememberMeDto.class);
    }

    @Override
    public void update(UserRememberMeDto dto) {
        userRememberMeRepository.save(modelMapper.map(dto, UserRememberMe.class));
    }

    @Override
    public void delete(String userId) {
        userRememberMeRepository.deleteByUserId(userId);
    }

}
