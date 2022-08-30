package zinsoft.web.common.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zinsoft.util.Constants;
import zinsoft.util.HierarchicalMenu;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.CommonDataService;
import zinsoft.web.common.service.MenuRoleService;
import zinsoft.web.common.service.MenuService;
import zinsoft.web.exception.CodeMessageException;

@RestController
@RequestMapping("${api.prefix}")
public class MenuController {

    @Resource
    Map<String, HierarchicalMenu> hierarchicalMenuMap;

    @Resource
    MenuService menuService;

    @Resource
    MenuRoleService menuRoleService;

    @Resource
    CommonDataService commonDataService;

    @GetMapping("/menu")
    public Result list() {
        return new Result(true, Result.OK, menuService.list(Constants.SITE_CD));
    }

    @GetMapping("/hmenu")
    public Result hierarchicalMenu(HttpServletRequest request) {
        String roleId = UserInfoUtil.getMainRoleId();
        HierarchicalMenu hierarchicalMenu = hierarchicalMenuMap.get(roleId);

        request.setAttribute("HIERARCHICAL_MENU", hierarchicalMenu);

        if (hierarchicalMenu == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return new Result(true, Result.OK, hierarchicalMenu.getSubMenu());
    }

    @GetMapping("/menu-role/{roleId}")
    public Result listMenuRole(@PathVariable String roleId) {
        return new Result(true, Result.OK, menuRoleService.listByRoleId(roleId));
    }

    @PutMapping("/menu-role/{roleId}")
    public Result update(String roleId, String[] listMenuIds, String[] insertMenuIds, String[] exprMenuIds) {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        menuRoleService.update(userInfo.getUserId(), roleId, listMenuIds, insertMenuIds, exprMenuIds);
        commonDataService.reload();

        return new Result(true, Result.OK);
    }

}
