package zinsoft.faas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.CropSpeciesDto;
import zinsoft.faas.entity.CropSpecies;
import zinsoft.faas.repository.CropSpeciesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CropSpeciesService {
    @Autowired
    private CropSpeciesRepository cropSpeciesRepository;

    public CropSpecies getEntity(Long cropSpeciesSeq){
        return cropSpeciesRepository.findById(cropSpeciesSeq).orElse(null);
    }

    public List<CropSpeciesDto> getAllByCropBCd(String cropBCd){
        return cropSpeciesRepository.findByCrop_CropBCd(cropBCd);
//        return null;
    }
}
