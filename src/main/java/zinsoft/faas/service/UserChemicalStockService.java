package zinsoft.faas.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserChemicalStockDto;
import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.util.DataTablesResponse;

public interface UserChemicalStockService {

    void insertWith(UserChemicalStockDto dto);

    void insert(UserDiaryDto diaryDto);

    void insert(UserInoutDto inoutDto);

    UserChemicalStockDto get(UserChemicalStockDto dto);

    UserChemicalStockDto get(String userId, Long userChemicalStockSeq);

    List<UserChemicalStockDto> list(Map<String, Object> param);

    //Page<UserChemicalStockDto> page(PagingParam pagingParam);

    DataTablesResponse<UserChemicalStockDto> page(Map<String, Object> search, Pageable pageable);

    List<UserChemicalStockDto> listByUserDiarySeq(String userId, Long userDiarySeq);

    List<UserChemicalStockDto> listByUserInoutSeq(String userId, Long userInoutSeq);

    Long countUsed(String userId, Long userChemicalSeq);

    void updateWith(UserChemicalStockDto dto);

    void updateBy(UserDiaryDto diaryDto) throws IllegalStateException, IOException;

    void updateBy(UserInoutDto inoutDto) throws IllegalStateException, IOException;

    void delete(String userId, Long userChemicalStockSeq);

    void deleteWith(String userId, Long userChemicalStockSeq);

    void delete(String userId, Long[] userChemicalStockSeqs);

    void deleteByUserDiarySeq(String userId, Long[] userDiarySeqs);

    void deleteByUserDiarySeq(String userId, Long userDiarySeq);

    void deleteByUserInoutSeq(String userId, Long[] userInoutSeqs);

    void deleteByUserInoutSeq(String userId, Long userInoutSeq);

    void deleteByUserChemicalSeq(String userId, Long userChemicalSeq);

    void deleteByUserId(String userId);









}