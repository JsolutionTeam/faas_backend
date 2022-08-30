package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.UserChemicalStockDto;

@Mapper
public interface UserChemicalStockMapper {

    void insert(UserChemicalStockDto vo);

    List<UserChemicalStockDto> list(@Param("search") Map<String, Object> search);

    List<UserChemicalStockDto> page(@Param("search") Map<String, Object> search, @Param("pageable") Pageable pageable);

    List<UserChemicalStockDto> listByUserDiarySeq(@Param("userId") String userId, @Param("userDiarySeq") Long userDiarySeq);

    List<UserChemicalStockDto> listByUserInoutSeq(@Param("userId") String userId, @Param("userInoutSeq") Long userInoutSeq);

    UserChemicalStockDto get(@Param("userId") String userId, @Param("userChemicalStockSeq") Long userChemicalStockSeq);

    int count(@Param("search") Map<String, Object> search);

    Long countUsed(@Param("userId") String userId, @Param("userChemicalSeq") Long userChemicalSeq);

    void update(UserChemicalStockDto vo);

    void updateBy(UserChemicalStockDto vo);

    void delete(@Param("userId") String userId, @Param("userChemicalStockSeq") Long userChemicalStockSeq);

    void deleteByUserDiarySeq(@Param("userId") String userId, @Param("userDiarySeq") Long userDiarySeq);

    void deleteByUserInoutSeq(@Param("userId") String userId, @Param("userInoutSeq") Long userInoutSeq);

    void deleteByUserChemicalSeq(@Param("userId") String userId, @Param("userChemicalSeq") Long userChemicalSeq);

    void deleteByUserId(@Param("userId") String userId);

}
