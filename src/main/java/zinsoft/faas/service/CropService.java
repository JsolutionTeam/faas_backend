package zinsoft.faas.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.CropDto;
import zinsoft.faas.entity.EpisFsHervInfo;
import zinsoft.util.DataTablesResponse;

public interface CropService {

    void insert(CropDto vo);

    List<CropDto> list();

    List<CropDto> list(Map<String, Object> param);

    CropDto get(Long cropSeq);

    List<EpisFsHervInfo> getCropShapeList();

    boolean isExistCropId(Long cropSeq);

    DataTablesResponse<CropDto> page(Map<String, Object> search, Pageable pageable);

    void update(CropDto vo);

    void delete(Long[] cropSeqs);



}
