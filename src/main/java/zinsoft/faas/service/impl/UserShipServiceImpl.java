package zinsoft.faas.service.impl;

import java.util.ArrayList;
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
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.dto.UserShipDto;
import zinsoft.faas.entity.UserShip;
import zinsoft.faas.repository.UserShipRepository;
import zinsoft.faas.service.UserInoutService;
import zinsoft.faas.service.UserShipService;
import zinsoft.util.Constants;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.exception.CodeMessageException;

@Service
@Transactional
public class UserShipServiceImpl extends EgovAbstractServiceImpl implements UserShipService {

    @Resource
    UserShipRepository userShipRepository;

    @Autowired
    UserInoutService userInoutService;

    @Autowired
    ModelMapper modelMapper;

    private UserShip getEntity(Long userShipSeq) {
        Optional<UserShip> data = userShipRepository.findById(userShipSeq);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    private UserShip getEntity(String userId, Long userShipSeq) {
        if (StringUtils.isBlank(userId)) {
            return getEntity(userShipSeq);
        }

        UserShip userShip = userShipRepository.findByUserIdAndUserShipSeq(userId, userShipSeq);

        if (userShip == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return userShip;
    }

    private UserShip getEntityByInoutSeq(String userId, Long userInoutSeq) {
        UserShip userShip = null;
        if (StringUtils.isBlank(userId)) {
            userShip = userShipRepository.findByUserInoutSeqAndStatusCd(userInoutSeq, "N");
            /*if (userProduction == null) {
                throw new CodeMessageException(Result.NO_DATA);
            }*/

            return userShip;
        }

        userShip = userShipRepository.findByUserIdAndUserInoutSeqAndStatusCd(userId, userInoutSeq, "N");

        /*if (userProduction == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }*/

        return userShip;
    }

    @Override
    public void insert(UserShipDto dto) {
        if(dto.getPlanTCd().equals(UserShipDto.PLAN_T_CD_ACTUAL)) {
            userInoutService.insert(dto);
        }

        UserShip userShip = modelMapper.map(dto, UserShip.class);
        userShip.setUserShipSeq(null);
        userShipRepository.save(userShip);
    }

    @Override
    public void insert(UserInoutDto inoutDto) {
        if ("503".equals(inoutDto.getAcId()) == false)
            return;

        if (inoutDto.getQuan() == null || inoutDto.getQuan() <= 0)
            return;

        if (inoutDto.getPackTCd() == null) {
            return;
        }

        UserShipDto dto = new UserShipDto();
        dto.setUserInoutSeq(inoutDto.getUserInoutSeq());
        dto.setUserId(inoutDto.getUserId());
        dto.setShipDt(inoutDto.getTrdDt());
        dto.setUnitPack(inoutDto.getUnitPack());
        dto.setUnitAmt(inoutDto.getUnitAmt());
        dto.setAmt(inoutDto.getAmt());
        dto.setQuan(inoutDto.getQuan());
        dto.setGradeTCd(inoutDto.getGradeTCd());
        dto.setPackTCd(inoutDto.getPackTCd());
        dto.setPlanTCd(UserShipDto.PLAN_T_CD_ACTUAL);
        dto.setRemark("수입지출 입력");
        dto.setUpdateYn("N");
        dto.setDeleteYn("N");

        UserShip userShip = modelMapper.map(dto, UserShip.class);
        userShip.setUserShipSeq(null);
        userShipRepository.save(userShip);
    }

    @Override
    public UserShipDto get(Long userShipSeq, String userId) {
        return userShipRepository.get(userShipSeq, userId);
    }

    @Override
    public DataTablesResponse<UserShipDto> page(Map<String, Object> search, Pageable pageable) {
        Page<UserShipDto> dtoPage = userShipRepository.page(search, pageable);
        search.put("planTCd", null);
        List<UserShipDto> totalList =  userShipRepository.totalByPlanTCd(search);

        DataTablesResponse<UserShipDto> result = DataTablesResponse.of(dtoPage);
        String rateYn = (String)search.get("rateYn");
        if(StringUtils.isNoneBlank(rateYn) && rateYn.equals(Constants.YN_YES)){
            List<UserShipDto> list = new ArrayList<>();
            list.addAll(result.getItems());

            UserShipDto item1 = totalList.get(0);//계획
            UserShipDto item2 = totalList.get(1);//실적

            if(item2.getPlanTCd().startsWith(UserShipDto.PLAN_T_CD_ACTUAL)) {//실적
                // x = 100*사용량/계획량
                Double bQuan = (item1.getQuan() == 0)? 1: item1.getQuan();
                Double aQuan = (item2.getQuan() == 0)? 1: item2.getQuan();
                Double prdRate = aQuan * 100.0 / bQuan;
                if(prdRate > 0) {
                    prdRate = Math.round(prdRate*100)/100.0;
                }
                item1.setUnitPack(bQuan);
                item2.setUnitPack(aQuan);
                item2.setShipRate(prdRate);
            }
            list.add(0, item1);
            list.add(1, item2);

            result.setItems(list);
        }

        return result;
    }

    @Override
    public void update(UserShipDto dto) {
        UserShip userShip = getEntity(dto.getUserId(), dto.getUserShipSeq());
        dto.setUpdateDtm(new Date());

        dto.setUserId(userShip.getUserId());
        dto.setUserInoutSeq(userShip.getUserInoutSeq());
        if (dto.getUserInoutSeq() != null) {
            if (UserShipDto.PLAN_T_CD_BUDGET.equals(dto.getPlanTCd())) {
                userInoutService.deleteBy(dto.getUserId(), dto.getUserInoutSeq());
                dto.setUserInoutSeq(new Long(0));
            } else {
                userInoutService.update(dto);
            }
        } else {
            if (UserShipDto.PLAN_T_CD_ACTUAL.equals(dto.getPlanTCd())) {
                userInoutService.insert(dto);
            }
        }

        modelMapper.map(dto, userShip);
        userShip.setDefRate(dto.getDefRate());
        userShipRepository.save(userShip);
    }

    @Override
    public void updateBy(UserInoutDto inoutDto) {
        UserShip userShip = getEntityByInoutSeq(inoutDto.getUserId(), inoutDto.getUserInoutSeq());
        UserShipDto dto = null;

        if(userShip != null) {
            dto = modelMapper.map(userShip, UserShipDto.class);
        }

        if(dto != null) {
            if (("503".equals(inoutDto.getAcId()) == false) || inoutDto.getQuan() == null || inoutDto.getQuan() <= 0 || StringUtils.isBlank(inoutDto.getPackTCd()) == true) {
                deleteByUserInoutSeq(dto.getUserId(), dto.getUserInoutSeq());
            } else {
                dto.setUpdateDtm(new Date());

                dto.setShipDt(inoutDto.getTrdDt());
                dto.setUnitPack(inoutDto.getUnitPack());
                dto.setUnitAmt(inoutDto.getUnitAmt());
                dto.setAmt(inoutDto.getAmt());
                dto.setQuan(inoutDto.getQuan());
                dto.setGradeTCd(inoutDto.getGradeTCd());
                dto.setPackTCd(inoutDto.getPackTCd());
                dto.setPlanTCd(UserShipDto.PLAN_T_CD_ACTUAL);

                modelMapper.map(dto, userShip);
                userShipRepository.save(userShip);
            }
        } else {
            insert(inoutDto);
        }
    }

    @Override
    public int delete(String userId, Long userShipSeq) {
        UserShip userShip = getEntity(userId, userShipSeq);

        if (userShip.getUserInoutSeq() != null) {
            userInoutService.deleteBy(userShip.getUserId(),userShip.getUserInoutSeq());
        }

        userShip.setUpdateDtm(new Date());
        userShip.setStatusCd("D");
        userShipRepository.save(userShip);
        return 1;
    }

    @Override
    public void delete(String userId, Long[] userShipSeqs) {
        // TODO Auto-generated method stub
        for(Long seq : userShipSeqs) {
            delete(userId, seq);
        }
    }

    @Override
    public void deleteByUserId(String userId) {
        userShipRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteByUserInoutSeq(String userId, Long userInoutSeq) {
        userShipRepository.deleteByUserInoutSeq(userId, new Long[] { userInoutSeq });
    }

    @Override
    public void deleteByUserInoutSeq(String userId, Long[] userInoutSeqs) {
        userShipRepository.deleteByUserInoutSeq(userId, userInoutSeqs);
    }

    @Override
    public List<Map<String, Object>> chartByPlanTCd(Map<String, Object> map){
        return userShipRepository.chartByPlanTCd(map);
    }
}
