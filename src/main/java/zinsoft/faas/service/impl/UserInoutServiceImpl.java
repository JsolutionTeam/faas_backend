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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dto.UserChemicalStockDto;
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.dto.UserInoutFileDto;
import zinsoft.faas.dto.UserManureStockDto;
import zinsoft.faas.dto.UserShipDto;
import zinsoft.faas.entity.UserInout;
import zinsoft.faas.repository.UserInoutRepository;
import zinsoft.faas.service.AccountService;
import zinsoft.faas.service.UserChemicalStockService;
import zinsoft.faas.service.UserCropService;
import zinsoft.faas.service.UserInoutFileService;
import zinsoft.faas.service.UserInoutService;
import zinsoft.faas.service.UserManureStockService;
import zinsoft.faas.service.UserShipService;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.service.CodeService;
import zinsoft.web.exception.CodeMessageException;

@Service
@Transactional
public class UserInoutServiceImpl extends EgovAbstractServiceImpl implements UserInoutService {

    @Resource
    UserInoutRepository userInoutRepository;

    @Autowired
    ModelMapper modelMapper;

    @Resource
    UserInoutFileService userInoutFileService;

    @Resource
    AccountService accountService;

    @Resource
    CodeService codeService;

    @Resource
    UserCropService userCropService;

    @Resource
    UserChemicalStockService userChemicalStockService;

    @Resource
    UserManureStockService userManureStockService;

    @Resource
    UserShipService userShipService;

    private UserInout getEntity(Long userInoutSeq) {
        Optional<UserInout> data = userInoutRepository.findById(userInoutSeq);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    private UserInout getEntity(String userId, Long userInoutSeq) {
        if (StringUtils.isBlank(userId)) {
            return getEntity(userInoutSeq);
        }

        UserInout userInout = userInoutRepository.findByUserIdAndUserInoutSeqAndStatusCd(userId, userInoutSeq, "N");

        if (userInout == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return userInout;
    }

    @Override
    public void insert(UserInoutDto dto) throws IllegalStateException, IOException {
        dto.setUserInoutSeq(null);
        UserInout userInout = modelMapper.map(dto, UserInout.class);
        userInout = userInoutRepository.save(userInout);

        dto.setUserInoutSeq(userInout.getUserInoutSeq());

        userInoutFileService.insert(dto);

        // 농약 구매
        userChemicalStockService.insert(dto);
        // 비료 구매
        userManureStockService.insert(dto);
        // 출하량 관리
        userShipService.insert(dto);
    }

    @Override
    public void insert(UserChemicalStockDto dto) {
        UserInout inout = new UserInout();
        inout.setUserId(dto.getUserId());
        inout.setInoutCd("O");
        inout.setTrdDt(dto.getInoutDt());
        inout.setCropSeq(dto.getCropSeq());
        //inout.setQuan(dto.getQuan());
        inout.setAcId("407");
        inout.setAmt(dto.getAmt());
        inout.setInoutTCd(dto.getInoutTCd());
        inout.setUserCropSeq(dto.getUserCropSeq());

        dto.setUserInoutSeq(null);
        UserInout userInout = modelMapper.map(inout, UserInout.class);
        userInout = userInoutRepository.save(userInout);
        dto.setUserInoutSeq(userInout.getUserInoutSeq());
    }

    @Override
    public void insert(UserManureStockDto dto) {
        UserInout inout = new UserInout();
        inout.setUserId(dto.getUserId());
        inout.setInoutCd("O");
        inout.setTrdDt(dto.getInoutDt());
        inout.setCropSeq(dto.getCropSeq());
        if ("1".equals(dto.getManureTCd())) {
            inout.setAcId("419");
        } else {
            inout.setAcId("405");
        }
        inout.setAmt(dto.getAmt());
        inout.setInoutTCd(dto.getInoutTCd());

        dto.setUserInoutSeq(null);
        UserInout userInout = modelMapper.map(inout, UserInout.class);
        userInout = userInoutRepository.save(userInout);
        dto.setUserInoutSeq(inout.getUserInoutSeq());
    }

    @Override
    public void insert(UserShipDto dto) {
        UserInout inout = new UserInout();
        inout.setUserId(dto.getUserId());
        inout.setInoutCd("I");
        inout.setTrdDt(dto.getShipDt());
        inout.setAcId("503");
        inout.setQuan(dto.getQuan());
        inout.setUnitAmt(dto.getUnitAmt());
        inout.setAmt(dto.getAmt());
        inout.setInoutTCd(dto.getInoutTCd());
        inout.setUnitPack(dto.getUnitPack());
        inout.setPackTCd(dto.getPackTCd());
        inout.setRemark(dto.getRemark());

        dto.setUserInoutSeq(null);
        UserInout userInout = modelMapper.map(inout, UserInout.class);
        userInout = userInoutRepository.save(userInout);
        dto.setUserInoutSeq(userInout.getUserInoutSeq());
    }

    @Override
    public UserInoutDto get(String userId, Long userInoutSeq) {
        UserInoutDto dto = userInoutRepository.get(new UserInoutDto(userInoutSeq, userId));

        if (dto != null) {
            setFiles(dto);
            dto.setChemicalList(userChemicalStockService.listByUserInoutSeq(userId, userInoutSeq));
            dto.setManureList(userManureStockService.listByUserInoutSeq(userId, userInoutSeq));
        }

        return dto;
    }

    @Override
    public DataTablesResponse<UserInoutDto> page(Map<String, Object> search, Pageable pageable) {
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
        DataTablesResponse<UserInoutDto> page;

        if ((search.get("sTrdDt") == null || search.get("eTrdDt") == null) && search.get("trdDt") == null) {
            new CodeMessageException(Result.BAD_REQUEST);
        }

        if (search.get("trdDt") != null) {
            String trdDt = (String) search.get("trdDt");
            trdDt = trdDt.replaceAll("[^0-9]", "");

            Date trdDate = null;
            try {
                trdDate = fm.parse(trdDt);
            } catch (ParseException e) {
                new CodeMessageException(Result.BAD_REQUEST, "조회날짜 오류");
            }

            search.put("trdDt", trdDt);
        } else {
            String sTrdDt = (String) search.get("sTrdDt");
            String eTrdDt = (String) search.get("eTrdDt");
            sTrdDt = sTrdDt.replaceAll("[^0-9]", "");
            eTrdDt = eTrdDt.replaceAll("[^0-9]", "");

            Date sDate = null;
            Date eDate = null;
            try {
                sDate = fm.parse(sTrdDt);
                eDate = fm.parse(eTrdDt);
            } catch (ParseException e) {
                new CodeMessageException(Result.BAD_REQUEST, "조회날짜 오류");
            }

            search.put("sTrdDt", sTrdDt);
            search.put("eTrdDt", eTrdDt);
        }

        Page<UserInoutDto> dtoPage = userInoutRepository.page(search, pageable);
        page = DataTablesResponse.of(dtoPage);

        List<UserInoutDto> list = dtoPage.getContent();
        if (list != null && list.size() > 0) {
            setFiles(dtoPage.getContent());
            setUsedChemicalStock(list);
            setUsedManureStock(list);
        }
        return page;
    }

    @Override
    public void update(UserInoutDto dto) throws IllegalStateException, IOException {
        dto.setTrdDt(dto.getTrdDt().replaceAll("\\D", ""));
        UserInout userInout = getEntity(dto.getUserId(), dto.getUserInoutSeq());

        if(dto.getUserId() == null) {
            dto.setUserId(userInout.getUserId());
        }

        dto.setUpdateDtm(new Date());
        if (userInout != null) {
            modelMapper.map(dto, userInout);
           // 숫자이고 값이 null인 경우 map이 복사가 안됨. 수동 복사
            userInout.setUnitPack(dto.getUnitPack());
            userInout.setQuan(dto.getQuan());
            userInout.setUnitAmt(dto.getUnitAmt());

            userInoutFileService.update(dto);
            // 농약 구매
            userChemicalStockService.updateBy(dto);
            // 비료 구매
            userManureStockService.updateBy(dto);
            // 출하량 관리
            userShipService.updateBy(dto);

            userInoutRepository.save(userInout);
        }
    }

    @Override
    public void update(UserChemicalStockDto dto) {

        UserInout userInout = getEntity(dto.getUserId(), dto.getUserInoutSeq());
        dto.setUpdateDtm(new Date());

        userInout.setTrdDt(dto.getInoutDt());
        userInout.setCropSeq(dto.getCropSeq());
        userInout.setAmt(dto.getAmt());
        userInout.setInoutTCd(dto.getInoutTCd());
        userInout.setUserCropSeq(dto.getUserCropSeq());
        userInoutRepository.save(userInout);
    }

    @Override
    public void update(UserManureStockDto dto) {
        UserInout userInout = getEntity(dto.getUserId(), dto.getUserInoutSeq());
        dto.setUpdateDtm(new Date());

        userInout.setTrdDt(dto.getInoutDt());
        userInout.setCropSeq(dto.getCropSeq());
        userInout.setAmt(dto.getAmt());
        userInout.setInoutTCd(dto.getInoutTCd());

        if ("1".equals(dto.getManureTCd())) {
            userInout.setAcId("419");
        } else {
            userInout.setAcId("405");
        }
        userInoutRepository.save(userInout);
    }

    @Override
    public void update(UserShipDto dto) {
        UserInout userInout = getEntity(dto.getUserId(), dto.getUserInoutSeq());
        dto.setUpdateDtm(new Date());

        userInout.setInoutTCd(dto.getInoutTCd());
        userInout.setTrdDt(dto.getShipDt());
        userInout.setGradeTCd(dto.getGradeTCd());
        userInout.setQuan(dto.getQuan());
        userInout.setUnitAmt(dto.getUnitAmt());
        userInout.setAmt(dto.getAmt());
        userInout.setUnitPack(dto.getUnitPack());
        userInout.setPackTCd(dto.getPackTCd());
        userInout.setRemark(dto.getRemark());

        userInoutRepository.save(userInout);
        dto.setUserInoutSeq(userInout.getUserInoutSeq());
    }

    @Override
    public void updateReversing(Long userInoutSeq, UserInoutDto rDto) {
        UserInout userInout = getEntity(rDto.getUserId(), rDto.getUserInoutSeq());
        rDto.setUpdateDtm(new Date());
        rDto.setUserInoutSeq(userInoutSeq);
        rDto.setRTrdDt(rDto.getRTrdDt().replaceAll("\\D", ""));
        modelMapper.map(rDto, userInout);
        userInoutRepository.save(userInout);
    }

    @Override
    public void delete(String userId, Long userInoutSeq) {
        if (UserInfoUtil.isAdmin() && StringUtils.isBlank(userId)) {
            UserInout userInout = getEntity(userId, userInoutSeq);
            userId = userInout.getUserId();
        }

        userInoutFileService.deleteByUserInoutSeq(userId, userInoutSeq);

        // 농약 구매
        userChemicalStockService.deleteByUserInoutSeq(userId, userInoutSeq);
        // 비료 구매
        userManureStockService.deleteByUserInoutSeq(userId, userInoutSeq);
        // 출하량 관리
        userShipService.deleteByUserInoutSeq(userId, userInoutSeq);
        userInoutRepository.delete(userId, userInoutSeq);
    }

    @Override
    public void delete(UserInoutDto dto) {
        delete(dto.getUserId(), dto.getUserInoutSeq());
    }

    @Override
    public void delete(String userId, Long[] userInoutSeqs) {
        if (UserInfoUtil.isAdmin() && StringUtils.isBlank(userId)) {
            if(userInoutSeqs != null && userInoutSeqs.length > 0) {
                for(Long userInoutSeq : userInoutSeqs) {
                    UserInout userInout = getEntity(userId, userInoutSeq);
                    userId = userInout.getUserId();
                    delete(userId, userInoutSeq);
                }
            }
        } else {
            userInoutFileService.deleteByUserInoutSeqs(userId, userInoutSeqs);
            // 농약 사용
            userChemicalStockService.deleteByUserInoutSeq(userId, userInoutSeqs);
            // 비료 사용
            userManureStockService.deleteByUserInoutSeq(userId, userInoutSeqs);
            // 출하량 관리
            userShipService.deleteByUserInoutSeq(userId, userInoutSeqs);

            userInoutRepository.delete(userId, userInoutSeqs);
        }
    }

    @Override
    public void deleteReversing(String userId, Long userInoutSeq) {
        UserInout userInout = getEntity(userId, userInoutSeq);
        userInout.setUpdateDtm(new Date());
        userInout.setRTrdDt(null);
        userInout.setRInoutTCd(null);
        userInoutRepository.save(userInout);
    }

    @Override
    public void deleteByUserId(String userId) {

        userInoutFileService.deleteByUserId(userId);
        // 농약 사용
        userChemicalStockService.deleteByUserId(userId);
        // 비료 사용
        userManureStockService.deleteByUserId(userId);

        userInoutRepository.deleteByUserId(userId);
    }

    @Override
    public void deleteBy(String userId, Long userInoutSeq) {
        userInoutFileService.deleteByUserInoutSeq(userId, userInoutSeq);
        userInoutRepository.delete(userId, userInoutSeq);
    }

    @Override
    public List<Map<String, Object>> countByAcId(String userId, String trdDt) {
        return userInoutRepository.countByAcId(userId, trdDt);
    }

    private void setFiles(UserInoutDto dto) {
        List<UserInoutFileDto> fileList = null;
        Map<Long, String> imgFiles = new LinkedHashMap<Long, String>();
        List<Long> imgFileSeqs = new ArrayList<Long>();

        fileList = userInoutFileService.list(dto);
        for (UserInoutFileDto f : fileList) {
            imgFiles.put(f.getFileSeq(), f.getFileNm());
            imgFileSeqs.add(f.getFileSeq());
        }
        dto.setImgFiles(imgFiles);
        dto.setImgFileSeqs(imgFileSeqs);
    }

    private void setFiles(List<UserInoutDto> list) {
        for (UserInoutDto dto : list) {
            setFiles(dto);
        }
    }

    private void setUsedChemicalStock(List<UserInoutDto> list) {
        for (UserInoutDto dto : list) {
            dto.setChemicalList(userChemicalStockService.listByUserInoutSeq(dto.getUserId(), dto.getUserInoutSeq()));
        }
    }

    private void setUsedManureStock(List<UserInoutDto> list) {
        for (UserInoutDto dto : list) {
            dto.setManureList(userManureStockService.listByUserInoutSeq(dto.getUserId(), dto.getUserInoutSeq()));
        }
    }

}
