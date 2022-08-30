package zinsoft.web.security.listener;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.stereotype.Component;

import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.UserAccessLogService;
import zinsoft.web.common.service.UserInfoService;
import zinsoft.web.security.WebUser;

@Component
public class RememberMeAuthticationListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @Resource
    UserInfoService userInfoService;

    @Resource
    UserAccessLogService userAccessLogService;

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        Class<?> src = event.getGeneratedBy();

        if (src != RememberMeAuthenticationFilter.class) {
            return;
        }

        // 로그인 정보
        Authentication auth = event.getAuthentication();
        String userId = auth.getName();
        Object principal = auth.getPrincipal();

        if (principal != null && principal instanceof WebUser) {
            String remoteAddr = ((WebAuthenticationDetails) auth.getDetails()).getRemoteAddress();
            UserInfoDto userInfo = ((WebUser) principal).getUser();
            userInfo.setRemoteAddr(remoteAddr);
        }

        userInfoService.updateLastLoginDtm(userId);
        userAccessLogService.rememberMe(userId);
    }

}
