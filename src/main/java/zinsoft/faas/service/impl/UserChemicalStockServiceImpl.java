package zinsoft.faas.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.UserChemicalStockMapper;
import zinsoft.faas.dto.UserChemicalStockDto;
import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.entity.UserChemicalStock;
import zinsoft.faas.repository.UserChemicalStockRepository;
import zinsoft.faas.service.UserChemicalStockService;
import zinsoft.faas.service.UserInoutService;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.exception.CodeMessageException;

@Service
public class UserChemicalStockServiceImpl extends EgovAbstractServiceImpl implements UserChemicalStockService {

    @Resource
    UserChemicalStockRepository userChemicalStockRepository;

    @Autowired
    UserInoutService userInoutService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserChemicalStockMapper userChemicalStockMapper;

    //사용되지 않음. 추후 필요할때위해 남김
//    private UserChemicalStock getEntity(Long id) {
//        Optional<UserChemicalStock> data = userChemicalStockRepository.findById(id);
//
//        if (!data.isPresent()) {
//            throw new CodeMessageException(Result.NO_DATA);
//        }
//
//        return data.get();
//    }

    private UserChemicalStock getEntity(Long userChemicalStockSeq) {
        Optional<UserChemicalStock> data = userChemicalStockRepository.findById(userChemicalStockSeq);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    private UserChemicalStock getEntity(String userId, Long userChemicalStockSeq) {
        if (StringUtils.isBlank(userId)) {
            return getEntity(userChemicalStockSeq);
        }

        UserChemicalStock userChemicalStock = userChemicalStockRepository.findByUserIdAndUserChemicalStockSeq(userId, userChemicalStockSeq);

        if (userChemicalStock == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return userChemicalStock;
    }

    private void insert(UserChemicalStockDto dto) {
        dto.setUserChemicalStockSeq(null);
        UserChemicalStock userChemicalStock = modelMapper.map(dto, UserChemicalStock.class);
        userChemicalStockRepository.save(userChemicalStock);
    }

    @Override
    public void insertWith(UserChemicalStockDto dto) {
        if ("I".equals(dto.getSupInoutCd())) {
            userInoutService.insert(dto);
        }
        insert(dto);
    }

    @Override
    public void insert(UserDiaryDto diaryDto) {
        List<UserChemicalStockDto> list = diaryDto.getChemicalList();
        Long userDiarySeq = diaryDto.getUserDiarySeq();

        if (list != null && list.isEmpty() == false) {
            for (UserChemicalStockDto dto : list) {
                if (dto.getUserChemicalSeq() == null)
                    continue;

                dto.setUserDiarySeq(userDiarySeq);
                dto.setUserId(diaryDto.getUserId());
                dto.setInoutDt(diaryDto.getActDt());
                dto.setSupInoutCd("O");
                dto.setDeleteYn("N");
                dto.setUpdateYn("N");
                dto.setCropSeq(diaryDto.getCropSeq());
                dto.setUserCropSeq(diaryDto.getUserCropSeq());
                dto.setRemark("영농일지 입력");

                insert(dto);
            }
        }
    }

    @Override
    public void insert(UserInoutDto inoutDto) {
        List<UserChemicalStockDto> list = inoutDto.getChemicalList();

        if (list != null && list.isEmpty() == false) {
            for (UserChemicalStockDto dto : list) {
                if (dto.getUserChemicalSeq() == null)
                    continue;

                dto.setInoutDt(inoutDto.getTrdDt());
                dto.setSupInoutCd("I");
                dto.setDeleteYn("N");
                dto.setUpdateYn("N");
                dto.setUserId(inoutDto.getUserId());
                dto.setCropSeq(inoutDto.getCropSeq());
                dto.setUserCropSeq(inoutDto.getUserCropSeq());
                dto.setUserInoutSeq(inoutDto.getUserInoutSeq());
                dto.setAmt(inoutDto.getAmt());
                dto.setRemark("수입지출 입력");

                insert(dto);
            }
        }
    }

    @Override
    public UserChemicalStockDto get(UserChemicalStockDto dto) {
        return get(dto.getUserId(), dto.getUserChemicalStockSeq());
    }

    @Override
    public UserChemicalStockDto get(String userId, Long userChemicalStockSeq) {
        return userChemicalStockRepository.get(userId, userChemicalStockSeq);
    }

    @Override
    public List<UserChemicalStockDto> list(Map<String, Object> param) {
        String stDt = (String)param.get("stDt");
        String edDt = (String)param.get("edDt");
        if(StringUtils.isNotBlank(stDt) && StringUtils.isNotBlank(edDt)) {
            stDt = stDt.replace("-", "");
            edDt = edDt.replace("-", "");
            param.put("stDt", stDt);
            param.put("edDt", edDt);
        }
        return userChemicalStockMapper.list(param);
        //return userChemicalStockRepository.list(param);
    }

    @Override
    public DataTablesResponse<UserChemicalStockDto> page(Map<String, Object> search, Pageable pageable) {
        Page<UserChemicalStockDto> page = null;
        String stDt = (String)search.get("stDt");
        String edDt = (String)search.get("edDt");
        if(StringUtils.isNotBlank(stDt) && StringUtils.isNotBlank(edDt)) {
            stDt = stDt.replace("-", "");
            edDt = edDt.replace("-", "");
            search.put("stDt", stDt);
            search.put("edDt", edDt);
        }

        List<UserChemicalStockDto> list = userChemicalStockMapper.page(search, pageable);
        int count = userChemicalStockMapper.count(search);

        page = new PageImpl<>(list, pageable, count);
        return DataTablesResponse.of(page);
    }

//    @Override
//    public DataTablesResponse<UserChemicalStockDto> page(Map<String, Object> search, Pageable pageable) {
//        org.springframework.data.domain.Page<UserChemicalStockDto> dtoPage = userChemicalStockRepository.page(search, pageable);
//        return DataTablesResponse.of(dtoPage);
//    }

    @Override
    public List<UserChemicalStockDto> listByUserDiarySeq(String userId, Long userDiarySeq) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userDiarySeq", userDiarySeq);
        return userChemicalStockRepository.list(map);
    }

    @Override
    public List<UserChemicalStockDto> listByUserInoutSeq(String userId, Long userInoutSeq) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userInoutSeq", userInoutSeq);
        return userChemicalStockRepository.list(map);
    }

    @Override
    public Long countUsed(String userId, Long userChemicalSeq) {
        return userChemicalStockRepository.countByUserChemicalSeqAndStatusCd(userChemicalSeq, "N");
    }

    private void update(UserChemicalStockDto dto) {
        UserChemicalStock userChemicalStock = getEntity(dto.getUserId(), dto.getUserChemicalStockSeq());

        dto.setUpdateDtm(new Date());

        modelMapper.map(dto, userChemicalStock);
        userChemicalStockRepository.save(userChemicalStock);
    }

    @Override
    public void updateWith(UserChemicalStockDto dto) {
        UserChemicalStock userChemicalStock = getEntity(dto.getUserId(), dto.getUserChemicalStockSeq());

        dto.setUserId(userChemicalStock.getUserId());
        dto.setUserInoutSeq(userChemicalStock.getUserInoutSeq());
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
        List<UserChemicalStockDto> list = diaryDto.getChemicalList();
        if (list != null && list.isEmpty() == false) {
            for (UserChemicalStockDto dto : list) {
                if (dto != null) {
                    dto.setUserId(diaryDto.getUserId());
                    dto.setInoutDt(diaryDto.getActDt());
                    dto.setCropSeq(diaryDto.getCropSeq());
                    dto.setUserCropSeq(diaryDto.getUserCropSeq());
                    dto.setSupInoutCd("O");
                    dto.setDeleteYn("N");
                    dto.setUpdateYn("N");
                    if (dto.getUserChemicalStockSeq() != null) {
                        if (dto.getUserChemicalSeq() == null) {
                            delete(dto.getUserId(), dto.getUserChemicalStockSeq());
                        } else {
                            //userChemicalStockMapper.updateBy(dto);
                            update(dto);
                        }
                    } else {
                        if (dto.getUserChemicalSeq() != null) {
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
        List<UserChemicalStockDto> list = inoutDto.getChemicalList();

        if (list != null && list.isEmpty() == false) {
            for (UserChemicalStockDto dto : list) {
                if (dto != null) {
                    dto.setUserId(inoutDto.getUserId());
                    dto.setAmt(inoutDto.getAmt());
                    dto.setInoutDt(inoutDto.getTrdDt());
                    dto.setCropSeq(inoutDto.getCropSeq());
                    dto.setUserCropSeq(inoutDto.getUserCropSeq());
                    dto.setSupInoutCd("I");
                    dto.setDeleteYn("N");
                    dto.setUpdateYn("N");
                    if (dto.getUserChemicalStockSeq() != null) {
                        if (dto.getUserChemicalSeq() == null) {
                            delete(dto.getUserId(), dto.getUserChemicalStockSeq());
                        } else {
                            //userChemicalStockMapper.updateBy(dto);
                            update(dto);
                        }
                    } else {
                        if (dto.getUserChemicalSeq() != null) {
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
    public void delete(String userId, Long userChemicalStockSeq) {

        UserChemicalStock userChemicalStock = getEntity(userId, userChemicalStockSeq);
        userChemicalStock.setUpdateDtm(new Date());
        userChemicalStock.setStatusCd("D");
        userChemicalStockRepository.save(userChemicalStock);
    }

    @Override
    public void deleteWith(String userId, Long userChemicalStockSeq) {

        UserChemicalStock userChemicalStock = getEntity(userId, userChemicalStockSeq);
        if (userChemicalStock.getUserInoutSeq() != null) {
            userInoutService.deleteBy(userChemicalStock.getUserId(), userChemicalStock.getUserInoutSeq());
        }
        userChemicalStock.setUpdateDtm(new Date());
        userChemicalStock.setStatusCd("D");
        userChemicalStockRepository.save(userChemicalStock);
    }

    //    public void deleteWith(String userId, Long userChemicalStockSeq) {
    //        UserChemicalStock old = get(userId, userChemicalStockSeq);
    //        if (old.getSlipSeq() != null) {
    //            Slip slip = new Slip();
    //            slip.setUserId(userId);
    //            slip.setSlipSeq(old.getSlipSeq());
    //            slipService.deleteByStock(slip);
    //        }
    //        userChemicalStockMapper.delete(userId, userChemicalStockSeq);
    //    }

    @Override
    public void delete(String userId, Long[] userChemicalStockSeqs) {
        for (Long userChemicalStockSeq : userChemicalStockSeqs) {
            deleteWith(userId, userChemicalStockSeq);
        }
    }

    @Override
    public void deleteByUserDiarySeq(String userId, Long[] userDiarySeqs) {
        userChemicalStockRepository.deleteByUserDiarySeq(userId, userDiarySeqs);
    }

    @Override
    public void deleteByUserDiarySeq(String userId, Long userDiarySeq) {

        userChemicalStockRepository.deleteByUserDiarySeq(userId, new Long[] { userDiarySeq });
    }

    @Override
    public void deleteByUserInoutSeq(String userId, Long[] userInoutSeqs) {
        userChemicalStockRepository.deleteByUserInoutSeq(userId, userInoutSeqs);
    }

    @Override
    public void deleteByUserInoutSeq(String userId, Long userInoutSeq) {
        userChemicalStockRepository.deleteByUserInoutSeq(userId, new Long[] { userInoutSeq });
    }

    @Override
    public void deleteByUserChemicalSeq(String userId, Long userChemicalSeq) {
        userChemicalStockRepository.deleteByUserChemicalSeq(userId, userChemicalSeq);
    }

    @Override
    public void deleteByUserId(String userId) {
        userChemicalStockRepository.deleteByUserId(userId);
    }

}
