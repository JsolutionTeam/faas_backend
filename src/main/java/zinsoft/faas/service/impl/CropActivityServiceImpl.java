package zinsoft.faas.service.impl;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.CropActivityDto;
import zinsoft.faas.repository.CropActivityRepository;
import zinsoft.faas.service.CropActivityService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CropActivityServiceImpl extends EgovAbstractServiceImpl implements CropActivityService {

    @Resource
    CropActivityRepository cropActivityRepository;

    @Override
    public CropActivityDto get(Long cropActivitySeq) {
        return cropActivityRepository.get(cropActivitySeq);
    }

    @Override
    public List<CropActivityDto> listByActivityTCd(Long activityTCd) {
        // CropActivityQueryRepository에서 가져옴.
        // 검색용 List<CropActivityDto> listByActivityTCd(Long activityTCd)
        return cropActivityRepository.listByActivityTCd(activityTCd);
    }

    @Override
    public List<CropActivityDto> listByCropSeq(Long cropSeq) {
        return cropActivityRepository.listByCropSeq(cropSeq);
    }


}
