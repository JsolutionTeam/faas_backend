package zinsoft.web.common.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.common.dto.UserAccessLogDto;
import zinsoft.web.common.service.UserAccessLogService;

@RestController
@RequestMapping("${api.prefix}")
public class UserAccessLogController {

    @Resource
    UserAccessLogService userAccessLogService;

    @GetMapping("/user-access-log")
    public Result page(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable) throws Exception {
        DataTablesResponse<UserAccessLogDto> page = userAccessLogService.page(search, pageable);
        return new Result(true, Result.OK, page);
    }

}
