package zinsoft.faas.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.UserChemicalMapper;
import zinsoft.faas.dto.UserChemicalDto;
import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.entity.UserChemical;
import zinsoft.faas.repository.UserChemicalRepository;
import zinsoft.faas.service.UserChemicalService;
import zinsoft.faas.service.UserChemicalStockService;
import zinsoft.faas.vo.Page;
import zinsoft.faas.vo.PagingParam;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.exception.CodeMessageException;

@Service
public class UserChemicalServiceImpl extends EgovAbstractServiceImpl implements UserChemicalService {

    @Resource
    UserChemicalMapper userChemicalMapper;

    @Resource
    UserChemicalRepository userChemicalRepository;

    @Autowired
    ModelMapper modelMapper;

    @Resource
    UserChemicalStockService userChemicalStockService;

    private UserChemical getEntity(Long id) {
        Optional<UserChemical> data = userChemicalRepository.findById(id);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    private UserChemical getEntity(String userId, Long id) {
        if (StringUtils.isBlank(userId)) {
            return getEntity(id);
        }

        UserChemical userChemical = userChemicalRepository.findByUserIdAndUserChemicalSeq(userId, id);

        if (userChemical == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return userChemical;
    }

    @Override
    public void insert(UserChemicalDto dto) {
        //userChemicalMapper.insert(dto);
        UserChemical userChemical = modelMapper.map(dto, UserChemical.class);
        userChemicalRepository.save(userChemical);
        dto.setUserChemicalSeq(userChemical.getUserChemicalSeq());
    }

    @Override
    public UserChemicalDto get(UserChemicalDto dto) {
        return userChemicalRepository.get(dto);
    }

    @Override
    public List<UserChemicalDto> list(Map<String, Object> param) {
        return userChemicalRepository.list(param);
    }

    @Override
    public Page<UserChemicalDto> page(PagingParam pagingParam) {
        Page<UserChemicalDto> page = new Page<UserChemicalDto>();
        List<UserChemicalDto> list = userChemicalMapper.page(pagingParam);
        int count = userChemicalMapper.count(pagingParam);

        page.setItems(list);
        page.setTotalPages((int) Math.ceil(count / (double) pagingParam.getLimit()));
        page.setCurrPage(pagingParam.getPage());
        page.setCount(count);

        return page;
    }

    @Override
    public DataTablesResponse<UserChemicalDto> page(Map<String, Object> search, Pageable pageable) {
        org.springframework.data.domain.Page<UserChemicalDto> dtoPage = userChemicalRepository.page(search, pageable);
        return DataTablesResponse.of(dtoPage);
    }

    @Override
    public void update(UserChemicalDto dto) {
        //userChemicalMapper.update(dto);

        UserChemical userChemical = getEntity(dto.getUserId(), dto.getUserChemicalSeq());
        dto.setUpdateDtm(new Date());

        modelMapper.map(dto, userChemical);
        userChemicalRepository.save(userChemical);
    }

    @Override
    public void delete(UserChemicalDto dto) {
        //userChemicalStockService.deleteByUserChemicalSeq(dto.getUserId(), dto.getUserChemicalSeq());
        userChemicalMapper.delete(dto);
    }

    @Override
    public int delete(String userId, Long userChemicalSeq) {
        UserChemical userChemical = getEntity(userId, userChemicalSeq);

        userChemical.setUpdateDtm(new Date());
        userChemical.setStatusCd("D");
        userChemicalRepository.save(userChemical);
        return 1;
    }

    @Override
    public void delete(String userId, Long[] userChemicalSeqs) {
        UserChemicalDto dto = new UserChemicalDto();

        dto.setUserId(userId);

        for (Long userChemicalSeq : userChemicalSeqs) {
            dto.setUserChemicalSeq(userChemicalSeq);
            delete(dto);
        }
    }

    @Override
    public void deleteByUserId(String userId) {
        userChemicalRepository.deleteByUserId(userId);

    }

}
