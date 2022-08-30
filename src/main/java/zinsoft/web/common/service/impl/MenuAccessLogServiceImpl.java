package zinsoft.web.common.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.web.common.entity.MenuAccessLog;
import zinsoft.web.common.repository.MenuAccessLogRepository;
import zinsoft.web.common.service.MenuAccessLogService;

@Service("menuAccessLogService")
public class MenuAccessLogServiceImpl extends EgovAbstractServiceImpl implements MenuAccessLogService {

    @Resource
    MenuAccessLogRepository menuAccessLogRepository;

    @Override
    public void insert(String menuId, String userId, String note) {
        RequestAttributes reqAttr = (RequestContextHolder.getRequestAttributes());
        String remoteAddr = null;

        if (reqAttr != null && reqAttr instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) reqAttr).getRequest();

            if (request != null) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        try {
            menuAccessLogRepository.save(new MenuAccessLog(menuId, userId, remoteAddr, note));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
