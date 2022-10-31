package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zinsoft.faas.dto.ConsultantResDto;
import zinsoft.faas.entity.Consultant;

import java.util.List;

public interface ConsultantRepository extends JpaRepository<Consultant, Long> {

}
