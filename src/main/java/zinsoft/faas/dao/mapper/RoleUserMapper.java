package zinsoft.faas.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.vo.RoleUser;
import zinsoft.web.common.dto.UserInfoDto;

@Mapper
public interface RoleUserMapper {

    void insert(RoleUser vo);

    List<UserInfoDto> listByManagerId(@Param("roleId") String roleId, @Param("managerId") String managerId);

    void deleteByRoleId(String roleId);

    void deleteByManagerId(@Param("roleId") String roleId, @Param("managerId") String managerId);

}
