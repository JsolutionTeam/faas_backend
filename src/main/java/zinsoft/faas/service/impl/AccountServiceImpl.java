package zinsoft.faas.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
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

@Service
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
        //accountMapper.insert(dto);
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
//
//        basicDataService.createBasicData();
    }

    @Override
    public List<AccountDto> list() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("inputYn", "Y");
        return accountMapper.list(param);
    }

    @Override
    public List<AccountDto> list(Map<String, Object> param) {
        //return accountMapper.list(param);
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

//    @Override
//    public Page<AccountDto> page(PagingParam pagingParam) {
//        Page<AccountDto> page = new Page<AccountDto>();
//        List<AccountDto> list = accountMapper.page(pagingParam);
//        int count = accountMapper.count(pagingParam);
//
//        page.setItems(list);
//        page.setTotalPages((int) Math.ceil(count / (double) pagingParam.getLimit()));
//        page.setCurrPage(pagingParam.getPage());
//        page.setCount(count);
//
//        return page;
//    }

    @Override
    public DataTablesResponse<AccountDto> page(Map<String, Object> search, Pageable pageable) {
        Page<AccountDto> dtoPage = accountRepository.page(search, pageable);
        return DataTablesResponse.of(dtoPage);
    }

//    public DHXGrid<Account> list002002001Grid1(String dhxFields, Map<String, String> params) {
//        List<Account> list = accountMapper.list002002001Grid1(params);
//        return new DHXGrid<Account>(Account.class, dhxFields, list);
//    }

//    public DHXGrid<Account> list002002005Grid1(String dhxFields, Map<String, String> params) {
//        List<Account> list = accountMapper.list002002005Grid1(params);
//        return new DHXGrid<Account>(Account.class, dhxFields, list);
//    }

    @Override
    public List<AccountDto> getRootAcIdList() {
        //List<AccountDto> list = accountMapper.rootAcIdList();
        List<Account> list = accountRepository.findAllByUpAcIdIsNullOrderByAcIdAsc();

        List<AccountDto> result = modelMapper.map(list, new TypeToken<List<AccountDto>>() {}.getType());
        return result;
    }

    @Override
    public List<AccountDto> getlvl2AcIdList(String acId) {
        //List<AccountDto> list = accountMapper.lvl2AcIdList(acId);
        List<Account> list = accountRepository.findAllByUpAcIdOrderByAcIdAsc(acId);

        List<AccountDto> result = modelMapper.map(list, new TypeToken<List<AccountDto>>() {}.getType());
        return result;
    }

    @Override
    public List<AccountDto> bpTCdList(String cdPrtId) {
        List<AccountDto> list = accountMapper.bpTCdList(cdPrtId);
        return list;
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
        for(String acId : acIds) {
            delete(acId);
        }
        basicDataService.createBasicData();
    }
}
