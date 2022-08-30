package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.AppProperties;

public interface AppPropertiesRepository extends JpaRepository<AppProperties, String> {

}
