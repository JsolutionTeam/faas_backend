package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zinsoft.faas.dto.MgrCropDetailResDto;
import zinsoft.faas.entity.MgrCropDetail;
import zinsoft.faas.entity.MgrCropDetailId;
import zinsoft.faas.entity.MgrCropType;

import java.util.List;

public interface MgrCropTypeRepository extends JpaRepository<MgrCropType, String> {

    @Query(value = "select codeName from MgrCropType where codeId = :codeId")
    List<String> findAllByCodeId(String codeId);
}
