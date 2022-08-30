package zinsoft.web.common.dao.mapper;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.web.common.dto.CodeDto;

@Mapper
public interface CodeMapper {

    void insert(CodeDto dto);

}
