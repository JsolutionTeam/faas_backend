package zinsoft.faas.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zinsoft.faas.service.EpisNsFmwrkActCalService;
import zinsoft.faas.service.EpisNsFmwrkWrkcdService;
import zinsoft.util.Result;

@RestController
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
public class EpisNsFmwrkController {

    private final EpisNsFmwrkWrkcdService wrkcdService;

    private final EpisNsFmwrkActCalService actCalService;

    @GetMapping("/fmwrk/wrkcd/{cropCd}/{growStep}")
    @ApiOperation(value = "작업 중분류 조회")
    public Result getWrkcdByCropCdAndGrowStep(@PathVariable String cropCd,@PathVariable String growStep) {
        return new Result(true, Result.OK, wrkcdService.getByCropCdAndGrowStep(cropCd, growStep));
    }

    @GetMapping("/fmwrk/cal/{cropCd}/{growStep}/{fmwrkCd}")
    @ApiOperation(value = "농작업활동(추천작업) 조회")
    public Result getFmwrkActCal(@PathVariable String cropCd, @PathVariable String growStep, @PathVariable String fmwrkCd){
        return new Result(true, Result.OK, actCalService.getFmwrkActCal(cropCd, growStep, fmwrkCd));
    }

}
