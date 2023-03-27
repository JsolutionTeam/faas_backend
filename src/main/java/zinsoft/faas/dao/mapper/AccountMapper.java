package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.AccountDto;
import zinsoft.faas.vo.PagingParam;

@Mapper
public interface AccountMapper {

    List<AccountDto> list(Map<String, Object> param);

    List<AccountDto> list2(PagingParam pagingParam);

    List<AccountDto> listSome(@Param("acIds") String[] acIds);

    List<AccountDto> bpTCdList(String cdPrtId);
}
