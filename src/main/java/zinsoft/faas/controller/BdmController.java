package zinsoft.faas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zinsoft.faas.service.EpisNsFmwrkActCalService;
import zinsoft.util.Result;

@RestController
@RequestMapping("/api/bdm")
public class BdmController {

    @Autowired
    private EpisNsFmwrkActCalService actCalService;

    @GetMapping("/bdm/fmwrk/act/cal/{actCode}")
    public Result getFmwrkActCalByActCode(@PathVariable String actCode){
        return new Result(true, Result.OK, actCalService.getByActCode(actCode));
    }
}
