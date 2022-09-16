package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zinsoft.faas.dto.CropSpeciesDto;
import zinsoft.faas.entity.Crop;
import zinsoft.faas.entity.CropSpecies;

import java.util.List;

public interface CropSpeciesRepository extends JpaRepository<CropSpecies, Long> {
    List<CropSpeciesDto> findByCrop_CropBCd(String cropBCd);
//    List<CropSpeciesDto> findByCrop(Crop crop);
}
