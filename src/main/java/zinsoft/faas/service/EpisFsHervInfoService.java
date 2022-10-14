package zinsoft.faas.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.HervInfoDto;
import zinsoft.faas.repository.EpisFsHervInfoRepository;
import zinsoft.faas.repository.impl.WeatherQueryRepositoryImpl;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;

import java.util.List;

@Service
@Slf4j
public class EpisFsHervInfoService {

    @Autowired
    private EpisFsHervInfoRepository hervInfoRepository;


    public List<HervInfoDto> findMyHervs(){
        UserInfoDto farmerInfo = UserInfoUtil.getFarmerInfo();
        String farmCode = farmerInfo.getFarmCode();
        List<HervInfoDto> hervInfos = hervInfoRepository.findAllByFarmCode(farmCode);
        hervInfos.forEach(hv->{
            log.info("hv : {}", hv.toString());
        });
        return hervInfos;
    }
}
