package zinsoft.faas.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.SelfLaborCostDto;
import zinsoft.faas.vo.PagingParam;

@Mapper
public interface SelfLaborCostMapper {

    void insert(SelfLaborCostDto vo);

    SelfLaborCostDto get(Long selfLaborSeq);

    SelfLaborCostDto getByYear(@Param("year") String year);

    int checkValidYear(Long year);

    List<SelfLaborCostDto> page(PagingParam pagingParam);

    int count(PagingParam pagingParam);

    void update(SelfLaborCostDto vo);

    void delete(SelfLaborCostDto vo);

}
