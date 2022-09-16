package zinsoft.faas.service;

import zinsoft.faas.dto.CropActivityDto;

import java.util.List;

public interface CropActivityService {

    CropActivityDto get(Long cropActivitySeq);

    List<CropActivityDto> listByActivityTCd(Long activityTCd);

    List<CropActivityDto> listByCropSeq(Long cropSeq);
}
