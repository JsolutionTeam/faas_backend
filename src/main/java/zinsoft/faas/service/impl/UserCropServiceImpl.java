package zinsoft.faas.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.UserCropMapper;
import zinsoft.faas.dto.UserCropDto;
import zinsoft.faas.entity.UserCrop;
import zinsoft.faas.repository.UserCropRepository;
import zinsoft.faas.service.UserCropService;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.exception.CodeMessageException;

@Service
public class UserCropServiceImpl extends EgovAbstractServiceImpl implements UserCropService{

    @Resource
    UserCropMapper userCropMapper;

    @Resource
    UserCropRepository userCropRepository;

    @Autowired
    ModelMapper modelMapper;

    private UserCrop getEntity(Long id) {
        Optional<UserCrop> data = userCropRepository.findById(id);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    @Override
    public void insert(UserCropDto dto) {
        /* if (dto.getYear() == null || dto.getYear().isEmpty()) {
            dto.setYear("1900");
        }

        // default value
        if (dto.getExprYN() == null || dto.getExprYN().isEmpty()) {
            dto.setExprYN("Y");
        }

        userCropMapper.insert(dto);*/
        UserCrop userCrop = modelMapper.map(dto, UserCrop.class);
        userCropRepository.save(userCrop);
    }

    @Override
    public int insertCopy(String workerId, String userId) {
        int cnt = userCropMapper.copy(userId);

        //if (cnt > 0) {
        //    userCropHistMapper.copy(workerId, Code.WORK_CD_INSERT, userId);
        //}

        return cnt;
    }

    @Override
    public UserCropDto get(UserCropDto dto) {
        /*UserCrop res = getEntity(dto.getUserCropSeq());
        return modelMapper.map(res, UserCropDto.class);*/
        return userCropRepository.get(dto);
    }

    @Override
    public UserCropDto get(String userId, Long cropSeq) {
        UserCropDto dto = new UserCropDto();
        dto.setUserId(userId);
        dto.setCropSeq(cropSeq);
        return get(dto);
    }

    @Override
    public UserCropDto get(String userId, Long cropSeq, Long userCropSeq) {
        UserCropDto dto = new UserCropDto();
        dto.setUserId(userId);
        dto.setCropSeq(cropSeq);
        dto.setUserCropSeq(userCropSeq);
        return get(dto);
    }

    @Override
    public Long getCropSeqByCropNm(String userId, String cropNm) {
        return userCropMapper.getCropSeqByCropNm(userId, cropNm);
    }

    @Override
    public int countByAliasNm(Long userCropSeq, String userId, String aliasNm) {
        return userCropMapper.countByAliasNm(userCropSeq, userId, aliasNm);

    }

    @Override
    public long countByAliasNm(UserCropDto dto) {
        return userCropRepository.countByAliasNm(dto);
    }

    @Override
    public boolean isExistUserCropId(String userId, Long userCropSeq) {
        return userCropRepository.existsByUserIdAndUserCropSeqAndStatusCd(userId, userCropSeq, "N");
    }

    @Override
    public List<UserCropDto> list(Map<String, Object> param) {
        //return userCropMapper.list(param);
        return userCropRepository.list(param);
    }

    @Override
    public List<UserCropDto> listByUserId(Map<String, Object> param) {
        return userCropMapper.listByUserId(param);
    }

    @Override
    public List<UserCropDto> list(String userId, String year, String exprYN) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("year", year);
        param.put("exprYN", exprYN);
        return userCropMapper.list(param);
    }

    @Override
    public List<UserCropDto> listSort(String userId, String year, String orderBy, String exprYN) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("year", year);
        param.put("exprYN", exprYN);
        param.put("orderBy", orderBy);

        return userCropMapper.list(param);
    }

//    @Override
//    public Page<UserCropDto> page(PagingParam pagingParam) {
//        Page<UserCropDto> page = new Page<UserCropDto>();
//        List<UserCropDto> list = userCropMapper.page(pagingParam);
//        int count = userCropMapper.count(pagingParam);
//
//        page.setItems(list);
//        page.setTotalPages((int) Math.ceil(count / (double) pagingParam.getLimit()));
//        page.setCurrPage(pagingParam.getPage());
//        page.setCount(count);
//
//        return page;
//    }

    @Override
    public DataTablesResponse<UserCropDto> page(Map<String, Object> search, Pageable pageable) {
        Page<UserCropDto> dtoPage = userCropRepository.page(search, pageable);
        return DataTablesResponse.of(dtoPage);
    }

    @Override
    public void update(UserCropDto dto) {
        if (dto.getYear() == null || dto.getYear().isEmpty()) {
            dto.setYear("1900");
        }
        //userCropMapper.update(dto);
        UserCrop userCrop = getEntity(dto.getUserCropSeq());
        dto.setUpdateDtm(new Date());

        modelMapper.map(dto, userCrop);
        userCropRepository.save(userCrop);
    }

    @Override
    public void updateExpr(UserCropDto dto) {
        userCropMapper.updateExpr(dto);
    }

    @Override
    public int delete(UserCropDto dto) {
        int cnt = userCropMapper.delete(dto);
        return cnt;
    }

    @Override
    public int delete(String userId, Long userCropSeq) {
        UserCrop userCrop = userCropRepository.findByUserCropSeqAndUserIdAndStatusCd(userCropSeq, userId, "N");
        if(userCrop == null) {
            return 0;
        }
        userCrop.setUpdateDtm(new Date());
        userCrop.setStatusCd("D");
        userCropRepository.save(userCrop);
        return 1;
    }

    @Override
    public int delete(String userId, Long[] userCropSeqs) {
        int cnt = 0;
        UserCropDto dto = new UserCropDto();

        dto.setUserId(userId);

        for (Long userCropSeq : userCropSeqs) {
            dto.setUserCropSeq(userCropSeq);
            cnt += delete(dto);
        }

        return cnt;
    }

    @Override
    public void deleteByUserId(String userId) {
        //int cnt = userCropMapper.deleteByUserId(userId);
        userCropRepository.deleteByUserId(userId);
    }

}
