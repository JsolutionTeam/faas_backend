package zinsoft.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.cmmn.trace.handler.DefaultTraceHandler;
import egovframework.rte.fdl.cmmn.trace.handler.TraceHandler;
import egovframework.rte.fdl.cmmn.trace.manager.DefaultTraceHandleManager;
import egovframework.rte.fdl.cmmn.trace.manager.TraceHandlerService;

@Configuration
public class EGovConfig {

    @Bean
    public LeaveaTrace leaveaTrace(TraceHandlerService egovTraceHandlerService) {
        LeaveaTrace leaveaTrace = new LeaveaTrace();

        leaveaTrace.setTraceHandlerServices(new TraceHandlerService[] { egovTraceHandlerService });

        return leaveaTrace;
    }

    @Bean
    public TraceHandlerService egovTraceHandlerService(PathMatcher egovPathMatcher, TraceHandler egovTraceHandler) {
        TraceHandlerService traceHandlerService = new DefaultTraceHandleManager();

        traceHandlerService.setReqExpMatcher(egovPathMatcher);
        traceHandlerService.setPatterns(new String[] { "*" });
        traceHandlerService.setHandlers(new TraceHandler[] { egovTraceHandler });

        return traceHandlerService;
    }

    @Bean
    public PathMatcher egovPathMatcher() {
        return new AntPathMatcher();
    }

    @Bean
    public TraceHandler egovTraceHandler() {
        return new DefaultTraceHandler();
    }

}
