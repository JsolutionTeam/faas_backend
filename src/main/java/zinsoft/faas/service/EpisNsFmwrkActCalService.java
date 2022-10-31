package zinsoft.faas.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.EpisNsFmwrkActCalResDto;
import zinsoft.faas.repository.EpisNsFmwrkActCalQueryRespository;
import zinsoft.faas.repository.EpisNsFmwrkActCalRespository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EpisNsFmwrkActCalService {


    private final EpisNsFmwrkActCalQueryRespository queryRespository;

    public EpisNsFmwrkActCalResDto getFmwrkActCal(String cropCd, String growStep, String fmwrkCd){
        return queryRespository.getFmwrkActCal(cropCd, growStep, fmwrkCd);
    }
}
