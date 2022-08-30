package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.vo.UserInoutDetail;

@Mapper
public interface UserInoutDetailMapper {

    List<UserInoutDetail> list(Map<String, Object> param);

}
