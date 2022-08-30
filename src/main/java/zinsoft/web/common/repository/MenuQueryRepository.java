package zinsoft.web.common.repository;

import java.util.List;

import zinsoft.web.common.dto.MenuDto;

public interface MenuQueryRepository {

    List<MenuDto> list(String siteCd, String roleId, String exprYn);

}
