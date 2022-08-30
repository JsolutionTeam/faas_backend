package zinsoft.faas.service.impl;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dto.CalDto;
import zinsoft.faas.entity.Cal;
import zinsoft.faas.entity.CalId;
import zinsoft.faas.repository.CalRepository;
import zinsoft.faas.service.CalService;
import zinsoft.util.Result;
import zinsoft.web.exception.CodeMessageException;

@Service
public class CalServiceImpl extends EgovAbstractServiceImpl implements CalService {

    private Type dtoListType = new TypeToken<List<CalDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    CalRepository calRepository;

    @Override
    public String get(String calDt, String calTCd) {
        Cal cal = getEntity(new CalId(calDt, calTCd));
        return cal.getExprNm();
    }

    @Override
    public List<CalDto> list(String startDt, String endDt) {
        List<Cal> list = calRepository.findByCalDtBetween(startDt, endDt);
        return modelMapper.map(list, dtoListType);
    }

    private Cal getEntity(CalId id) {
        Optional<Cal> data = calRepository.findById(id);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

}
