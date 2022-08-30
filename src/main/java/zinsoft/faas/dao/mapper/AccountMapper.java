package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.AccountDto;
import zinsoft.faas.vo.PagingParam;

@Mapper
public interface AccountMapper {

    void insert(AccountDto vo);

    AccountDto get(String acId);

    String getAcIdByAcNm(String acNm);

    List<AccountDto> list(Map<String, Object> param);

    List<AccountDto> list2(PagingParam pagingParam);

    List<AccountDto> listSome(@Param("acIds") String[] acIds);

    List<AccountDto> page(PagingParam pagingParam);

    List<AccountDto> bpTCdList(String cdPrtId);

    List<AccountDto> list002002001Grid1(Map<String, String> params);

    List<AccountDto> list002002005Grid1(Map<String, String> params);

    List<AccountDto> rootAcIdList();

    List<AccountDto> lvl2AcIdList(String rootAcId);

    int count(PagingParam pagingParam);

    void update(AccountDto vo);

    void delete(String acId);

}
