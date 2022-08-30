package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRole>, UserRoleQueryRepository {

}
