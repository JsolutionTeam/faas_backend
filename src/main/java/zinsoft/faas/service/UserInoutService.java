package zinsoft.faas.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserChemicalStockDto;
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.dto.UserManureStockDto;
import zinsoft.faas.dto.UserShipDto;
import zinsoft.util.DataTablesResponse;

public interface UserInoutService {

    void insert(UserInoutDto dto) throws IllegalStateException, IOException;

    void insert(UserChemicalStockDto dto);

    void insert(UserManureStockDto dto);

    void insert(UserShipDto dto);

    UserInoutDto get(String userId, Long userDiarySeq);

    DataTablesResponse<UserInoutDto> page(Map<String, Object> search, Pageable pageable);

    void update(UserInoutDto dto) throws IllegalStateException, IOException;

    void update(UserChemicalStockDto dto);

    void update(UserManureStockDto dto);

    void update(UserShipDto dto);

    void updateReversing(Long userInoutSeq, UserInoutDto rDto);

    void delete(String userId, Long userInoutSeq);

    void delete(UserInoutDto dto);

    void delete(String userId, Long[] userInoutSeqs);

    void deleteReversing(String userId, Long userInoutSeq);

    void deleteByUserId(String userId);

    void deleteBy(String userId, Long userInoutSeq);

    public List<Map<String, Object>> countByAcId(String userId, String trdDt );




}
