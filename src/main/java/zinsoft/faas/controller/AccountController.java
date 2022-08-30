package zinsoft.faas.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import zinsoft.faas.dto.AccountDto;
import zinsoft.faas.service.AccountService;
import zinsoft.faas.view.AccountListExcelView;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;

@Controller
@RequestMapping("${api.prefix}/account")
public class AccountController {

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @Resource
    AccountService accountService;

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public ModelAndView excel(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable) throws Exception {

        DataTablesResponse<AccountDto> page = accountService.page(search, pageable);
        ModelAndView mv = new ModelAndView(new AccountListExcelView());

        mv.addObject("sheetName", "계정과목관리");
        mv.addObject("cond", search);
        mv.addObject("list", page.getItems());

        return mv;
    }

    @PostMapping(value = "")
    @ResponseBody
    public Result insert(@Valid AccountDto dto, HttpServletResponse response) throws Exception {

        String existAcId = accountService.getAcIdByAcNm(dto.getAcNm());
        if(existAcId != null && Integer.parseInt(existAcId) > 0) {
            return new Result(false, "9011");
        }

        accountService.insert(dto);

        return new Result(true, Result.OK);
    }

    @GetMapping(value = "/{acId}")
    @ResponseBody
    public Result get(@PathVariable("acId") String acId) throws Exception {
        return new Result(true, Result.OK, accountService.get(acId));
    }

    @GetMapping(value = "")
    @ResponseBody
    public Result page(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable) throws Exception {

        if (search.get(pageSizeParameter) != null) {
            DataTablesResponse<AccountDto> page = accountService.page(search, pageable);
            return new Result(true, Result.OK, page);
        } else {
            List<AccountDto> list = accountService.list(search);
            return new Result(true, Result.OK, list);
        }
    }

    @GetMapping(value = "/acId")
    @ResponseBody
    public Result listByAcId(String acId) throws Exception {
        List<AccountDto> list = null;
        if(StringUtils.isBlank(acId)) {
            list = accountService.getRootAcIdList();
        }else {
            list = accountService.getlvl2AcIdList(acId);
        }

        return new Result(true, Result.OK, list);
    }

    @PutMapping(value = "/{acId}")
    @ResponseBody
    public Result update(@PathVariable("acId") String acId, @Valid AccountDto dto) throws Exception {
        String existAcId = accountService.getAcIdByAcNm(dto.getAcNm());
        if(existAcId != null && Integer.parseInt(existAcId) > 0 && dto.getAcId().equals(existAcId) == false) {
            return new Result(false, "9011");
        }

        accountService.update(dto);
        return new Result(true, Result.OK);
    }

    @DeleteMapping(value = "/{acId}")
    @ResponseBody
    public Result delete(@PathVariable("acId") String acId) throws Exception {
        accountService.delete(acId);
        return new Result(true, Result.OK);
    }

}
