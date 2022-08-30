package zinsoft.web.common.service;

import java.util.List;
import java.util.ListIterator;

import zinsoft.util.HierarchicalMenu;
import zinsoft.web.common.dto.MenuDto;

public interface MenuService {

    List<MenuDto> list(String siteCd, String roleId, String exprYn);

    List<MenuDto> list(String siteCd);

    List<HierarchicalMenu> makeMenuHierarchy(List<MenuDto> menuList, ListIterator<MenuDto> menuIList, int curDepth);

    void reload();

}