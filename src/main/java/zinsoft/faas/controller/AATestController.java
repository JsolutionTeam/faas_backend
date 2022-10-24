package zinsoft.faas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zinsoft.faas.service.EpisNsFmwrkActCalService;
import zinsoft.util.Result;

@RestController
@RequestMapping("/test")
public class AATestController {

    @Autowired
    private EpisNsFmwrkActCalService actCalService;

    @GetMapping("/fmwrkcal/{actcd}")
    public Result getFmwrkcalTest(@PathVariable String actcd){
        return new Result(true, Result.OK, actCalService.getByActCode(actcd));
    }
}
