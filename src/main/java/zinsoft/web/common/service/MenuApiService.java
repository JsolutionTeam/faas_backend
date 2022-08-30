package zinsoft.web.common.service;

import java.util.List;

import zinsoft.web.common.dto.RestfulApiAccessDto;

public interface MenuApiService {

    List<RestfulApiAccessDto> listRestfulApiAccess();

}