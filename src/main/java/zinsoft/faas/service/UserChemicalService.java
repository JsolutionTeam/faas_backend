package zinsoft.faas.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserChemicalDto;
import zinsoft.faas.vo.Page;
import zinsoft.faas.vo.PagingParam;
import zinsoft.util.DataTablesResponse;

public interface UserChemicalService {

    void insert(UserChemicalDto dto);

    UserChemicalDto get(UserChemicalDto dto);

    List<UserChemicalDto> list(Map<String, Object> param);

    Page<UserChemicalDto> page(PagingParam pagingParam);

    DataTablesResponse<UserChemicalDto> page(Map<String, Object> search, Pageable pageable);

    void update(UserChemicalDto dto);

    void delete(UserChemicalDto dto);

    int delete(String userId, Long userChemicalSeq);

    void delete(String userId, Long[] userChemicalSeqs);

    void deleteByUserId(String userId);

}