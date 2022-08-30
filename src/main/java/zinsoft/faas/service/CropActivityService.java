package zinsoft.faas.service;

import java.util.List;

import zinsoft.faas.dto.CropActivityDto;

public interface CropActivityService {

    CropActivityDto get(Long cropActivitySeq);

    List<CropActivityDto> listByActivityTCd(Long activityTCd);

    List<CropActivityDto> listByCropSeq(Long cropSeq);


}
