package zinsoft.faas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zinsoft.faas.service.EpisNsFmwrkActCalService;
import zinsoft.util.Result;

@RestController
@RequestMapping("/test")
public class AATestController {

    @Autowired
    private EpisNsFmwrkActCalService actCalService;

    @GetMapping("/fmwrkcal")
    public Result getFmwrkcalTest(){
        return new Result(true, Result.OK, actCalService.getByActCode("80300"));
    }
}
