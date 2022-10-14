package zinsoft.faas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import zinsoft.faas.dto.HervInfoDto;
import zinsoft.faas.service.EpisFsHervInfoService;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/epis/fs/herv/info")
public class EpisFsHervInfoController {

    @Autowired
    private EpisFsHervInfoService hervInfoService;

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public List<HervInfoDto> getMyHervInfo(){
        return hervInfoService.findMyHervs();
    }
}
