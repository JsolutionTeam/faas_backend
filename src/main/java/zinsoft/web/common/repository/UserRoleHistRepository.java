package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.UserRoleHist;

public interface UserRoleHistRepository extends JpaRepository<UserRoleHist, Long>, UserRoleHistQueryRepository {

}
