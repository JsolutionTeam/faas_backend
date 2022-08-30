package zinsoft.web.common.service;

import zinsoft.web.common.dto.UserInfoDto;

public interface UserInfoExtService {

    public int insert(UserInfoDto dto);

    public UserInfoDto get(UserInfoDto userInfo);

    public int update(UserInfoDto dto);

    public int delete(String userId);

}
