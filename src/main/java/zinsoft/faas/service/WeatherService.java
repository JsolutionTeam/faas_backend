package zinsoft.faas.service;

import zinsoft.faas.dto.WeatherDto;

public interface WeatherService {

    WeatherDto get(String userId, String baseDate);

}