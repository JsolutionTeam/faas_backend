package zinsoft.faas.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.UserManureMapper;
import zinsoft.faas.dto.UserManureDto;
import zinsoft.faas.entity.UserManure;
import zinsoft.faas.repository.UserManureRepository;
import zinsoft.faas.service.UserManureService;
import zinsoft.faas.service.UserManureStockService;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.exception.CodeMessageException;

@Service
@Transactional
public class UserManureServiceImpl extends EgovAbstractServiceImpl implements UserManureService {

    @Resource
    UserManureMapper userManureMapper;

    @Resource
    UserManureRepository userManureRepository;

    @Autowired
    ModelMapper modelMapper;

    @Resource
    UserManureStockService userManureStockService;

    private UserManure getEntity(Long userManureSeq) {
        Optional<UserManure> data = userManureRepository.findById(userManureSeq);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    private UserManure getEntity(String userId, Long userManureSeq) {
        if (StringUtils.isBlank(userId)) {
            return getEntity(userManureSeq);
        }

        UserManure userManure = userManureRepository.findByUserIdAndUserManureSeq(userId, userManureSeq);

        if (userManure == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return userManure;
    }

    @Override
    public void insert(UserManureDto dto) {
        UserManure userManure = modelMapper.map(dto, UserManure.class);
        userManure.setUserManureSeq(null);
        userManureRepository.save(userManure);
        dto.setUserManureSeq(userManure.getUserManureSeq());
    }

    @Override
    public UserManureDto get(UserManureDto dto) {
        return userManureRepository.get(dto);
    }

    @Override
    public List<UserManureDto> list(Map<String, Object> param) {
        return userManureRepository.list(param);
    }

    @Override
    public DataTablesResponse<UserManureDto> page(Map<String, Object> search, Pageable pageable) {
        Page<UserManureDto> dtoPage = userManureRepository.page(search, pageable);
        return DataTablesResponse.of(dtoPage);
    }

    @Override
    public void update(UserManureDto dto) {
        UserManure userManure = getEntity(dto.getUserId(), dto.getUserManureSeq());
        dto.setUpdateDtm(new Date());

        modelMapper.map(dto, userManure);
        userManure.setCpFerN(dto.getCpFerN());
        userManure.setCpFerP(dto.getCpFerP());
        userManure.setCpFerK(dto.getCpFerK());
        userManureRepository.save(userManure);
    }

    @Override
    public void delete(UserManureDto dto) {
       // userManureStockService.deleteByUserManureSeq(dto.getUserId(), dto.getUserManureSeq());
        userManureMapper.delete(dto);
    }

    @Override
    public int delete(String userId, Long userManureSeq) {
        UserManure userManure = getEntity(userId, userManureSeq);

        userManure.setUpdateDtm(new Date());
        userManure.setStatusCd("D");
        userManureRepository.save(userManure);
        return 1;
    }

    @Override
    public void delete(String userId, Long[] userManureSeqs) {
        UserManureDto dto = new UserManureDto();

        dto.setUserId(userId);

        for (Long userManureSeq : userManureSeqs) {
            dto.setUserManureSeq(userManureSeq);
            delete(dto);
        }
    }

    @Override
    public void deleteByUserId(String userId) {
        userManureRepository.deleteByUserId(userId);
    }

}
