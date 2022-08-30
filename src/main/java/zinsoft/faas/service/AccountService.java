package zinsoft.faas.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.AccountDto;
import zinsoft.faas.vo.PagingParam;
import zinsoft.util.DataTablesResponse;

public interface AccountService {

    void insert(AccountDto dto);

    List<AccountDto> list();

    List<AccountDto> list(Map<String, Object> param);

    List<AccountDto> list2(PagingParam pagingParam);

    List<AccountDto> listSome(String[] acIds);

    AccountDto get(String acId);

    String getAcIdByAcNm(String acNm);

    //Page<AccountDto> page(PagingParam pagingParam);

    DataTablesResponse<AccountDto> page(Map<String, Object> search, Pageable pageable);

    List<AccountDto> getRootAcIdList();

    List<AccountDto> getlvl2AcIdList(String acId);

    List<AccountDto> bpTCdList(String cdPrtId);

    void update(AccountDto dto);

    void delete(String acId);

    void delete(String[] acIds);



}