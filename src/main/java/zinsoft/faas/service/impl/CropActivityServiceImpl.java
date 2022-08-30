package zinsoft.faas.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.CropActivityMapper;
import zinsoft.faas.dto.CropActivityDto;
import zinsoft.faas.repository.CropActivityRepository;
import zinsoft.faas.service.CropActivityService;

@Service
public class CropActivityServiceImpl extends EgovAbstractServiceImpl implements CropActivityService{

    @Resource
    CropActivityMapper cropActivityMapper;

    @Resource
    CropActivityRepository cropActivityRepository;

    @Override
    public CropActivityDto get(Long cropActivitySeq) {
        return cropActivityRepository.get(cropActivitySeq);
    }

    @Override
    public List<CropActivityDto> listByActivityTCd(Long activityTCd) {
        return cropActivityRepository.listByActivityTCd(activityTCd);
    }

    @Override
    public List<CropActivityDto> listByCropSeq(Long cropSeq) {
        return cropActivityRepository.listByCropSeq(cropSeq);
    }

}
