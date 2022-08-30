package zinsoft.faas.repository;

import java.util.List;

import zinsoft.faas.dto.CropActivityDto;

public interface CropActivityQueryRepository {

    CropActivityDto get(Long cropActivitySeq);

    List<CropActivityDto> listByActivityTCd(Long activityTCd);

    List<CropActivityDto> listByCropSeq(Long cropSeq);

}
