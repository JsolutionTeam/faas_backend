package zinsoft.web.common.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.jxls.common.Context;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import zinsoft.util.CommonUtil;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.RoleDto;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.RoleService;
import zinsoft.web.common.view.JxlsView;
import zinsoft.web.exception.CodeMessageException;

@RestController
@RequestMapping("${api.prefix}")
public class RoleController {

    @Resource
    RoleService roleService;

    @GetMapping("/role.xlsx")
    public ModelAndView xlsx(@RequestParam Map<String, Object> search) {
        Context jxlsContext = new Context();
        ModelAndView mv = new ModelAndView(new JxlsView(jxlsContext));
        List<RoleDto> list = roleService.list(search);

        jxlsContext.putVar("search", search);
        jxlsContext.putVar("list", list);

        mv.addObject("outputType", search.get("outputType"));
        mv.addObject("template", "Role.xlsx");
        mv.addObject("filename", "그룹목록_" + CommonUtil.getToday());

        return mv;
    }

    @PostMapping("/role")
    public Result insert(String[] listMenuIds, String[] insertMenuIds, String[] exprMenuIds, @Valid RoleDto dto) {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        try {
            roleService.insert(userInfo.getUserId(), dto, listMenuIds, insertMenuIds, exprMenuIds);
        } catch (DuplicateKeyException dke) {
            throw new CodeMessageException(Result.ID_ALREADY_IN_USE);
        }

        return new Result(true, Result.OK);
    }

    @GetMapping("/role")
    public Result page(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable) {
        DataTablesResponse<RoleDto> page = roleService.page(search, pageable);

        if (((Number) search.get("draw")).intValue() > -1) {
            return new Result(true, Result.OK, page);
        } else {
            return new Result(true, Result.OK, page.getItems());
        }
    }

    @GetMapping("/role/{roleId}")
    public Result get(@PathVariable String roleId) {
        return new Result(true, Result.OK, roleService.get(roleId));
    }

    @PutMapping("/role/{roleId}")
    public Result update(String[] listMenuIds, String[] insertMenuIds, String[] exprMenuIds, @Valid RoleDto dto) {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        roleService.update(userInfo.getUserId(), dto, listMenuIds, insertMenuIds, exprMenuIds);

        return new Result(true, Result.OK);
    }

    @DeleteMapping("/role/{roleId}")
    public Result delete(@PathVariable String roleId) {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        roleService.delete(userInfo.getUserId(), roleId);

        return new Result(true, Result.OK);
    }

    @DeleteMapping("/role")
    public Result delete(String[] roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        roleService.delete(userInfo.getUserId(), roleIds);

        return new Result(true, Result.OK);
    }

}
