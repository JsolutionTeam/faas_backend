package zinsoft.faas.service.impl;

import org.springframework.stereotype.Service;

import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.UserInfoExtService;

@Service
public class UserInfoExtServiceImpl implements UserInfoExtService {

    @Override
    public int insert(UserInfoDto userInfo) {
        return 0;
    }

    @Override
    public UserInfoDto get(UserInfoDto userInfo) {
        return userInfo;
    }

    @Override
    public int update(UserInfoDto dto) {
        return 0;
    }

    @Override
    public int delete(String userId) {
        return 0;
    }

}
