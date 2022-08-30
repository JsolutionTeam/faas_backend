package zinsoft.web.common.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import zinsoft.web.common.dto.FileInfoDto;

public interface FileInfoService {

    Long insert(FileInfoDto dto);

    Long insert(String userId, MultipartFile mf) throws IllegalStateException, IOException;

    List<Long> insert(String userId, List<MultipartFile> mfList) throws IllegalStateException, IOException;

    List<FileInfoDto> list(List<Long> fileSeqList);

    FileInfoDto get(Long fileSeq);

    void update(String userId, Long fileSeq, MultipartFile mf) throws IllegalStateException, IOException;

    void delete(Long fileSeq);

    void delete(List<Long> fileSeqs);

}
