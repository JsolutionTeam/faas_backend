package zinsoft.faas.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import zinsoft.faas.dto.SmarfarmCropResDto;
import zinsoft.faas.dto.WeatherDto;
import zinsoft.faas.service.SmartfarmCropService;

import java.util.List;

@RestController
@RequestMapping("/smartfarm/crop")
@Slf4j
public class SmartfarmCropController {

    @Autowired
    private SmartfarmCropService smartfarmCropService;


    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public Object getCropTest(){
        return smartfarmCropService.getCropCodeList();
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<SmarfarmCropResDto> getCropList(){
        return smartfarmCropService.getCropCodeList();
    }
}