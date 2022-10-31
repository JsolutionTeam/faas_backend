package zinsoft.faas.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserDiaryFileDto;
import zinsoft.faas.dto.UserProductionDto;
import zinsoft.faas.entity.UserDiary;
import zinsoft.faas.repository.UserDiaryRepository;
import zinsoft.faas.service.ActivityService;
import zinsoft.faas.service.UserChemicalStockService;
import zinsoft.faas.service.UserCropService;
import zinsoft.faas.service.UserDiaryFileService;
import zinsoft.faas.service.UserDiaryService;
import zinsoft.faas.service.UserManureStockService;
import zinsoft.faas.service.UserProductionService;
import zinsoft.util.CommonUtil;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.service.CodeService;
import zinsoft.web.exception.CodeMessageException;

@Service
@Transactional
public class UserDiaryServiceImpl extends EgovAbstractServiceImpl implements UserDiaryService {

    @Resource
    UserDiaryRepository userDiaryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Resource
    UserDiaryFileService userDiaryFileService;

    @Resource
    UserChemicalStockService userChemicalStockService;

    @Resource
    UserManureStockService userManureStockService;

    @Resource
    UserProductionService userProductionService;

    @Resource
    CodeService codeService;

    @Resource
    UserCropService userCropService;

    @Resource
    ActivityService activityService;

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    private UserDiary getEntity(Long userDiarySeq) {
        Optional<UserDiary> data = userDiaryRepository.findById(userDiarySeq);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    private UserDiary getEntity(String userId, Long userDiarySeq) {
        if (StringUtils.isBlank(userId)) {
            return getEntity(userDiarySeq);
        }

        UserDiary userDiary = userDiaryRepository.findByUserIdAndUserDiarySeq(userId, userDiarySeq);

        if (userDiary == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return userDiary;
    }

    @Override
    public void insert(UserDiaryDto dto) throws IllegalStateException, IOException {
        dto.setActDt(dto.getActDt().replaceAll("\\D", ""));
        dto.setUserDiarySeq(null);

        UserDiary userDiary = modelMapper.map(dto, UserDiary.class);
        userDiary = userDiaryRepository.save(userDiary);

        dto.setUserDiarySeq(userDiary.getUserDiarySeq());

        userDiaryFileService.insert(dto);
        // 농약 사용
        userChemicalStockService.insert(dto);
        // 비료 사용
        userManureStockService.insert(dto);
        // 생산량 관리
        userProductionService.insert(dto);
    }

    @Override
    public void insert(String actStDt, String actEdDt, UserDiaryDto dto) throws IllegalStateException, IOException {
        if (actStDt != null && !actStDt.isEmpty() && actEdDt != null && !actEdDt.isEmpty()) {
            String actDt = actStDt;
            //            String inputSkyTCd = dto.getSkyTCd();
            //            Float inputRnf = dto.getRnf();
            //            Float inputTmn = dto.getTmn();
            //            Float inputTmx = dto.getTmx();
            //            Long inputReh = dto.getReh();

            while (actDt.compareTo(actEdDt) <= 0) {
                dto.setActDt(actDt);
                insert(dto);
                dto.setWorking(null);
                actDt = CommonUtil.addDay(actDt, "yyyyMMdd", 1);
                //                if (actDt.compareTo(actEdDt) <= 0) {
                //                    // 과거의 여러날 등록 시 과거 날의 날씨를 가져온다.
                //                    setWeatherPast(dto, actDt, inputSkyTCd, inputRnf, inputTmn, inputTmx, inputReh);
                //                }
            }
        } else {
            insert(dto);
        }
    }

    @Override
    public void insert(UserProductionDto dto) {
        UserDiaryDto diaryDto = new UserDiaryDto();
        diaryDto.setUserDiarySeq(null);
        diaryDto.setActDt(dto.getPrdDt());
        diaryDto.setUserId(dto.getUserId());
        String diaryTCd = (dto.getPlanTCd().equals(UserProductionDto.PLAN_T_CD_ACTUAL)) ? "D" : "P";
        diaryDto.setDiaryTCd(diaryTCd);
        diaryDto.setRemark(dto.getRemark());
        diaryDto.setQuan(dto.getQuan());
        diaryDto.setPackTCd(dto.getPackTCd());
        diaryDto.setGradeTCd(dto.getGradeTCd());

        UserDiary userDiary = modelMapper.map(diaryDto, UserDiary.class);
        userDiary = userDiaryRepository.save(userDiary);
        dto.setUserDiarySeq(userDiary.getUserDiarySeq());
    }

    @Override
    public void copy(String userId, String srcYear, String dstYear) {

    }

    @Override
    public UserDiaryDto get(String userId, Long userDiarySeq) {
        UserDiaryDto dto = userDiaryRepository.get(new UserDiaryDto(userDiarySeq, userId));

        if (dto != null) {
            setFiles(dto);
            dto.setChemicalList(userChemicalStockService.listByUserDiarySeq(userId, userDiarySeq));
            dto.setManureList(userManureStockService.listByUserDiarySeq(userId, userDiarySeq));
        }
        return dto;
    }

    @Override
    public DataTablesResponse<UserDiaryDto> page(Map<String, Object> search, Pageable pageable) {
        //DataTablesResponse<UserDiaryDto> page;
        Page<UserDiaryDto> page = null;
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");

        if ((search.get("sActDt") == null || search.get("eActDt") == null) && search.get("actDt") == null) {
            new CodeMessageException(Result.BAD_REQUEST);
        }

        if (search.get("actDt") != null) {
            String actDt = (String) search.get("actDt");
            actDt = actDt.replaceAll("[^0-9]", "");

            try {
                fm.parse(actDt);
            } catch (ParseException e) {
                new CodeMessageException(Result.BAD_REQUEST, "조회날짜 오류");
            }

            search.put("actDt", actDt);
        } else {
            String sActDt = (String) search.get("sActDt");
            String eActDt = (String) search.get("eActDt");
            sActDt = sActDt.replaceAll("[^0-9]", "");
            eActDt = eActDt.replaceAll("[^0-9]", "");

            Date sDate = null;
            Date eDate = null;
            try {
                sDate = fm.parse(sActDt);
                eDate = fm.parse(eActDt);
            } catch (ParseException e) {
                new CodeMessageException(Result.BAD_REQUEST, "조회날짜 오류");
            }

            search.put("sActDt", sActDt);
            search.put("eActDt", eActDt);
        }

      //  Page<UserDiaryDto> dtoPage = userDiaryRepository.page(search, pageable);

//        page = DataTablesResponse.of(dtoPage);

//        List<UserDiaryDto> list = dtoPage.getContent();
//        if (list != null && list.size() > 0) {
//            setFiles(dtoPage.getContent());
//            setUsedChemicalStock(list);
//            setUsedManureStock(list);
//        }
//        return page;
        List<UserDiaryDto> list = null;
        try {
            //
            page = userDiaryRepository.page(search, pageable);
//                list = userDiaryMapper.pageData(search, pageable);
            if (list != null && list.size() > 0) {
                setFiles(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataTablesResponse.of(page);

    }

    @Override
    public List<String> listYear(String userId, String diaryTCd, String year) {
        return userDiaryRepository.listYear(userId, diaryTCd, year);
    }

    @Override
    public void update(UserDiaryDto dto) throws IllegalStateException, IOException {
        dto.setActDt(dto.getActDt().replaceAll("\\D", ""));
        UserDiary userDiary = getEntity(dto.getUserId(), dto.getUserDiarySeq());
        dto.setUpdateDtm(new Date());

        if (dto.getUserId() == null) {
            dto.setUserId(userDiary.getUserId());
        }
        modelMapper.map(dto, userDiary);
        // 숫자이고 값이 null인 경우 map이 복사가 안됨. 수동 복사
        userDiary.setManSelf(dto.getManSelf());
        userDiary.setManSelfTm(dto.getManSelfTm());
        userDiary.setManSelfTmm(dto.getManSelfTmm());
        userDiary.setWomanSelf(dto.getWomanSelf());
        userDiary.setWomanSelfTm(dto.getWomanSelfTm());
        userDiary.setWomanSelfTmm(dto.getWomanSelfTmm());
        userDiary.setManHire(dto.getManHire());
        userDiary.setManHireTm(dto.getManHireTm());
        userDiary.setManHireTmm(dto.getManHireTmm());
        userDiary.setWomanHire(dto.getWomanHire());
        userDiary.setWomanHireTm(dto.getWomanHireTm());
        userDiary.setWomanHireTmm(dto.getWomanHireTmm());
        userDiary.setQuan(dto.getQuan());
        userDiary.setTmn(dto.getTmn());
        userDiary.setTmx(dto.getTmx());
        userDiary.setRnf(dto.getRnf());
        userDiary.setTemp(dto.getTemp());
        userDiary.setReh(dto.getReh());

        userDiaryFileService.update(dto);

        // 농약 사용
        userChemicalStockService.updateBy(dto);
        // 비료 사용
        userManureStockService.updateBy(dto);
        // 생산량 관리
        userProductionService.updateBy(dto);

        userDiaryRepository.save(userDiary);
    }

    @Override
    public void update(UserProductionDto dto) {
        UserDiary userDiary = getEntity(dto.getUserId(), dto.getUserDiarySeq());

        userDiary.setActDt(dto.getPrdDt());
        String diaryTCd = (dto.getPlanTCd().equals(UserProductionDto.PLAN_T_CD_ACTUAL)) ? "D" : "P";
        userDiary.setDiaryTCd(diaryTCd);
        // 값이 null인 경우 map이 복사가 안됨. 수동 복사
        userDiary.setRemark(dto.getRemark());
        userDiary.setQuan(dto.getQuan());
        userDiary.setPackTCd(dto.getPackTCd());
        userDiary.setGradeTCd(dto.getGradeTCd());

        userDiary = userDiaryRepository.save(userDiary);
        dto.setUserDiarySeq(userDiary.getUserDiarySeq());
    }

    @Override
    public void delete(String userId, Long userDiarySeq) {
        if (UserInfoUtil.isAdmin() && StringUtils.isBlank(userId)) {
            UserDiary userDiary = getEntity(userId, userDiarySeq);
            userId = userDiary.getUserId();
        }
        userDiaryFileService.deleteByUserDiarySeq(userId, userDiarySeq);
        // 농약 사용
        userChemicalStockService.deleteByUserDiarySeq(userId, userDiarySeq);
        // 비료 사용
        userManureStockService.deleteByUserDiarySeq(userId, userDiarySeq);
        // 생산량 관리
        userProductionService.deleteByUserDiarySeq(userId, userDiarySeq);

        userDiaryRepository.delete(userId, userDiarySeq);
    }

    @Override
    public void delete(UserDiaryDto dto) {
        delete(dto.getUserId(), dto.getUserDiarySeq());
    }

    public void deleteBy(String userId, Long userDiarySeq) {
        userDiaryRepository.delete(userId, userDiarySeq);
    }

    @Override
    public void delete(String userId, Long[] userDiarySeqs) {
        if (UserInfoUtil.isAdmin() && StringUtils.isBlank(userId)) {
            if(userDiarySeqs != null && userDiarySeqs.length > 0) {
                for(Long userDiarySeq : userDiarySeqs) {
                    UserDiary userDiary = getEntity(userId, userDiarySeq);
                    userId = userDiary.getUserId();
                    delete(userId, userDiarySeq);
                }
            }
        } else {
            userDiaryFileService.deleteByUserDiarySeqs(userId, userDiarySeqs);
            // 농약 사용
            userChemicalStockService.deleteByUserDiarySeq(userId, userDiarySeqs);
            // 비료 사용
            userManureStockService.deleteByUserDiarySeq(userId, userDiarySeqs);
            // 생산량 관리
            userProductionService.deleteByUserDiarySeq(userId, userDiarySeqs);

            userDiaryRepository.delete(userId, userDiarySeqs);
        }
    }

    @Override
    public void deleteByUserId(String userId) {
        // 영농일지 관련 - 영농일지, 사진, 사용비료, 사용농약, 사용농기계
        // userDiaryFileService.deleteByUserId(userId);
        // 농약 사용
        // userChemicalStockService.deleteByUserId(userId);
        // 비료 사용
        //   userManureStockService.deleteByUserId(userId);

        userDiaryRepository.deleteByUserId(userId);
    }

    private void setFiles(UserDiaryDto dto) {
        List<UserDiaryFileDto> dFileList = null;
        Map<Long, String> workingFiles = new LinkedHashMap<Long, String>();
        List<Long> workingFileSeqs = new ArrayList<Long>();

        dFileList = userDiaryFileService.list(dto, UserDiaryFileDto.FILE_K_CD_WORKING);
        for (UserDiaryFileDto f : dFileList) {
            workingFiles.put(f.getFileSeq(), f.getFileNm());
            workingFileSeqs.add(f.getFileSeq());
        }
        dto.setWorkingFiles(workingFiles);
        dto.setWorkingFileSeqs(workingFileSeqs);
    }

    private void setFiles(List<UserDiaryDto> list) {
        for (UserDiaryDto dto : list) {
            setFiles(dto);
        }
    }

    private void setUsedChemicalStock(List<UserDiaryDto> list) {
        for (UserDiaryDto dto : list) {
            dto.setChemicalList(userChemicalStockService.listByUserDiarySeq(dto.getUserId(), dto.getUserDiarySeq()));
        }
    }

    private void setUsedManureStock(List<UserDiaryDto> list) {
        for (UserDiaryDto dto : list) {
            dto.setManureList(userManureStockService.listByUserDiarySeq(dto.getUserId(), dto.getUserDiarySeq()));
        }
    }

    @Override
    public List<Map<String, Object>> countByActSeq(String userId, String actDt) {
        return userDiaryRepository.countByActSeq(userId, actDt);
    }
}