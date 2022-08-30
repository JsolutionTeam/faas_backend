package zinsoft.faas.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import zinsoft.faas.service.WeatherService;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;

@Controller
@RequestMapping("${api.prefix}/weather")
public class WeatherController {

    @Resource
    WeatherService weatherService;

  @RequestMapping(value = "/{baseDate}", method = RequestMethod.GET)
  @ResponseBody
  public Result get(@PathVariable("baseDate") String baseDate) throws Exception {
      UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
      Result result = new Result(true, Result.OK);

      result.setBody(weatherService.get(farmerInfo.getUserId(), baseDate));

      return result;
  }

}
