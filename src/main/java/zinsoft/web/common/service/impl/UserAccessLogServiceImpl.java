package zinsoft.web.common.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.util.DataTablesResponse;
import zinsoft.web.common.dto.UserAccessLogDto;
import zinsoft.web.common.entity.UserAccessLog;
import zinsoft.web.common.repository.UserAccessLogRepository;
import zinsoft.web.common.service.UserAccessLogService;

@Service("userAccessLogService")
public class UserAccessLogServiceImpl extends EgovAbstractServiceImpl implements UserAccessLogService {

    //private Type dtoListType = new TypeToken<List<UserAccessLogDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    UserAccessLogRepository userAccessLogRepository;

    @Override
    public void insert(String userId, String inOut, String successYn, String note) {
        RequestAttributes reqAttr = (RequestContextHolder.getRequestAttributes());
        String remoteAddr = null;
        String userAgent = null;

        if (reqAttr != null && reqAttr instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) reqAttr).getRequest();

            if (request != null) {
                remoteAddr = request.getRemoteAddr();
                userAgent = request.getHeader("User-Agent");
            }
        }

        UserAccessLogDto dto = new UserAccessLogDto(userId, inOut, successYn, remoteAddr, userAgent, note);
        userAccessLogRepository.save(modelMapper.map(dto, UserAccessLog.class));
    }

    @Override
    public void login(String userId) {
        insert(userId, UserAccessLogDto.INOUT_LOGIN, UserAccessLogDto.SUCCESS_YN_YES, "");
    }

    @Override
    public void login(String userId, String successYn, String note) {
        insert(userId, UserAccessLogDto.INOUT_LOGIN, successYn, note);
    }

    @Override
    public void rememberMe(String userId) {
        insert(userId, UserAccessLogDto.INOUT_REMEMBER_ME, UserAccessLogDto.SUCCESS_YN_YES, "");
    }

    @Override
    public void logout(String userId) {
        insert(userId, UserAccessLogDto.INOUT_LOGOUT, UserAccessLogDto.SUCCESS_YN_YES, "");
    }

    @Override
    public void logout(String userId, String successYn, String note) {
        insert(userId, UserAccessLogDto.INOUT_LOGOUT, successYn, note);
    }

    @Override
    public void sessionDestroyed(String userId) {
        insert(userId, UserAccessLogDto.INOUT_SESSION_DESTROYED, UserAccessLogDto.SUCCESS_YN_YES, "");
    }

    @Override
    public DataTablesResponse<UserAccessLogDto> page(Map<String, Object> search, Pageable pageable) {
        return DataTablesResponse.of(userAccessLogRepository.page(search, pageable));
    }

    @Override
    public List<UserAccessLogDto> list(Map<String, Object> search) {
        return userAccessLogRepository.list(search);
    }

}
