package zinsoft.faas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.EpisNsFmwrkWrkcdResDto;
import zinsoft.faas.repository.EpisNsFmwrkWrkcdRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisNsFmwrkWrkcdService {

    private final EpisNsFmwrkWrkcdRepository repository;

    public List<EpisNsFmwrkWrkcdResDto> getByCropCdAndGrowStep(String cropCd, String growStep) {
        return repository.getFmwrkWrkcdByCropCdAndGrowStep(cropCd, growStep);
    }

    public Boolean isExistByCode(String fmwrkCd) {
        return repository.findAllById(Collections.singleton(fmwrkCd)).size() > 0;
    }
}
