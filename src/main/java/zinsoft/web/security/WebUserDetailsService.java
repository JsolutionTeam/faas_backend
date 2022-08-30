package zinsoft.web.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.dto.UserRoleDto;
import zinsoft.web.common.service.UserInfoService;

public class WebUserDetailsService implements UserDetailsService {

    @Resource
    UserInfoService userInfoService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        if (userId == null || userId.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist: " + userId);
        }

        UserInfoDto userInfo = userInfoService.get(userId);

        if (userInfo == null || !userInfo.isValid()) {
            throw new UsernameNotFoundException("User does not exist: " + userId);
        }

        List<UserRoleDto> userRoleList = userInfo.getUserRoleList();
        List<GrantedAuthority> authList = new ArrayList<>();

        if (userRoleList != null) {
            for (UserRoleDto userRole : userRoleList) {
                authList.add(new SimpleGrantedAuthority(userRole.getRoleId()));
            }
        }

        return new WebUser(userInfo, authList);
    }

}
