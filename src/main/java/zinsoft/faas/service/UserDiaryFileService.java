package zinsoft.faas.service;

import java.io.IOException;
import java.util.List;

import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserDiaryFileDto;

public interface UserDiaryFileService {

    public void insert(UserDiaryFileDto dto);

    public void insert(UserDiaryDto userDiaryDto) throws IllegalStateException, IOException;

    public List<UserDiaryFileDto> list(UserDiaryDto userDiaryDto, String fileKCd) ;

    public void update(UserDiaryDto userDiaryDto) throws IllegalStateException, IOException;

    public void deleteMultiple(String userId, Long userDiarySeq, List<Long> fileSeqList);

    public void deleteByUserDiarySeq(String userId, Long userDiarySeq);

    public void deleteByUserDiarySeqs(String userId, Long[] userDiarySeqs) ;

    public void deleteByUserId(String userId);

}
