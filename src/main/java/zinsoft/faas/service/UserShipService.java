package zinsoft.faas.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.dto.UserShipDto;
import zinsoft.util.DataTablesResponse;

public interface UserShipService {

    void insert(UserShipDto dto);

    void insert(UserInoutDto inoutDto);

    UserShipDto get(Long userShipSeq, String userId);

    DataTablesResponse<UserShipDto> page(Map<String, Object> search, Pageable pageable);

    void update(UserShipDto dto);

    void updateBy(UserInoutDto inoutDto);

    int delete(String userId, Long userChemicalSeq);

    void delete(String userId,Long[] userShipSeqs);

    void deleteByUserId(String userId);

    void deleteByUserInoutSeq(String userId, Long userInoutSeq);

    void deleteByUserInoutSeq(String userId, Long[] userInoutSeqs);

    List<Map<String, Object>> chartByPlanTCd(Map<String, Object> map);

}