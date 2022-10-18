package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zinsoft.faas.dto.HervInfoDto;
import zinsoft.faas.entity.EpisFsHervInfo;

import java.util.List;

public interface EpisFsHervInfoRepository extends JpaRepository<EpisFsHervInfo, Long>{

    List<HervInfoDto> findAllByFarmCode(String farmCode);
    List<String> findPestiKorNameByFarmCode(String farmCode);

}
