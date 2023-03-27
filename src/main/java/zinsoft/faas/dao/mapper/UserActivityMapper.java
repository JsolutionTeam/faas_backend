package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.vo.UserActivity;

@Mapper
public interface UserActivityMapper {

    void insert(UserActivity vo);

    UserActivity get(UserActivity vo);

    List<UserActivity> list(Map<String, Object> param);

    void delete(UserActivity vo);

    void deleteByUserId(String userId);
}
