package zinsoft.faas.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserManureDto;
import zinsoft.util.DataTablesResponse;

public interface UserManureService {

    void insert(UserManureDto dto);

    UserManureDto get(UserManureDto dto);

    List<UserManureDto> list(Map<String, Object> param);

    DataTablesResponse<UserManureDto> page(Map<String, Object> search, Pageable pageable);

    void update(UserManureDto dto);

    void delete(UserManureDto dto);

    int delete(String userId, Long userChemicalSeq);

    void delete(String userId, Long[] userManureSeqs);

    void deleteByUserId(String userId);





}