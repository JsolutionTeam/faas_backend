package zinsoft.faas.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.EpisNsFmwrkActCalResDto;
import zinsoft.faas.repository.EpisnsFmwrkActCalRespository;

import java.util.List;

@Service
@Slf4j
public class EpisNsFmwrkActCalService {

    @Autowired
    private EpisnsFmwrkActCalRespository fmwrkActCalRespository;


    public List<EpisNsFmwrkActCalResDto> getByActCode(String actCode){
        log.info("get by act code - act code : {}", actCode);
        return fmwrkActCalRespository.findAllByFmwrkActCode(actCode);
    }

}
