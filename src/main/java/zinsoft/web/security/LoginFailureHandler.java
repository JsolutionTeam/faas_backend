package zinsoft.web.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import zinsoft.util.Constants;
import zinsoft.util.Result;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.UserAccessLogService;
import zinsoft.web.common.service.UserInfoService;

public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private ObjectWriter objectWriter = new ObjectMapper().writer();

    @Autowired
    MessageSourceAccessor messageSourceAccessor;

    @Resource
    UserInfoService userInfoService;

    @Resource
    UserAccessLogService userAccessLogService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 로그인 정보
        String userId = request.getParameter(Constants.WEB_SECURITY_USERNAME_PARAMETER);
        String note = exception.toString();
        boolean notApproved = false;

        if (userId != null && !userId.isEmpty()) {
            UserInfoDto userInfo = userInfoService.get(userId);

            if (userInfo != null && UserInfoDto.STATUS_CD_WAITING.equals(userInfo.getStatusCd())) {
                note = "Not approved";
                notApproved = true;
            } else {
                note = exception.toString();
                //loginFailCntService.insert(userId); // TODO
            }
            userAccessLogService.login(userId, Constants.YN_NO, note);
        }

        String code = null;
        Result result = null;

        if (exception instanceof BadCredentialsException) {
            int failCnt = 0; //loginFailCntService.get(userId); // TODO

            if (notApproved) {
                code = Result.USER_NOT_APPROVED;
            } else if (failCnt < 5) {
                code = Result.LOGIN_FAIL;
            } else {
                code = Result.BLOCKED_USER;
                failCnt = 5;
                userInfoService.updateStatusCd(userId, UserInfoDto.STATUS_CD_BLOCK);
            }

            result = new Result(true, code, StringEscapeUtils.unescapeJava(messageSourceAccessor.getMessage("code." + code, new Object[] { failCnt }, Locale.getDefault())));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            code = Result.INTERNAL_SERVER_ERROR;
            result = new Result(true, code);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        writer.write(objectWriter.writeValueAsString(result));
        writer.flush();
    }

}
