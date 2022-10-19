package zinsoft.faas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import zinsoft.faas.service.ConsultantService;
import zinsoft.util.Result;

@RestController
@RequestMapping("/api/v1")
public class ConsultantController {

    @Autowired
    private ConsultantService consultantService;

    @GetMapping("/consultant")
    public Result getMyConsult(){
        return new Result(true, Result.OK, (Object)consultantService.findMyConsult());
    }

}
