package zinsoft.web.common.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.util.Result;
import zinsoft.web.common.dao.mapper.CodeMapper;
import zinsoft.web.common.dto.CodeDto;
import zinsoft.web.common.entity.Code;
import zinsoft.web.common.entity.CodeId;
import zinsoft.web.common.repository.CodeRepository;
import zinsoft.web.common.service.CodeService;
import zinsoft.web.exception.CodeMessageException;

@Service("codeService")
@Transactional
public class CodeServiceImpl extends EgovAbstractServiceImpl implements CodeService {

    //private Type dtoListType = new TypeToken<List<CodeDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    CodeMapper codeMapper;

    @Resource
    CodeRepository codeRepository;

    @Override
    public void insert(CodeDto dto) {
        codeMapper.insert(dto);
    }

    @Override
    public List<CodeDto> list(String codeId) {
        return codeRepository.list(codeId, null);
    }

    @Override
    public List<CodeDto> list(String codeId, String withNormalize) {
        return codeRepository.list(codeId, withNormalize);
    }

    @Override
    public List<CodeDto> listStartsWithCodeVal(String codeId, String codeVal) {
        return codeRepository.listStartsWithCodeVal(codeId, codeVal);
    }

    @Override
    public void update(CodeDto dto) {
        Code code = getEntity(dto);

        modelMapper.map(dto, code);
        codeRepository.save(code);
    }

    @Override
    public void delete(CodeDto dto) {
        Code code = getEntity(dto);

        modelMapper.map(dto, code);
        codeRepository.delete(code);
    }

    private Code getEntity(CodeDto dto) {
        Optional<Code> data = codeRepository.findById(modelMapper.map(dto, CodeId.class));

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

}
