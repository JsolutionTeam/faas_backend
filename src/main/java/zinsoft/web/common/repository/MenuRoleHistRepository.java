package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.MenuRoleHist;

public interface MenuRoleHistRepository extends JpaRepository<MenuRoleHist, Long>, MenuRoleHistQueryRepository {

}
