package zinsoft.web.common.service;

import zinsoft.web.common.dto.UserRememberMeDto;

public interface UserRememberMeService {

    void insert(UserRememberMeDto dto);

    UserRememberMeDto get(String series);

    void update(UserRememberMeDto dto);

    void delete(String userId);

}