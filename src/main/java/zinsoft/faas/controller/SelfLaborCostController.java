package zinsoft.faas.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

import zinsoft.faas.dto.SelfLaborCostDto;
import zinsoft.faas.service.SelfLaborCostService;
import zinsoft.faas.view.SelfLaborCostExcelView;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.exception.CodeMessageException;

@Controller
@RequestMapping("${api.prefix}/self-labor-cost")
public class SelfLaborCostController {

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @Resource
    SelfLaborCostService selfLaborCostService;

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public ModelAndView excel(@RequestParam Map<String, Object> search) throws Exception {
        List<SelfLaborCostDto> list = selfLaborCostService.list(search);
        ModelAndView mv = new ModelAndView(new SelfLaborCostExcelView());

        mv.addObject("sheetName", "자가노력비관리");
        mv.addObject("cond", search);
        mv.addObject("list", list);

        return mv;
    }

    @GetMapping(value = "/{selfLaborSeq}")
    @ResponseBody
    public SelfLaborCostDto get(@PathVariable("selfLaborSeq") Long selfLaborSeq) throws Exception {

        return selfLaborCostService.get(selfLaborSeq);
    }

    @PostMapping(value = "")
    @ResponseBody
    public Result insert(@Valid SelfLaborCostDto dto, HttpServletResponse response) throws Exception {

        int yearCnt = selfLaborCostService.checkValidYear(dto.getYear());
        if(yearCnt > 0) {
            throw new CodeMessageException(Result.ALREADY_COST);
        }

        selfLaborCostService.insert(dto);

        return new Result(true, Result.OK);
    }

    @GetMapping(value = "")
    @ResponseBody
    public Result page(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable, HttpSession session) throws Exception {
        DataTablesResponse<SelfLaborCostDto> page = selfLaborCostService.page(search, pageable);

        if (search.get(pageSizeParameter) != null) {
            return new Result(true, Result.OK, page);
        } else {
            return new Result(true, Result.OK, page.getItems());
        }
    }

    @PutMapping(value = "/{selfLaborSeq}")
    @ResponseBody
    public Result put( @Valid SelfLaborCostDto dto) throws Exception {
        int yearCnt = selfLaborCostService.checkValidYear(dto.getSelfLaborSeq(), dto.getYear());
        if(yearCnt > 0) {
            throw new CodeMessageException(Result.ALREADY_COST);
        }

        selfLaborCostService.update(dto);

        return new Result(true, "0000");
    }

    @DeleteMapping(value = "/{selfLaborSeq}")
    @ResponseBody
    public Result delete(@PathVariable("selfLaborSeq")Long selfLaborSeq) throws Exception {

        selfLaborCostService.delete(selfLaborSeq);

        return new Result(true, "0000");
    }

    @GetMapping(value = "/year")
    @ResponseBody
    public List<Integer> year() throws Exception {

        return selfLaborCostService.yearList();
    }
}
