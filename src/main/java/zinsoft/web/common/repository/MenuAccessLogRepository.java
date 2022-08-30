package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.MenuAccessLog;

public interface MenuAccessLogRepository extends JpaRepository<MenuAccessLog, Long> {

}
