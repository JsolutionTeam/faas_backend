package zinsoft.faas.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dto.AccountDetailDto;
import zinsoft.faas.entity.AccountDetail;
import zinsoft.faas.repository.AccountDetailRepository;
import zinsoft.faas.service.AccountDetailService;

@Service
public class AccountDetailServiceImpl extends EgovAbstractServiceImpl implements AccountDetailService {

    private Type dtoListType = new TypeToken<List<AccountDetailDto>>() {
    }.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    AccountDetailRepository accountDetailRepository;

    @Override
    public List<AccountDetailDto> listByAcId(String acId) {
        List<AccountDetail> list = accountDetailRepository.findByAcId(acId);
        return modelMapper.map(list, dtoListType);
    }

}
