package zinsoft.web.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import zinsoft.util.Constants;
import zinsoft.util.Result;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.UserAccessLogService;
import zinsoft.web.common.service.UserInfoService;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private ObjectWriter objectWriter = new ObjectMapper().writer();

    @Resource
    UserInfoService userInfoService;

    @Resource
    UserAccessLogService userAccessLogService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 정보
        String userId = request.getParameter(Constants.WEB_SECURITY_USERNAME_PARAMETER);
        UserInfoDto userInfo = null;

        Object principal = authentication.getPrincipal();
        if (principal instanceof WebUser) {
            userInfo = ((WebUser) principal).getUser();
            userInfo.setRemoteAddr(request.getRemoteAddr());
        }

        // 기능이 주석처리 되어있어서 사용x
//        userInfoService.updateLastLoginDtm(userId);
        userAccessLogService.login(userId);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        logger.info("로그인 유저 id : " + userInfo);

        writer.write(objectWriter.writeValueAsString(new Result(true, Result.OK, userInfo)));
        writer.flush();
    }

}
