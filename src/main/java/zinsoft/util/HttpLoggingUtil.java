package zinsoft.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.extern.slf4j.Slf4j;
import zinsoft.web.common.dto.UserInfoDto;

@Slf4j
public class HttpLoggingUtil {

    public static String error(String msg, Throwable e, HttpServletRequest request) {
        final String logId = RandomStringUtils.randomAlphanumeric(8);
        final UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        final String method = request.getMethod();
        final String newLine = System.getProperty("line.separator");

        msg += newLine + "Log ID: " + logId + newLine;
        msg += "Method: " + method + newLine;
        msg += "Path: " + request.getServletPath() + newLine;

        if (userInfo != null) {
            msg += "User: " + userInfo.getUserId() + newLine;
        }

        msg += "Query: " + request.getQueryString() + newLine;
        msg += "Request Param:";

        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = request.getParameter(name);
            msg += "  " + name + "=" + value + newLine;
        }

        msg += newLine;

        log.error(msg, e);

        return logId;
    }

}
