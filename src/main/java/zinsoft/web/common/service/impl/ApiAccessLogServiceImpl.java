package zinsoft.web.common.service.impl;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.web.common.entity.ApiAccessLog;
import zinsoft.web.common.repository.ApiAccessLogRepository;
import zinsoft.web.common.service.ApiAccessLogService;

@Service("apiAccessLogService")
@Slf4j
public class ApiAccessLogServiceImpl extends EgovAbstractServiceImpl implements ApiAccessLogService {

    //private Type dtoListType = new TypeToken<List<ApiAccessLogDto>>() {}.getType();

    @Value("${api.prefix:}")
    private String apiPrefix;

    private final ServletContext servletContext;

    private final ApiAccessLogRepository apiAccessLogRepository;

    @Autowired
    public ApiAccessLogServiceImpl(
            @Autowired ServletContext servletContext,
            @Autowired ApiAccessLogRepository apiAccessLogRepository
    ){
        this.servletContext = servletContext;
        this.apiAccessLogRepository = apiAccessLogRepository;
    }

    @Override
    public void insert(String path, String method, String userId, String remoteAddr, String note) {
        path = path.substring(servletContext.getContextPath().length());
        if ((apiPrefix + "/session").equals(path)) {
            log.info("ApiAccessLogServiceImpl - /session equals!");
            return;
        }

        try {
            apiAccessLogRepository.save(new ApiAccessLog(path, method, userId, remoteAddr, note));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
