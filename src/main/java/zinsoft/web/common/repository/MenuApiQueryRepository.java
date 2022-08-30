package zinsoft.web.common.repository;

import java.util.List;

import zinsoft.web.common.dto.RestfulApiAccessDto;

public interface MenuApiQueryRepository {

    List<RestfulApiAccessDto> listRestfulApiAccess();

}
