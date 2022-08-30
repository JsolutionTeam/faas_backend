package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.UserManureStockDto;

@Mapper
public interface UserManureStockMapper {

    void insert(UserManureStockDto vo);

    List<UserManureStockDto> page(@Param("search") Map<String, Object> search, @Param("pageable") Pageable pageable);

    List<UserManureStockDto> list(@Param("search") Map<String, Object> search);

    List<UserManureStockDto> listByUserDiarySeq(@Param("userId") String userId, @Param("userDiarySeq") Long userDiarySeq);

    List<UserManureStockDto> listByUserInoutSeq(@Param("userId") String userId, @Param("userInoutSeq") Long userInoutSeq);

    UserManureStockDto get(@Param("userId") String userId, @Param("userManureStockSeq") Long userManureStockSeq);

    int count(@Param("search") Map<String, Object> search);

    Long countUsed(@Param("userId") String userId, @Param("userManureSeq") Long userManureSeq);

    void update(UserManureStockDto vo);

    void updateBy(UserManureStockDto vo);

    void delete(@Param("userId") String userId, @Param("userManureStockSeq") Long userManureStockSeq);

    void deleteByUserDiarySeq(@Param("userId") String userId, @Param("userDiarySeq") Long userDiarySeq);

    void deleteByUserInoutSeq(@Param("userId") String userId, @Param("userInoutSeq") Long userInoutSeq);

    void deleteByUserManureSeq(@Param("userId") String userId, @Param("userManureSeq") Long userManureSeq);

    void deleteByUserId(@Param("userId") String userId);


}
