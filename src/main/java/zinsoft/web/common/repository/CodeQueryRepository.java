package zinsoft.web.common.repository;

import java.util.List;

import zinsoft.web.common.dto.CodeDto;

public interface CodeQueryRepository {

    List<CodeDto> list(String codeId, String withNormalize);

    List<CodeDto> listStartsWithCodeVal(String codeId, String codeVal);

}
