package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String>, RoleQueryRepository {

    Role findByRoleIdAndStatusCd(String roleId, String statusCdNormal);

}
