package zinsoft.faas.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.UserDiaryFileDto;
import zinsoft.web.common.dto.FileInfoDto;

@Mapper
public interface UserDiaryFileMapper {

    void insert(UserDiaryFileDto vo);

    List<FileInfoDto> listFileInfo(@Param("userId") String userId, @Param("userDiarySeq") Long userDiarySeq, @Param("fileKCd") String fileKCd);

    List<Long> listFileSeqByUserDiarySeq(@Param("userId") String userId, @Param("userDiarySeq") Long userDiarySeq);

    List<Long> listFileSeqByUserId(String userId);

    void delete(@Param("userId") String userId, @Param("userDiarySeq") Long userDiarySeq, @Param("fileSeq") Long fileSeq);

    void deleteMultiple(@Param("userId") String userId, @Param("userDiarySeq") Long userDiarySeq, @Param("fileSeqList") List<Long> fileSeqList);

    void deleteByUserDiarySeq(@Param("userId") String userId, @Param("userDiarySeq") Long userDiarySeq);

    void deleteByUserId(@Param("userId") String userId);

}
