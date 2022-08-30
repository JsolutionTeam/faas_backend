package zinsoft.faas.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.SelfLaborCostDto;
import zinsoft.faas.vo.Page;
import zinsoft.faas.vo.PagingParam;
import zinsoft.util.DataTablesResponse;

public interface SelfLaborCostService {

    void insert(SelfLaborCostDto dto);

    SelfLaborCostDto get(Long selfLaborSeq);

    SelfLaborCostDto getByYear(String year);

    int checkValidYear(String year);

    int checkValidYear(Long selfLaborSeq, String year);

    List<SelfLaborCostDto> list(Map<String, Object> map);

    Page<SelfLaborCostDto> page(PagingParam pagingParam);

    DataTablesResponse<SelfLaborCostDto> page(Map<String, Object> search, Pageable pageable);

    void update(SelfLaborCostDto dto);

    void delete(Long selfLaborSeq);

    void delete(Long[] selfLaborSeq);

    List<Integer> yearList();

}