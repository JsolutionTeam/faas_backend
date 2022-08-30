package zinsoft.faas.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserEmpCostDto;
import zinsoft.util.DataTablesResponse;

public interface UserEmpCostService {

    void insert(UserEmpCostDto dto);

    UserEmpCostDto getByUserId(String userId);

    int checkValidYear(String year, String userId);

    int checkValidYear(Long userEmpCostSeq, String year, String userId);

    UserEmpCostDto get(String userId, Long userEmpCostSeq);

    UserEmpCostDto getByYear(String userId, String year);

    DataTablesResponse<UserEmpCostDto> page(Map<String, Object> search, Pageable pageable);

    List<UserEmpCostDto> list(Map<String, Object> param);

    void update(UserEmpCostDto dto);

    void deleteByUserId(String userId);

    void delete(String userId, Long userEmpCostSeq);

    List<Integer> yearList(String userId);

}