package zinsoft.faas.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.dto.UserManureStockDto;
import zinsoft.util.DataTablesResponse;

public interface UserManureStockService {

    void insertWith(UserManureStockDto dto);

    void insert(UserDiaryDto diaryDto);

    void insert(UserInoutDto inoutDto);

    UserManureStockDto get(UserManureStockDto dto);

    UserManureStockDto get(String userId, Long userManureStockSeq);

    List<UserManureStockDto> list(Map<String, Object> param);

    DataTablesResponse<UserManureStockDto> page(Map<String, Object> search, Pageable pageable);

    List<UserManureStockDto> listByUserDiarySeq(String userId, Long userDiarySeq);

    List<UserManureStockDto> listByUserInoutSeq(String userId, Long userInoutSeq);

    Long countUsed(String userId, Long userManureSeq);

    void updateWith(UserManureStockDto dto);

    void updateBy(UserDiaryDto diaryDto) throws IllegalStateException, IOException;

    void updateBy(UserInoutDto inoutDto) throws IllegalStateException, IOException;

    void delete(String userId, Long userManureStockSeq);

    void deleteWith(String userId, Long userManureStockSeq);

    void delete(String userId, Long[] userManureStockSeqs);

    void deleteByUserDiarySeq(String userId, Long[] userDiarySeqs);

    void deleteByUserDiarySeq(String userId, Long userDiarySeq);

    void deleteByUserInoutSeq(String userId, Long[] userInoutSeqs);

    void deleteByUserInoutSeq(String userId, Long userInoutSeq);

    void deleteByUserManureSeq(String userId, Long userManureSeq);

    void deleteByUserId(String userId);



}