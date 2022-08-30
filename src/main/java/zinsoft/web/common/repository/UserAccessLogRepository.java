package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.UserAccessLog;

public interface UserAccessLogRepository extends JpaRepository<UserAccessLog, Long>, UserAccessLogQueryRepository {

}
