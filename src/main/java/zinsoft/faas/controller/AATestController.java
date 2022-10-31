package zinsoft.faas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zinsoft.faas.repository.EpisNsFmwrkActCalQueryRespository;
import zinsoft.faas.service.EpisNsFmwrkActCalService;
import zinsoft.faas.service.EpisNsFmwrkWrkcdService;
import zinsoft.util.Result;
import zinsoft.faas.repository.MgrCropDetailRepository;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class AATestController {

    private final EpisNsFmwrkActCalService actCalService;

    private final EpisNsFmwrkWrkcdService wrkcdService;

    private final MgrCropDetailRepository repository;

    @GetMapping("/fmwrk/cal/{cropCd}/{growStep}/{fmwrkCd}")
    public Result getFmwrkActCal(@PathVariable String cropCd, @PathVariable String growStep, @PathVariable String fmwrkCd){
        return new Result(true, Result.OK, actCalService.getFmwrkActCal(cropCd, growStep, fmwrkCd));
    }

    @GetMapping("/fmwrk/wrkcd/{cropCd}/{growStep}")
    public Result getWrkcdByCropCdAndGrowStep(@PathVariable String cropCd,@PathVariable String growStep) {
        return new Result(true, Result.OK, wrkcdService.getByCropCdAndGrowStep(cropCd, growStep));
    }

    @GetMapping("/repo/{value}")
    public Result getRepoTest(@PathVariable String value){
        return new Result(true, Result.OK, repository.getMgrCropDetailByCodeId(value));
    }
}
