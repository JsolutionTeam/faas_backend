package zinsoft.faas.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zinsoft.faas.service.AccountDetailService;
import zinsoft.util.Result;

@RestController
@RequestMapping("${api.prefix}")
public class AccountDetailController {

    @Resource
    AccountDetailService AccountDetailService;

    @GetMapping("/account-detail/{acId}")
    public Result list(@PathVariable String acId) {
        return new Result(true, Result.OK, AccountDetailService.listByAcId(acId));
    }

}
