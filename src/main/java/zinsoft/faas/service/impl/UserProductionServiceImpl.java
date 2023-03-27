package zinsoft.faas.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserProductionDto;
import zinsoft.faas.entity.UserProduction;
import zinsoft.faas.repository.UserProductionRepository;
import zinsoft.faas.service.UserProductionService;
import zinsoft.util.Constants;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.exception.CodeMessageException;

@Service
@Transactional
public class UserProductionServiceImpl extends EgovAbstractServiceImpl implements UserProductionService {

    @Resource
    UserProductionRepository userProductionRepository;

//    @Autowired
//    UserDiaryService userDiaryService;

    @Autowired
    ModelMapper modelMapper;

    private UserProduction getEntity(Long userProductionSeq) {
        Optional<UserProduction> data = userProductionRepository.findById(userProductionSeq);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    private UserProduction getEntity(String userId, Long userProductionSeq) {
        if (StringUtils.isBlank(userId)) {
            return getEntity(userProductionSeq);
        }

        UserProduction userProduction = userProductionRepository.findByUserIdAndUserProductionSeq(userId, userProductionSeq);

        if (userProduction == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return userProduction;
    }

    private UserProduction getEntityByDiarySeq(String userId, Long userDiarySeq) {
        UserProduction userProduction = null;
        if (StringUtils.isBlank(userId)) {
            userProduction = userProductionRepository.findByUserDiarySeqAndStatusCd(userDiarySeq, "N");
            /*if (userProduction == null) {
                throw new CodeMessageException(Result.NO_DATA);
            }*/

            return userProduction;
        }

        userProduction = userProductionRepository.findByUserIdAndUserDiarySeqAndStatusCd(userId, userDiarySeq, "N");

        /*if (userProduction == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }*/

        return userProduction;
    }

    @Override
    public void insert(UserProductionDto dto) {
        //userDiaryService.insert(dto);

        UserProduction userProduction = modelMapper.map(dto, UserProduction.class);
        userProduction.setUserProductionSeq(null);
        userProductionRepository.save(userProduction);
    }

    @Override
    public void insert(UserDiaryDto diaryDto) {
        if (diaryDto.getQuan() == null || diaryDto.getQuan() <= 0)
            return;

        if (diaryDto.getPackTCd() == null) {
            return;
        }

        UserProductionDto dto = new UserProductionDto();
        dto.setUserDiarySeq(diaryDto.getUserDiarySeq());
        dto.setUserId(diaryDto.getUserId());
        dto.setPrdDt(diaryDto.getActDt());
        dto.setQuan(diaryDto.getQuan());
        dto.setPackTCd(diaryDto.getPackTCd());
        dto.setRemark("영농일지 입력");
        dto.setUpdateYn("N");
        dto.setDeleteYn("N");

        if(UserDiaryDto.DIARY_T_CD_DIARY.equals(diaryDto.getDiaryTCd())) {
            dto.setPlanTCd(UserProductionDto.PLAN_T_CD_ACTUAL);
        } else {
            dto.setPlanTCd(UserProductionDto.PLAN_T_CD_BUDGET);
        }

        UserProduction userProduction = modelMapper.map(dto, UserProduction.class);
        userProduction.setUserProductionSeq(null);
        userProductionRepository.save(userProduction);
    }

    @Override
    public UserProductionDto get(Long userProductionSeq, String userId) {
        return userProductionRepository.get(userProductionSeq, userId);
    }

    @Override
    public DataTablesResponse<UserProductionDto> page(Map<String, Object> search, Pageable pageable) {
        Page<UserProductionDto> dtoPage = userProductionRepository.page(search, pageable);
        search.put("planTCd", null);
        List<UserProductionDto> totalList = userProductionRepository.totalByPlanTCd(search);

        DataTablesResponse<UserProductionDto> result = DataTablesResponse.of(dtoPage);

        String rateYn = (String) search.get("rateYn");
        if (StringUtils.isNoneBlank(rateYn) && rateYn.equals(Constants.YN_YES)) {
            List<UserProductionDto> list = new ArrayList<>();
            list.addAll(result.getItems());

            UserProductionDto item1 = totalList.get(0);//계획
            UserProductionDto item2 = totalList.get(1);//실적

            if (item2.getPlanTCd().startsWith(UserProductionDto.PLAN_T_CD_ACTUAL)) {//실적
                // x = 100*생산량/계획량

                Double bQuan = item1.getQuan();//b : 계획
                Double aQuan = item2.getQuan();//a : 실적

                if(bQuan > 0){ // 생산 계획이 없을 때 생산율이 나오지 않아야 한다.
                    // 기존 코드대로 하면 계획이 없을때 생산율이 100% 초과 값이 나온다
                    Double prdRate = aQuan * 100.0 / bQuan;
                    prdRate = Math.round(prdRate * 100) / 100.0;
                    item2.setPrdRate(prdRate);
                }else{
                    item2.setPrdRate(0.0);
                }


            }
            list.add(0, item1);
            list.add(1, item2);
            result.setItems(list);

        }
        return result;
    }

    @Override
    public void update(UserProductionDto dto) {
        UserProduction userProduction = getEntity(dto.getUserId(), dto.getUserProductionSeq());
        dto.setUpdateDtm(new Date());

//        dto.setUserId(userProduction.getUserId());
//        dto.setUserDiarySeq(userProduction.getUserDiarySeq());
//        userDiaryService.update(dto);

        modelMapper.map(dto, userProduction);
        userProductionRepository.save(userProduction);
    }

    @Override
    public void updateBy(UserDiaryDto diaryDto) {
        UserProduction userProduction = getEntityByDiarySeq(diaryDto.getUserId(), diaryDto.getUserDiarySeq());
        UserProductionDto dto = null;

        if(userProduction != null) {
            dto = modelMapper.map(userProduction, UserProductionDto.class);
        }

        if(dto != null) {
            if (diaryDto.getQuan() == null || diaryDto.getQuan() <= 0 || StringUtils.isBlank(diaryDto.getPackTCd())) {
                deleteByUserDiarySeq(dto.getUserId(), dto.getUserDiarySeq());
            } else {
                dto.setUpdateDtm(new Date());

                dto.setPrdDt(diaryDto.getActDt());
                dto.setQuan(diaryDto.getQuan());
                dto.setPackTCd(diaryDto.getPackTCd());
                if(UserDiaryDto.DIARY_T_CD_DIARY.equals(diaryDto.getDiaryTCd())) {
                    dto.setPlanTCd(UserProductionDto.PLAN_T_CD_ACTUAL);
                } else {
                    dto.setPlanTCd(UserProductionDto.PLAN_T_CD_BUDGET);
                }

                modelMapper.map(dto, userProduction);
                userProductionRepository.save(userProduction);
            }
        } else {
            insert(diaryDto);
        }
    }

    @Override
    public int delete(String userId, Long userProductionSeq) {
        UserProduction userProduction = getEntity(userId, userProductionSeq);

//        userDiaryService.delete(userId, userProduction.getUserDiarySeq());

        userProduction.setUpdateDtm(new Date());
        userProduction.setStatusCd("D");
        userProductionRepository.save(userProduction);
        return 1;
    }

    @Override
    public void deleteByUserId(String userId) {
        userProductionRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteByUserDiarySeq(String userId, Long userDiarySeq) {
        userProductionRepository.deleteByUserDiarySeq(userId, new Long[] { userDiarySeq });
    }

    @Override
    public void deleteByUserDiarySeq(String userId, Long[] userDiarySeqs) {
        userProductionRepository.deleteByUserDiarySeq(userId, userDiarySeqs);
    }

    @Override
    public void delete(String userId, Long[] userProductionSeq) {
        // TODO Auto-generated method stub
        for (Long seq : userProductionSeq) {
            delete(userId, seq);
        }
    }

    @Override
    public List<Map<String, Object>> chartByPlanTCd(Map<String, Object> map){
        return userProductionRepository.chartByPlanTCd(map);
    }

}
