package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zinsoft.faas.dto.EpisNsFmwrkWrkcdResDto;
import zinsoft.faas.entity.EpisNsFmwrkWrkcd;

import java.util.List;

public interface EpisNsFmwrkWrkcdRepository extends JpaRepository<EpisNsFmwrkWrkcd, String> {
    @Query(value = "select NEW zinsoft.faas.dto.EpisNsFmwrkWrkcdResDto(fmwrkCd, fmwrkNm) from EpisNsFmwrkWrkcd where cropCd = :cropCd and growStep = :growStep")
    List<EpisNsFmwrkWrkcdResDto> getFmwrkWrkcdByCropCdAndGrowStep(String cropCd, String growStep);

}
