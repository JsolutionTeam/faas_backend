package zinsoft.faas.service.impl;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.WeatherDto;
import zinsoft.faas.repository.WeatherRepository;
import zinsoft.faas.service.WeatherService;
import zinsoft.util.Result;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.UserInfoService;
import zinsoft.web.exception.CodeMessageException;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WeatherServiceImpl extends EgovAbstractServiceImpl implements WeatherService {

    @Resource
    WeatherRepository weatherRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserInfoService userInfoService;

    @Override
    public WeatherDto get(String userId, String baseDate) {
        SimpleDateFormat from = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd");
        Date baseDt = null;
        WeatherDto weatherDto;
        UserInfoDto userInfoDto = userInfoService.get(userId);

        if (baseDate == null) {
            new CodeMessageException(Result.BAD_REQUEST);
        }

        baseDate = baseDate.replaceAll("[^0-9]", "");
        if (baseDate.length() != 8) {
            new CodeMessageException(Result.BAD_REQUEST);
        }

        try {
            baseDt = from.parse(baseDate);
        } catch (ParseException e) {
            new CodeMessageException(Result.BAD_REQUEST, "조회날짜 형식 오류");
        }

        baseDate = to.format(baseDt);

        weatherDto = weatherRepository.get(baseDate, null);
        return weatherDto;
    }

}
