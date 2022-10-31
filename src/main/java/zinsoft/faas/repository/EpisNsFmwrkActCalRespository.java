package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zinsoft.faas.dto.EpisNsFmwrkActCalResDto;
import zinsoft.faas.entity.EpisNsFmwrkActCal;

import java.util.List;

public interface EpisNsFmwrkActCalRespository extends JpaRepository<EpisNsFmwrkActCal, Long> {
    List<EpisNsFmwrkActCalResDto> findAllByFmwrkActCode(String actCode);

}
