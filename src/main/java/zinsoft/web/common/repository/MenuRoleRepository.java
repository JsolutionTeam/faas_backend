package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.MenuRole;

public interface MenuRoleRepository extends JpaRepository<MenuRole, MenuRole>, MenuRoleQueryRepository {

    // void deleteByRoleId(String roleId);

}
