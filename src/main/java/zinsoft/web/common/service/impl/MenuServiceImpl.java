package zinsoft.web.common.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.util.Constants;
import zinsoft.util.HierarchicalMenu;
import zinsoft.web.common.dto.MenuDto;
import zinsoft.web.common.dto.RoleDto;
import zinsoft.web.common.entity.Menu;
import zinsoft.web.common.repository.MenuRepository;
import zinsoft.web.common.service.MenuService;
import zinsoft.web.common.service.RoleService;

@Service("menuService")
public class MenuServiceImpl extends EgovAbstractServiceImpl implements MenuService {

    private Type dtoListType = new TypeToken<List<MenuDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    Map<String, List<MenuDto>> menuListMap;

    @Resource
    Map<String, HierarchicalMenu> hierarchicalMenuMap;

    @Resource
    MenuRepository menuRepository;

    @Resource
    RoleService roleService;

    @Override
    public List<MenuDto> list(String siteCd, String roleId, String exprYn) {
        return menuRepository.list(siteCd, roleId, exprYn);
    }

    @Override
    public List<MenuDto> list(String siteCd) {
        List<Menu> list = menuRepository.findBySiteCdAndStatusCdOrderBySortOrderAscDepthAsc(siteCd, Constants.STATUS_CD_NORMAL);
        return modelMapper.map(list, dtoListType);
    }

    @Override
    public List<HierarchicalMenu> makeMenuHierarchy(List<MenuDto> menuList, ListIterator<MenuDto> menuIList, int curDepth) {
        MenuDto dto = null;
        MenuDto prev = null;
        List<MenuDto> parentList = null;
        List<HierarchicalMenu> hierarchicalMenuList = new ArrayList<>();
        HierarchicalMenu hierarchicalMenu = null;
        int depth = 0;
        int parentDepth = 0;

        while (menuIList.hasNext()) {
            dto = menuIList.next();
            depth = dto.getDepth();
            parentDepth = depth - 1;

            if (curDepth == depth) {
                int prevIdx = menuIList.previousIndex();

                parentList = new ArrayList<>();
                for (int i = prevIdx; i >= 0; i--) {
                    prev = menuList.get(i);
                    if (prev.getDepth().intValue() == parentDepth) {
                        parentList.add(prev);
                        parentDepth--;
                    }
                }
                Collections.reverse(parentList);
                dto.setParentList(parentList);

                hierarchicalMenu = new HierarchicalMenu();
                hierarchicalMenu.setMenu(dto);
                hierarchicalMenuList.add(hierarchicalMenu);
            } else if (curDepth < depth) {
                menuIList.previous();
                if (hierarchicalMenu != null) {
                    hierarchicalMenu.setSubMenu(makeMenuHierarchy(menuList, menuIList, depth));
                }
            } else {
                menuIList.previous();
                return hierarchicalMenuList;
            }
        }

        return hierarchicalMenuList;
    }

    @Override
    public void reload() {
        List<RoleDto> roleList = roleService.list();

        menuListMap.clear();
        hierarchicalMenuMap.clear();

        for (RoleDto role : roleList) {
            String roleId = role.getRoleId();
            List<MenuDto> list = list(Constants.SITE_CD, roleId, Constants.YN_YES);
            menuListMap.put(roleId, list);

            List<HierarchicalMenu> hierarchicalMenuList = makeMenuHierarchy(list, list.listIterator(), 0);
            HierarchicalMenu mainMenu = new HierarchicalMenu();
            mainMenu.setMenu(new MenuDto("Main"));
            mainMenu.setSubMenu(hierarchicalMenuList);
            hierarchicalMenuMap.put(roleId, mainMenu);
        }
    }

}
