package zinsoft.faas.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.UserInoutDetailMapper;
import zinsoft.faas.vo.UserInoutDetail;

@Service
public class UserInoutDetailService extends EgovAbstractServiceImpl {

    @Resource
    UserInoutDetailMapper userInoutDetailMapper;

    public List<UserInoutDetail> list(String userId, Long cropSeq, String inoutCd, String detail) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("cropSeq", cropSeq);
        param.put("inoutCd", inoutCd);
        param.put("detail", detail);

        return userInoutDetailMapper.list(param);
    }

}
