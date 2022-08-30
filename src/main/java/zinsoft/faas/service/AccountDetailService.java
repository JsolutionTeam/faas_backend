package zinsoft.faas.service;

import java.util.List;

import zinsoft.faas.dto.AccountDetailDto;

public interface AccountDetailService {

    List<AccountDetailDto> listByAcId(String acId);

}