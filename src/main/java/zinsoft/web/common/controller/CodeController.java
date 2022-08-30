package zinsoft.web.common.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zinsoft.util.Result;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.service.CodeService;

@RestController
@RequestMapping("${api.prefix}")
public class CodeController {

    @Resource
    CodeService codeService;

    @PostMapping("/code")
    public Result insert(@Valid CodeDto dto) {
        codeService.insert(dto);
        return new Result(true, Result.OK);
    }

    @GetMapping("/code/{codeId}")
    public Result list(@PathVariable String codeId) {
        return new Result(true, Result.OK, codeService.list(codeId));
    }

    @PutMapping("/code/{codeId}/{codeVal}")
    public Result update(@Valid CodeDto dto) {
        codeService.update(dto);
        return new Result(true, Result.OK);
    }

    @DeleteMapping("/code/{codeId}/{codeVal}")
    public Result delete(@Valid CodeDto dto) {
        codeService.delete(dto);
        return new Result(true, Result.OK);
    }

}
