package zinsoft.faas.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import zinsoft.faas.dto.UserInoutFileDto;
import zinsoft.web.common.dto.FileInfoDto;

@Mapper
public interface UserInoutFileMapper {

    void insert(UserInoutFileDto dto);

    List<FileInfoDto> listFileInfo(@Param("userId") String userId, @Param("userInoutSeq") Long userInoutSeq);

    List<Long> listFileSeqByUserInoutSeq(@Param("userId") String userId, @Param("userInoutSeq") Long userInoutSeq);

    List<Long> listFileSeqByUserId(String userId);

    void delete(@Param("userId") String userId, @Param("userInoutSeq") Long userInoutSeq, @Param("fileSeq") Long fileSeq);

    void deleteMultiple(@Param("userId") String userId, @Param("userInoutSeq") Long userInoutSeq, @Param("fileSeqList") List<Long> fileSeqList);

    void deleteByUserInoutSeq(@Param("userId") String userId, @Param("userInoutSeq") Long userInoutSeq);

    void deleteByUserId(@Param("userId") String userId);

}
