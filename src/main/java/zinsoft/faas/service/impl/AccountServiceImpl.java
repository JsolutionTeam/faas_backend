package zinsoft.faas.service.impl;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zinsoft.faas.dao.mapper.AccountMapper;
import zinsoft.faas.dto.AccountDto;
import zinsoft.faas.entity.Account;
import zinsoft.faas.repository.AccountRepository;
import zinsoft.faas.service.AccountService;
import zinsoft.faas.vo.PagingParam;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.common.service.BasicDataService;
import zinsoft.web.exception.CodeMessageException;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class AccountServiceImpl extends EgovAbstractServiceImpl implements AccountService {

    @Resource
    AccountMapper accountMapper;

    @Resource
    BasicDataService basicDataService;

    @Resource
    AccountRepository accountRepository;

    @Resource
    ModelMapper modelMapper;

    private Account getEntity(String id) {
        Optional<Account> data = accountRepository.findById(id);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    @Override
    public void insert(AccountDto dto) {
        String acId = accountRepository.nextAcId(dto.getUpAcId());
        Long exprSeq = accountRepository.nextExprSeqByUpAcId(dto.getUpAcId());
        String cdTCd = dto.getRootAcId().equals("400") ? "C" : "D";
        dto.setCdTCd(cdTCd);
        dto.setAcId(acId);
        dto.setExprSeq(exprSeq);
        dto.setExprYn("Y");
        dto.setUpdateYn("Y");
        dto.setDeleteYn("Y");
        dto.setBpTCd("P");
        dto.setPsTCd("N");//일반(N),매입(P),매출(S)

        Account account = modelMapper.map(dto, Account.class);
        accountRepository.save(account);
    }

    @Override
    public List<AccountDto> list() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("inputYn", "Y");
        return accountMapper.list(param);
    }

    @Override
    public List<AccountDto> list(Map<String, Object> param) {
        return accountRepository.list(param);
    }

    @Override
    public List<AccountDto> list2(PagingParam pagingParam) {
        return accountMapper.list2(pagingParam);
    }

    @Override
    public List<AccountDto> listSome(String[] acIds) {
        return accountMapper.listSome(acIds);
    }

    @Override
    public AccountDto get(String acId) {
        return accountRepository.get(acId);
    }

    @Override
    public String getAcIdByAcNm(String acNm) {
        return accountRepository.findAcIdByAcNm(acNm);
    }

    @Override
    public DataTablesResponse<AccountDto> page(Map<String, Object> search, Pageable pageable) {
        Page<AccountDto> dtoPage = accountRepository.page(search, pageable);
        return DataTablesResponse.of(dtoPage);
    }

    @Override
    public List<AccountDto> getRootAcIdList() {
        List<Account> list = accountRepository.findAllByUpAcIdIsNullOrderByAcIdAsc();

        List<AccountDto> result = modelMapper.map(list, new TypeToken<List<AccountDto>>() {
        }.getType());
        return result;
    }

    @Override
    public List<AccountDto> getlvl2AcIdList(String acId) {
        List<Account> list = accountRepository.findAllByUpAcIdOrderByAcIdAsc(acId);

        List<AccountDto> result = modelMapper.map(list, new TypeToken<List<AccountDto>>() {
        }.getType());
        return result;
    }

    @Override
    public List<AccountDto> bpTCdList(String cdPrtId) {
        return accountMapper.bpTCdList(cdPrtId);
    }

    @Override
    public void update(AccountDto dto) {
        Account account = getEntity(dto.getAcId());

        dto.setUpdateDtm(new Date());
        String cdTCd = dto.getRootAcId().equals("400") ? "C" : "D";
        dto.setCdTCd(cdTCd);

        modelMapper.map(dto, account);
        accountRepository.save(account);

        basicDataService.createBasicData();
    }

    @Override
    public void delete(String acId) {
        Account account = getEntity(acId);

        account.setUpdateDtm(new Date());
        account.setStatusCd("D");
        accountRepository.save(account);

        basicDataService.createBasicData();
    }

    @Override
    public void delete(String[] acIds) {
        for (String acId : acIds) {
            delete(acId);
        }
        basicDataService.createBasicData();
    }
}
