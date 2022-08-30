package zinsoft.faas.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.vo.AdmZipcode;

@Mapper
public interface AdmZipcodeMapper {

    List<AdmZipcode> list(@Param("upAdmCd") String upAdmCd);

}
