package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zinsoft.faas.entity.EpisFsHervInfo;

import java.util.List;

public interface EpisFsHervInfoRepository extends JpaRepository<EpisFsHervInfo, Long> {

    @Query(value = "select herv from EpisFsHervInfo herv where herv.farmCode = :farmCode")
    List<EpisFsHervInfo> findAllByFarmCode( String farmCode);


}
