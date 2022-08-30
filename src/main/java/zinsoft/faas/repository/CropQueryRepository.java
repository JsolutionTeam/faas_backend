package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.CropDto;

public interface CropQueryRepository {

    CropDto get(Long cropSeq);

    List<CropDto> list(Map<String, Object> search);

    Page<CropDto> page(Map<String, Object> search, Pageable pageable);

}