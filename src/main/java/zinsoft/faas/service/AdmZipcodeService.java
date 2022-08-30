package zinsoft.faas.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.AdmZipcodeMapper;
import zinsoft.faas.vo.AdmZipcode;

@Service
public class AdmZipcodeService extends EgovAbstractServiceImpl {

    @Resource
    AdmZipcodeMapper admZipcodeMapper;

    public List<AdmZipcode> list(String upAdmCd) {
        return admZipcodeMapper.list(upAdmCd);
    }

}
