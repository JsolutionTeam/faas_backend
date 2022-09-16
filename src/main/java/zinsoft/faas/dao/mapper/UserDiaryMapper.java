package zinsoft.faas.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.vo.UserDiaryAggregate;

@Mapper
public interface UserDiaryMapper {

    void insert(UserDiaryDto vo);

    void copy(@Param("userId") String userId, @Param("srcYear") String srcYear, @Param("dstYear") String dstYear);

//    UserDiaryDto get(UserDiaryDto vo);

    List<UserDiaryDto> list(@Param("search") Map<String, Object> search);

    List<UserDiaryDto> pageData(@Param("search") Map<String, Object> search, @Param("pageable") Pageable pageable);

    int count(@Param("search") Map<String, Object> search);

    List<String> listYear(@Param("userId") String userId, @Param("diaryTCd") String diaryTCd, @Param("year") String year);

    int countUsedCrop(@Param("userId") String userId, @Param("userCropSeq") Long userCropSeq);

    List<UserDiaryAggregate> getAggregate(Map<String, Object> param);

    void update(UserDiaryDto vo);

    int delete(UserDiaryDto vo);

    int deleteMultiple(@Param("userId") String userId, @Param("userDiarySeqs") Long[] userDiarySeqs);

    int deleteByUserId(String userId);

}
