package zinsoft.faas.dao.mapper;

import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.vo.AdmKmaCoord;

@Mapper
public interface AdmKmaCoordMapper {

    List<AdmKmaCoord> listCoord();

    AdmKmaCoord get(String admCd);

    AdmKmaCoord getByUserId(String userId);

}
