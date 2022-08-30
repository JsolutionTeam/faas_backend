package zinsoft.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import zinsoft.util.Result;

//@Component
//@Order(1)
public class IpFilter implements Filter {

    private Set<String> whiteIpList;
    private List<AntPathRequestMatcher> whitePathList = Arrays.asList(
        new AntPathRequestMatcher("/static/**"),
        new AntPathRequestMatcher("/content/error"),
        new AntPathRequestMatcher("/favicon.ico")
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        final String remoteAddr = request.getRemoteAddr();
        final Set<String> ipList = whiteIpList;
        boolean pass = false;
 
        for (AntPathRequestMatcher matcher : whitePathList) {
            if (matcher.matches(request)) {
                pass = true;
                break;
            }
        }

        if (pass
                || ipList != null && ipList.contains(remoteAddr)
                || "127.0.0.1".equals(remoteAddr)
                || "0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            chain.doFilter(servletRequest, servletResponse);
        } else {
            RequestDispatcher rd = request.getRequestDispatcher("/content/error");

            request.setAttribute("result", new Result(false, Result.IP_NOT_ALLOWED, new String[] { remoteAddr }));
            rd.forward(servletRequest, servletResponse);
        }
    }

    public void setWhiteIpList(Set<String> whiteIpList) {
        this.whiteIpList = whiteIpList;
    }

}