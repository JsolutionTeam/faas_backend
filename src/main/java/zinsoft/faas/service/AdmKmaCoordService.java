package zinsoft.faas.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.AdmKmaCoordMapper;
import zinsoft.faas.vo.AdmKmaCoord;

@Service
public class AdmKmaCoordService extends EgovAbstractServiceImpl {

    @Resource
    AdmKmaCoordMapper admKmaCoordMapper;

    public List<AdmKmaCoord> listCoord() {
        return admKmaCoordMapper.listCoord();
    }

    public AdmKmaCoord get(String admCd) {
        return admKmaCoordMapper.get(admCd);
    }

    public AdmKmaCoord getByUserId(String userId) {
        return admKmaCoordMapper.getByUserId(userId);
    }

}
