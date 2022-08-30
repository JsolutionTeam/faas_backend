package zinsoft.web.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, String>, MenuQueryRepository {

    List<Menu> findBySiteCdAndStatusCdOrderBySortOrderAscDepthAsc(String siteCd, String statusCd);

}
