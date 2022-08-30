package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.MenuApi;

public interface MenuApiRepository extends JpaRepository<MenuApi, String>, MenuApiQueryRepository {

}
