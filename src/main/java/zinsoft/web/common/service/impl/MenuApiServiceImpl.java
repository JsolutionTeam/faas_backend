package zinsoft.web.common.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.web.common.dto.RestfulApiAccessDto;
import zinsoft.web.common.repository.MenuApiRepository;
import zinsoft.web.common.service.MenuApiService;

@Service("restfulApiAccessService")
public class MenuApiServiceImpl extends EgovAbstractServiceImpl implements MenuApiService {

    @Resource
    MenuApiRepository menuApiRepository;

    @Override
    public List<RestfulApiAccessDto> listRestfulApiAccess() {
        return menuApiRepository.listRestfulApiAccess();
    }

}
