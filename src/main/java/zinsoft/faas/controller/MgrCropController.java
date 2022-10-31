package zinsoft.faas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zinsoft.faas.service.MgrCropDetailService;
import zinsoft.util.Result;

@RestController
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
public class MgrCropController {

    private final MgrCropDetailService mgrCropDetailService;

    /*
    * 작물코드, 작업 대분류 조회
    * 작물코드 : CROPS_CD
    * 작업 대분류 : BDM_CROP
    */
    @GetMapping("/mgr/crop/{codeId}")
    public Result getMgrCropDetailByCodeId(@PathVariable String codeId) {
        return new Result(true, Result.OK, mgrCropDetailService.getCropByCodeId(codeId));
    }
}
