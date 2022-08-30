package zinsoft.web.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import zinsoft.web.common.dto.UserInfoDto;

public class WebUser extends User {

    private static final long serialVersionUID = -8861696887269066327L;
    private UserInfoDto userInfo;

    public WebUser(UserInfoDto userInfo, Collection<? extends GrantedAuthority> authorities) {
        super(userInfo.getUserId(), userInfo.getUserPwd(), authorities);
        this.userInfo = userInfo;
    }

    public WebUser(UserInfoDto userInfo, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(userInfo.getUserId(), userInfo.getUserPwd(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userInfo = userInfo;
    }

    public UserInfoDto getUser() {
        return userInfo;
    }

    public void setUser(UserInfoDto user) {
        this.userInfo = user;
    }

}
