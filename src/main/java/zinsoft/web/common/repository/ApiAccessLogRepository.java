package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.ApiAccessLog;

public interface ApiAccessLogRepository extends JpaRepository<ApiAccessLog, Long> {

}
