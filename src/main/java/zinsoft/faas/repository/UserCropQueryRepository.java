package zinsoft.faas.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserCropDto;

public interface UserCropQueryRepository {

    /*UserCropDto get(UserCropDto dto);

    UserCropDto get(Map<String, Object> search);*/

    UserCropDto get(UserCropDto dto);

    Page<UserCropDto> page(Map<String, Object> search, Pageable pageable);

    List<UserCropDto> list(Map<String, Object> search);

    long countByAliasNm(UserCropDto dto);

}