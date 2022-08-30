package zinsoft.faas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.faas.entity.Cal;
import zinsoft.faas.entity.CalId;

public interface CalRepository extends JpaRepository<Cal, CalId> {

    List<Cal> findByCalDtBetween(String startDt, String endDt);

}
