package zinsoft.web.listener;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import zinsoft.web.common.service.UserAccessLogService;
import zinsoft.web.security.WebUser;

@Slf4j
@Component
public class SessionDestroyedListener implements ApplicationListener<HttpSessionDestroyedEvent> {

    private static final String LOGOUT_CHECK_STR = "LogoutFilter";

    @Resource
    UserAccessLogService userAccessLogService;

    @Override
    public void onApplicationEvent(HttpSessionDestroyedEvent event) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        for (StackTraceElement ste : stackTrace) {
            if (ste.getClassName().contains(LOGOUT_CHECK_STR)) {
                return;
            }
        }

        try {
            for (SecurityContext securityContext : event.getSecurityContexts()) {
                Authentication authentication = securityContext.getAuthentication();
                Object principal = authentication.getPrincipal();
                if (principal instanceof WebUser) {
                    WebUser user = (WebUser) principal;
                    userAccessLogService.sessionDestroyed(user.getUsername());
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

}
