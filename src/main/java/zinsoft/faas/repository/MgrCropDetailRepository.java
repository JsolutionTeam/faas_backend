package zinsoft.faas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zinsoft.faas.dto.MgrCropDetailResDto;
import zinsoft.faas.entity.MgrCropDetail;
import zinsoft.faas.entity.MgrCropDetailId;

import java.util.List;

public interface MgrCropDetailRepository extends JpaRepository<MgrCropDetail, MgrCropDetailId> {

    @Query(value = "select NEW zinsoft.faas.dto.MgrCropDetailResDto(id.code, codeNm) from MgrCropDetail where id.codeId = :codeId")
    List<MgrCropDetailResDto> getMgrCropDetailByCodeId(String codeId);

    @Query(value = "select crops from MgrCropDetail as crops where crops.id.code = :code")
    List<MgrCropDetail> findAllByCode(String code);
}
