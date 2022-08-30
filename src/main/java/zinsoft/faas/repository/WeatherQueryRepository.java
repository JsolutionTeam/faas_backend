package zinsoft.faas.repository;

import zinsoft.faas.dto.WeatherDto;

public interface WeatherQueryRepository {

    WeatherDto get(String baseDate, String stnCode);

}
