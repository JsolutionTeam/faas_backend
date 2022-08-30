package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.UserChemicalDto;
import zinsoft.faas.vo.PagingParam;

@Mapper
public interface UserChemicalMapper {

    void insert(UserChemicalDto vo);

    UserChemicalDto get(UserChemicalDto vo);

    List<UserChemicalDto> list(Map<String, Object> param);

    List<UserChemicalDto> page(PagingParam pagingParam);

    int count(PagingParam pagingParam);

    void update(UserChemicalDto vo);

    void delete(UserChemicalDto vo);

    void deleteByUserId(String userId);

}
