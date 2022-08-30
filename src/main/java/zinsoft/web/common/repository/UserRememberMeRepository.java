package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.UserRememberMe;
import zinsoft.web.common.entity.UserRememberMeId;

public interface UserRememberMeRepository extends JpaRepository<UserRememberMe, UserRememberMeId>, UserRememberMeQueryRepository {

    UserRememberMe findBySeries(String series);

}
