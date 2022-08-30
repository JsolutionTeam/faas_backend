package zinsoft.web.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import zinsoft.util.Result;
import zinsoft.web.common.service.UserAccessLogService;

public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private ObjectWriter objectWriter = new ObjectMapper().writer();

    @Resource
    UserAccessLogService userAccessLogService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            userAccessLogService.logout(authentication.getName());
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        writer.write(objectWriter.writeValueAsString(new Result(true, Result.OK)));
        writer.flush();
    }

}
