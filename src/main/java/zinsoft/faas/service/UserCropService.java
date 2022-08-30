package zinsoft.faas.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserCropDto;
import zinsoft.util.DataTablesResponse;

public interface UserCropService {

    void insert(UserCropDto dto);

    int insertCopy(String workerId, String userId);

    UserCropDto get(UserCropDto dto);

    UserCropDto get(String userId, Long cropSeq);

    UserCropDto get(String userId, Long cropSeq, Long userCropSeq);

    Long getCropSeqByCropNm(String userId, String cropNm);

    int countByAliasNm(Long userCropSeq, String userId, String aliasNm);

    long countByAliasNm(UserCropDto dto);

    boolean isExistUserCropId(String userId, Long userCropSeq);

    List<UserCropDto> list(Map<String, Object> param);

    List<UserCropDto> listByUserId(Map<String, Object> param);

    List<UserCropDto> list(String userId, String year, String exprYN);

    List<UserCropDto> listSort(String userId, String year, String orderBy, String exprYN);

    //Page<UserCropDto> page(PagingParam pagingParam);

    DataTablesResponse<UserCropDto> page(Map<String, Object> search, Pageable pageable);

    void update(UserCropDto dto);

    void updateExpr(UserCropDto dto);

    int delete(UserCropDto dto);

    int delete(String userId, Long userCropSeq);

    int delete(String userId, Long[] userCropSeqs);

    void deleteByUserId(String userId);

}