package zinsoft.web.common.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

import io.swagger.v3.oas.annotations.Operation;
import zinsoft.util.CommonUtil;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.UserInfoService;
import zinsoft.web.common.view.JxlsView;
import zinsoft.web.exception.CodeMessageException;

@RestController
@RequestMapping("${api.prefix}")
public class UserInfoController {

    private static final String FIELD_USER_ID = "userId";
    private static final String FIELD_USER_NM = "userNm";
    private static final String FIELD_MOBILE_NUM = "mobileNum";

    @Resource
    UserInfoService userInfoService;

    @GetMapping("/user.xlsx")
    public ModelAndView xlsx(@RequestParam Map<String, Object> search) {
        Context jxlsContext = new Context();
        ModelAndView mv = new ModelAndView(new JxlsView(jxlsContext));
        List<UserInfoDto> list = userInfoService.list(search);

        jxlsContext.putVar("search", search);
        jxlsContext.putVar("list", list);

        mv.addObject("outputType", search.get("outputType"));
        mv.addObject("template", "UserInfo.xlsx");
        mv.addObject("filename", "????????????_" + CommonUtil.getToday());

        return mv;
    }

    @Operation(summary = "????????? ??????")
    @PostMapping("/user")
    public Result insert(@Valid UserInfoDto dto, HttpServletRequest request) {
        dto.setStatusCd(UserInfoDto.STATUS_CD_NORMAL);

        try {
            userInfoService.insert(dto.getUserId(), dto, CommonUtil.getFrontEndBaseUri(request));
        } catch (DuplicateKeyException dke) {
            throw new CodeMessageException(Result.ID_ALREADY_IN_USE);
        }

        return new Result(true, Result.OK);
    }

    @Operation(summary = "????????? ?????? ??????")
    @GetMapping("/user")
    public Result page(@RequestParam Map<String, Object> search, String[] roleIds, @PageableDefault Pageable pageable) {
        if (roleIds != null && roleIds.length > 0) {
            search.put("roleIds", roleIds);
        }

        DataTablesResponse<UserInfoDto> page = userInfoService.page(search, pageable);
        return new Result(true, Result.OK, page);
    }

    @Operation(summary = "????????? ?????? ??????", description = "")
    @PostMapping("/user/item")
    public Result get(@PathVariable(required = false) String userId) {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        if (userId == null) {
            userId = userInfo.getUserId();
        }

        if (!UserInfoUtil.isPermitUserResource(userId)) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        UserInfoDto dto = userInfoService.get(userId);

        return new Result(true, Result.OK, dto);
    }

    @Operation(summary = "?????? ????????? ??????/????????? ??????")
    @PostMapping("/user-id")
    public Result findId(@RequestParam Map<String, Object> search) {
        String userId = (String) search.get(FIELD_USER_ID);

        if (userId != null && !userId.isEmpty()) {
            // ??????????????? ??????
            UserInfoDto dto = userInfoService.get(userId);
            return dto == null ? new Result(true, Result.ID_AVAILABLE) : new Result(false, Result.ID_ALREADY_IN_USE);
        }

        if (!CommonUtil.isValidMapItems(search, new String[] { FIELD_USER_NM, FIELD_MOBILE_NUM })) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        List<String> list = userInfoService.listUserId(search);

        if (list != null && list.size() > 0) {
            return new Result(true, Result.OK, list);
        } else {
            return new Result(true, Result.NO_USER);
        }
    }

    @Operation(summary = "???????????? ??????")
    @GetMapping("/user-pwd")
    public Result findPwd(@RequestParam Map<String, Object> search, HttpServletRequest request) {
        if (!CommonUtil.isValidMapItems(search, new String[] { FIELD_USER_ID, FIELD_USER_NM, FIELD_MOBILE_NUM })) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        boolean done = userInfoService.findPwd((String) search.get(FIELD_USER_ID), (String) search.get(FIELD_USER_NM), (String) search.get(FIELD_MOBILE_NUM), CommonUtil.getBaseUri(request));

        if (done) {
            return new Result(true, Result.OK);
        } else {
            return new Result(true, Result.NO_USER);
        }
    }

    @Operation(summary = "????????? ?????? ??????")
    @PutMapping("/user/item")
    public Result update(@Valid UserInfoDto dto, HttpServletRequest request) throws CodeMessageException {
        String reqUserId = dto.getUserId();

        if (!UserInfoUtil.isPermitUserResource(reqUserId)) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        if (UserInfoUtil.isManager(request)) {
            dto.setCurUserPwd(null); // ???????????? ?????? ????????? ?????? ???????????? ?????? ??????
        } else {
            dto.setUserId(userInfo.getUserId());
            dto.setUserRoleList(null); // ?????????????????? role ?????? ??????

            if (dto.getUserPwd() != null && !dto.getUserPwd().isEmpty() && dto.getCurUserPwd() == null) {
                dto.setCurUserPwd(""); // ?????? ????????? null??? ?????? ?????? ??????????????? ??????????????? ???
            }
        }

        userInfoService.update(userInfo.getUserId(), dto);
        dto = userInfoService.get(reqUserId);

        return new Result(true, Result.OK, dto);
    }

    @Operation(summary = "???????????? ?????????")
    @PutMapping("/user-reset-pwd/item")
    public Result resetPwd(String userId) {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        String tempPwd = userInfoService.resetPwd(userInfo.getUserId(), userId);

        return new Result(true, Result.OK, (Object) tempPwd);
    }

    @Operation(summary = "????????? ??????/??????")
    @DeleteMapping("/user/item")
    public Result delete(String userId, HttpServletRequest request) {
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        if (!UserInfoUtil.isManager(request)) {
            userId = userInfo.getUserId();
        }

        if (!UserInfoUtil.isPermitUserResource(userId)) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        userInfoService.delete(userInfo.getUserId(), userId);

        return new Result(true, Result.OK);
    }

    @Operation(summary = "?????? ????????? ??????")
    @DeleteMapping("/user")
    public Result delete(String[] userIds) {
        if (userIds == null || userIds.length == 0) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        userInfoService.delete(userInfo.getUserId(), userIds);

        return new Result(true, Result.OK);
    }

}
