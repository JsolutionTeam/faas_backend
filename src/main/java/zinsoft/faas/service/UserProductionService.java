package zinsoft.faas.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserProductionDto;
import zinsoft.util.DataTablesResponse;

public interface UserProductionService {

    void insert(UserProductionDto dto);

    void insert(UserDiaryDto dto);

    UserProductionDto get(Long userProductionSeq, String userId);

    DataTablesResponse<UserProductionDto> page(Map<String, Object> search, Pageable pageable);

    void update(UserProductionDto dto);

    void updateBy(UserDiaryDto diaryDto);

    int delete(String userId, Long userProductionSeq);

    void delete(String userId, Long[] userProductionSeq);

    void deleteByUserId(String userId);

    void deleteByUserDiarySeq(String userId, Long userDiarySeq);

    void deleteByUserDiarySeq(String userId, Long[] userDiarySeqs);

    List<Map<String, Object>> chartByPlanTCd(Map<String, Object> map);

}