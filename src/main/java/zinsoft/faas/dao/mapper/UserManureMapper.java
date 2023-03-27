package zinsoft.faas.dao.mapper;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.UserManureDto;

@Mapper
public interface UserManureMapper {
    void delete(UserManureDto vo);
}
