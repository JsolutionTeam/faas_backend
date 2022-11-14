package zinsoft.web.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zinsoft.util.HierarchicalMenu;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.CommonDataService;
import zinsoft.web.common.service.MenuAccessLogService;
import zinsoft.web.exception.CodeMessageException;

@RestController
@RequestMapping("${api.prefix}")
@Slf4j
public class BasicController {

    @Resource
    Map<String, HierarchicalMenu> hierarchicalMenuMap;

    @Resource
    CommonDataService commonDataService;

    @Resource
    MenuAccessLogService menuAccessLogService;

    @RequestMapping(value = "/")
    public Result index(HttpServletRequest request) {
        String name = request.getServletContext().getServletContextName(); // web.xml의 display-name
        return new Result(true, Result.OK, name + " Server");
    }

    @RequestMapping(value = "/page/error")
    public Result error(HttpServletRequest request) {
        Result result = (Result) request.getAttribute("result");

        if (result != null) {
            return result;
        }

        String code = request.getParameter("code");
        Object[] args = new Object[1];

        if (code != null && !code.isEmpty()) {
            args[0] = request.getParameter("arg");
        } else {
            Throwable e = (Throwable) request.getAttribute("javax.servlet.error.exception");

            if (e instanceof CodeMessageException) {
                CodeMessageException cme = (CodeMessageException) e;
                code = cme.getCode();
                args[0] = cme.getArg();
            } else {
                String type = request.getAttribute("javax.servlet.error.exception_type").toString().replace("class ", "");
                code = Result.INTERNAL_SERVER_ERROR;
                args[0] = type;
            }
        }

        return new Result(false, code, args);
    }

    @GetMapping("/session")
    public Result session(String menuId, boolean withMenu) {
        log.info("request /session, menuId : {}, withMenu : {}", menuId, withMenu);
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        log.info("/session - userInfo : {}", userInfo);
        if (StringUtils.isNotBlank(menuId)) {
            String userId = userInfo != null ? userInfo.getUserId() : null;
            log.info("/session - userId : {}", userId);
            menuAccessLogService.insert(menuId, userId, null);
        }

        // 권한관리에서 메뉴 권한이 수정될 수 있으므로 실제로 세션에 데이터를 넣으면 안됨

        String roleId = UserInfoUtil.getMainRoleId(userInfo);
        log.info("/session - roleId : {}", roleId);
        Map<String, Object> ret = new HashMap<>();

        ret.put("userInfo", userInfo);

        if (withMenu) {
            HierarchicalMenu hierarchicalMenu = hierarchicalMenuMap.get(roleId);
            ret.put("menuList", hierarchicalMenu != null ? hierarchicalMenu.getSubMenu() : null);
        }
        log.info("/session - ret : {}", ret);

        return new Result(true, Result.OK, ret);
    }

    @GetMapping("/basicdata/reload")
    public Result reload() {
        commonDataService.reload();
        return new Result(true, Result.OK);
    }

}
