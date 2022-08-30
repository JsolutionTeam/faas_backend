package zinsoft.faas.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserProductionDto;
import zinsoft.util.DataTablesResponse;

public interface UserDiaryService {

    void insert(UserDiaryDto dto) throws IllegalStateException, IOException;

    void insert(String actStDt, String actEdDt, UserDiaryDto dto) throws IllegalStateException, IOException;

    void insert(UserProductionDto dto);

    void copy(String userId, String srcYear, String dstYear);

    UserDiaryDto get(String userId, Long userDiarySeq);

    DataTablesResponse<UserDiaryDto> page(Map<String, Object> search, Pageable pageable);

    List<String> listYear(String userId, String diaryTCd, String year);

    int countUsedCrop(String userId, Long userCropSeq);

    void update(UserDiaryDto dto) throws IllegalStateException, IOException;

    void update(UserProductionDto dto);

    void delete(String userId, Long userDiarySeq);

    void delete(UserDiaryDto dto);

    void delete(String userId, Long[] userDiarySeqs);

    void deleteByUserId(String userId);

    List<Map<String, Object>> countByActSeq(String userId, String actDt);

}