package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.UserManureDto;
import zinsoft.faas.vo.PagingParam;

@Mapper
public interface UserManureMapper {

    void insert(UserManureDto vo);

    UserManureDto get(UserManureDto vo);

    List<UserManureDto> list(Map<String, Object> param);

    List<UserManureDto> page(PagingParam pagingParam);

    int count(PagingParam pagingParam);

    void update(UserManureDto vo);

    void delete(UserManureDto vo);

    void deleteByUserId(String userId);

}
