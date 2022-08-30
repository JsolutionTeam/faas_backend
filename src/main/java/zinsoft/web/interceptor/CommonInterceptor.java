package zinsoft.web.interceptor;

import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.ApiAccessLogService;

@Component
public class CommonInterceptor implements HandlerInterceptor {

    @Resource
    ApiAccessLogService apiAccessLogService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 현재 시간
        Calendar cal = Calendar.getInstance();
        request.setAttribute("NOW", cal.getTime());
        request.setAttribute("NOW_YEAR", cal.get(Calendar.YEAR));
        request.setAttribute("NOW_MONTH", cal.get(Calendar.MONTH) + 1);
        request.setAttribute("NOW_DAY", cal.get(Calendar.DATE));
        request.setAttribute("NOW_TIME", cal.getTimeInMillis());

        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        String userId = null;
        if (userInfo != null) {
            userInfo.setRemoteAddr(request.getRemoteAddr());
            userId = userInfo.getUserId();
        }

        // API 접근 로그
        apiAccessLogService.insert(request.getRequestURI(), request.getMethod(), userId, request.getRemoteAddr(), null);

        return true;
    }

}
