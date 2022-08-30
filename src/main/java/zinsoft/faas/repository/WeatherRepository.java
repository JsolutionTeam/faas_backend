package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.faas.entity.Weather;

public interface WeatherRepository extends JpaRepository<Weather, Long>, WeatherQueryRepository {


}
