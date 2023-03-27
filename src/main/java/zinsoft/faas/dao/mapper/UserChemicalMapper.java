package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.UserChemicalDto;
import zinsoft.faas.vo.PagingParam;

@Mapper
public interface UserChemicalMapper {

    List<UserChemicalDto> page(PagingParam pagingParam);

    int count(PagingParam pagingParam);

    void delete(UserChemicalDto vo);
}
