package zinsoft.web.common.service;

import java.util.List;

import zinsoft.web.common.dto.CodeDto;

public interface CodeService {

    void insert(CodeDto dto);

    List<CodeDto> list(String codeId);

    List<CodeDto> list(String codeId, String withNormalize);

    List<CodeDto> listStartsWithCodeVal(String codeId, String codeVal);

    void update(CodeDto dto);

    void delete(CodeDto dto);

}