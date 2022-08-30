package zinsoft.faas.service;

import java.io.IOException;
import java.util.List;

import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.dto.UserInoutFileDto;

public interface UserInoutFileService {

    void insert(UserInoutFileDto dto);

    void insert(UserInoutDto userInoutDto) throws IllegalStateException, IOException;

    List<UserInoutFileDto> list(UserInoutDto userInoutDto);

    void update(UserInoutDto userInoutDto) throws IllegalStateException, IOException;

    void deleteMultiple(String userId, Long userInoutSeq, List<Long> fileSeqList);

    void deleteByUserInoutSeq(String userId, Long userInoutSeq);

    void deleteByUserInoutSeqs(String userId, Long[] userInoutSeqs);

    void deleteByUserId(String userId);

}
