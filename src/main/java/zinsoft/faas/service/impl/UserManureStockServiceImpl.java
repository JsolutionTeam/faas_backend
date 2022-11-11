package zinsoft.faas.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.UserManureStockMapper;
import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.dto.UserManureStockDto;
import zinsoft.faas.entity.UserManureStock;
import zinsoft.faas.repository.UserManureStockRepository;
import zinsoft.faas.service.UserInoutService;
import zinsoft.faas.service.UserManureStockService;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.exception.CodeMessageException;

@Service
public class UserManureStockServiceImpl extends EgovAbstractServiceImpl implements UserManureStockService {

    @Autowired
    UserManureStockRepository userManureStockRepository;

    @Autowired
    UserInoutService userInoutService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserManureStockMapper userManureStockMapper;

    private UserManureStock getEntity(Long userManureStockSeq) {
        Optional<UserManureStock> data = userManureStockRepository.findById(userManureStockSeq);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    private UserManureStock getEntity(String userId, Long userManureStockSeq) {
        if (StringUtils.isBlank(userId)) {
            return getEntity(userManureStockSeq);
        }

        UserManureStock userManureStock = userManureStockRepository.findByUserIdAndUserManureStockSeq(userId, userManureStockSeq);

        if (userManureStock == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return userManureStock;
    }

    private void insert(UserManureStockDto dto) {
        UserManureStock userManureStock = modelMapper.map(dto, UserManureStock.class);
        userManureStockRepository.save(userManureStock);
    }

    @Override
    public void insertWith(UserManureStockDto dto) {
        if ("I".equals(dto.getSupInoutCd())) {
            userInoutService.insert(dto);
        }
        UserManureStock userManureStock = modelMapper.map(dto, UserManureStock.class);
        userManureStockRepository.save(userManureStock);
    }

    @Override
    public void insert(UserDiaryDto diaryDto) {
        List<UserManureStockDto> list = diaryDto.getManureList();
        Long userDiarySeq = diaryDto.getUserDiarySeq();

        if (list != null && list.isEmpty() == false ) {
            for(UserManureStockDto dto : list) {
                if (dto.getUserManureSeq() == null)
                    continue;

                dto.setUserDiarySeq(userDiarySeq);
                dto.setUserId(diaryDto.getUserId());
                dto.setInoutDt(diaryDto.getActDt());
                dto.setSupInoutCd("O");
                Long cropCd = null;
                try{
                    cropCd = Long.parseLong(diaryDto.getCropCd());
                }catch(Exception ignore){}
                dto.setCropSeq(cropCd);
                dto.setUserCropSeq(diaryDto.getUserCropSeq());
                dto.setRemark("영농일지 입력");
                dto.setUpdateYn("N");
                dto.setDeleteYn("N");
                insert(dto);
            }
        }
    }

    @Override
    public void insert(UserInoutDto inoutDto) {
        List<UserManureStockDto> list = inoutDto.getManureList();

        if (list != null && list.isEmpty() == false ) {
            for(UserManureStockDto dto : list) {
                if (dto.getUserManureSeq() == null)
                    continue;

                dto.setInoutDt(inoutDto.getTrdDt());
                dto.setSupInoutCd("I");
                dto.setUserId(inoutDto.getUserId());
                dto.setCropSeq(inoutDto.getCropSeq());
                dto.setUserCropSeq(inoutDto.getUserCropSeq());
                dto.setUserInoutSeq(inoutDto.getUserInoutSeq());
                dto.setAmt(inoutDto.getAmt());
                dto.setRemark("수입지출 입력");
                dto.setUpdateYn("N");
                dto.setDeleteYn("N");

                insert(dto);
            }
        }

    }

    @Override
    public UserManureStockDto get(UserManureStockDto dto) {
        return get(dto.getUserId(), dto.getUserManureStockSeq());
    }

    @Override
    public UserManureStockDto get(String userId, Long userManureStockSeq) {
        return userManureStockRepository.get(userId, userManureStockSeq);
    }

    @Override
    public List<UserManureStockDto> list(Map<String, Object> param) {
        String stDt = (String)param.get("stDt");
        String edDt = (String)param.get("edDt");
        if(StringUtils.isNotBlank(stDt) && StringUtils.isNotBlank(edDt)) {
            stDt = stDt.replace("-", "");
            edDt = edDt.replace("-", "");
            param.put("stDt", stDt);
            param.put("edDt", edDt);
        }
//        return userManureStockMapper.list(param);
        return userManureStockRepository.list(param);
    }

    @Override
    public DataTablesResponse<UserManureStockDto> page(Map<String, Object> search, Pageable pageable) {
        Page<UserManureStockDto> page = null;
        String stDt = (String)search.get("stDt");
        String edDt = (String)search.get("edDt");
        if(StringUtils.isNotBlank(stDt) && StringUtils.isNotBlank(edDt)) {
            stDt = stDt.replace("-", "");
            edDt = edDt.replace("-", "");
            search.put("stDt", stDt);
            search.put("edDt", edDt);
        }

        return DataTablesResponse.of(userManureStockRepository.page(search, pageable));
    }

//    @Override
//    public DataTablesResponse<UserManureStockDto> page(Map<String, Object> search, Pageable pageable) {
//        org.springframework.data.domain.Page<UserManureStockDto> dtoPage = userManureStockRepository.page(search, pageable);
//        return DataTablesResponse.of(dtoPage);
//    }

    @Override
    public List<UserManureStockDto> listByUserDiarySeq(String userId, Long userDiarySeq) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userDiarySeq", userDiarySeq);
        return userManureStockRepository.list(map);
    }

    @Override
    public List<UserManureStockDto> listByUserInoutSeq(String userId, Long userInoutSeq) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userInoutSeq", userInoutSeq);
        return userManureStockRepository.list(map);
    }


    @Override
    public Long countUsed(String userId, Long userManureSeq) {
        return userManureStockRepository.countByUserManureSeqAndStatusCd(userManureSeq, "N");
    }

    private void update(UserManureStockDto dto) {
        UserManureStock userManureStock = getEntity(dto.getUserId(), dto.getUserManureStockSeq());

        dto.setUpdateDtm(new Date());

        modelMapper.map(dto, userManureStock);
        userManureStockRepository.save(userManureStock);
    }

    @Override
    public void updateWith(UserManureStockDto dto) {
        UserManureStock userManureStock = getEntity(dto.getUserId(), dto.getUserManureStockSeq());

        dto.setUserId(userManureStock.getUserId());
        dto.setUserInoutSeq(userManureStock.getUserInoutSeq());
        if (dto.getUserInoutSeq() != null) {
            if ("O".equals(dto.getSupInoutCd())) {
                userInoutService.deleteBy(dto.getUserId(), dto.getUserInoutSeq());
                dto.setUserInoutSeq(new Long(0));
            } else {
                userInoutService.update(dto);
            }
        } else {
            if ("I".equals(dto.getSupInoutCd())) {
                userInoutService.insert(dto);
            }
        }
        update(dto);
    }

    @Override
    public void updateBy(UserDiaryDto diaryDto) throws IllegalStateException, IOException {
        List<UserManureStockDto> list = diaryDto.getManureList();
        if (list != null && list.isEmpty() == false) {
            for (UserManureStockDto dto : list) {
                if (dto != null) {
                    dto.setUserId(diaryDto.getUserId());
                    dto.setInoutDt(diaryDto.getActDt());
                    Long cropCd = null;
                    try{
                        cropCd = Long.parseLong(diaryDto.getCropCd());
                    }catch(Exception ignore){}
                    dto.setCropSeq(cropCd);
                    dto.setUserCropSeq(diaryDto.getUserCropSeq());
                    dto.setSupInoutCd("O");
                    dto.setDeleteYn("N");
                    dto.setUpdateYn("N");
                    if (dto.getUserManureStockSeq() != null) {
                        if (dto.getUserManureSeq() == null) {
                            delete(dto.getUserId(), dto.getUserManureStockSeq());
                        } else {
                            //userManureStockMapper.updateBy(dto);
                            update(dto);
                        }
                    } else {
                        if (dto.getUserManureSeq() != null) {
                            dto.setUserDiarySeq(diaryDto.getUserDiarySeq());
                            dto.setRemark("영농일지 입력");
                            insert(dto);
                        }
                    }
                }
            }
        } else {
            deleteByUserDiarySeq(diaryDto.getUserId(), diaryDto.getUserDiarySeq());
        }
    }

    @Override
    public void updateBy(UserInoutDto inoutDto) throws IllegalStateException, IOException {
        List<UserManureStockDto> list = inoutDto.getManureList();

        if (list != null && list.isEmpty() == false) {
            for (UserManureStockDto dto : list) {
                if (dto != null) {
                    dto.setUserId(inoutDto.getUserId());
                    dto.setAmt(inoutDto.getAmt());
                    dto.setInoutDt(inoutDto.getTrdDt());
                    dto.setCropSeq(inoutDto.getCropSeq());
                    dto.setUserCropSeq(inoutDto.getUserCropSeq());
                    dto.setSupInoutCd("I");
                    dto.setDeleteYn("N");
                    dto.setUpdateYn("N");
                    if (dto.getUserManureStockSeq() != null) {
                        if (dto.getUserManureSeq() == null) {
                            delete(dto.getUserId(), dto.getUserManureStockSeq());
                        } else {
                            //userManureStockMapper.updateBy(dto);
                            update(dto);
                        }
                    } else {
                        if (dto.getUserManureSeq() != null) {
                            dto.setInoutDt(inoutDto.getTrdDt());
                            dto.setUserInoutSeq(inoutDto.getUserInoutSeq());
                            dto.setRemark("수입지출 입력");
                            insert(dto);
                        }
                    }
                }
            }
        } else {
            deleteByUserInoutSeq(inoutDto.getUserId(), inoutDto.getUserInoutSeq());
        }
    }

    @Override
    public void delete(String userId, Long userManureStockSeq) {
        UserManureStock userManureStock = getEntity(userId, userManureStockSeq);

        userManureStock.setUpdateDtm(new Date());
        userManureStock.setStatusCd("D");
        userManureStockRepository.save(userManureStock);
    }

    @Override
    public void deleteWith(String userId, Long userManureStockSeq) {
        UserManureStock userManureStock = getEntity(userId, userManureStockSeq);
        if (userManureStock.getUserInoutSeq() != null) {
            userInoutService.deleteBy(userManureStock.getUserId(),userManureStock.getUserInoutSeq());
        }
        userManureStock.setUpdateDtm(new Date());
        userManureStock.setStatusCd("D");
        userManureStockRepository.save(userManureStock);
    }

    @Override
    public void delete(String userId, Long[] userManureStockSeqs) {
        for (Long userManureStockSeq : userManureStockSeqs) {
            delete(userId, userManureStockSeq);
        }
    }

    @Override
    public void deleteByUserDiarySeq(String userId, Long[] userDiarySeqs) {
        userManureStockRepository.deleteByUserDiarySeq(userId, userDiarySeqs);
    }

    @Override
    public void deleteByUserDiarySeq(String userId, Long userDiarySeq) {
        userManureStockRepository.deleteByUserDiarySeq(userId, new Long[] { userDiarySeq });
    }

    @Override
    public void deleteByUserInoutSeq(String userId, Long[] userInoutSeqs) {
        userManureStockRepository.deleteByUserInoutSeq(userId, userInoutSeqs);
    }

    @Override
    public void deleteByUserInoutSeq(String userId, Long userInoutSeq) {
        userManureStockRepository.deleteByUserInoutSeq(userId, new Long[] {userInoutSeq});
    }

    @Override
    public void deleteByUserManureSeq(String userId, Long userManureSeq) {
        userManureStockRepository.deleteByUserManureSeq(userId, userManureSeq);
    }

    @Override
    public void deleteByUserId(String userId) {
        userManureStockRepository.deleteByUserId(userId);
    }

}
