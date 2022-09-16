package zinsoft.faas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.CropSpeciesDto;
import zinsoft.faas.repository.CropSpeciesRepository;

import java.util.List;

@Service
public class CropSpeciesService {
    @Autowired
    private CropSpeciesRepository cropSpeciesRepository;

    public List<CropSpeciesDto> getAllByCropBCd(String cropBCd){
        return cropSpeciesRepository.findByCrop_CropBCd(cropBCd);
//        return null;
    }
}
