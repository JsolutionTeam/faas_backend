package zinsoft.faas.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dao.mapper.UserDiaryFileMapper;
import zinsoft.faas.dto.UserDiaryDto;
import zinsoft.faas.dto.UserDiaryFileDto;
import zinsoft.faas.entity.UserDiaryFile;
import zinsoft.faas.entity.UserDiaryFileId;
import zinsoft.faas.repository.UserDiaryFileRepository;
import zinsoft.faas.service.UserDiaryFileService;
import zinsoft.util.Result;
import zinsoft.web.common.service.FileInfoService;
import zinsoft.web.exception.CodeMessageException;

@Service
@Transactional
public class UserDiaryFileServiceImpl extends EgovAbstractServiceImpl implements UserDiaryFileService {

    @Resource
    UserDiaryFileMapper userDiaryFileMapper;

    @Resource
    UserDiaryFileRepository userDiaryFileRepository;

    @Autowired
    ModelMapper modelMapper;

    @Resource
    FileInfoService fileInfoService;

    private UserDiaryFile getEntity(UserDiaryFileId id) {
        Optional<UserDiaryFile> data = userDiaryFileRepository.findById(id);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    @Override
    public void insert(UserDiaryFileDto dto) {
        //userDiaryFileMapper.insert(dto);
        UserDiaryFile userDiaryFile = modelMapper.map(dto, UserDiaryFile.class);
        userDiaryFileRepository.save(userDiaryFile);
    }

    @Override
    public void insert(UserDiaryDto userDiaryDto) throws IllegalStateException, IOException {
        UserDiaryFileDto dto = new UserDiaryFileDto(userDiaryDto);
        String userId = userDiaryDto.getUserId();
        List<MultipartFile> mfList = userDiaryDto.getWorking();

        if (mfList != null && mfList.size() > 0) {
            List<Long> fileSeqList = fileInfoService.insert(userId, mfList);
            dto.setFileKCd(UserDiaryFileDto.FILE_K_CD_WORKING);
            for (Long fileSeq : fileSeqList) {
                dto.setFileSeq(fileSeq);
                insert(dto);
            }
        }
    }

    @Override
    public List<UserDiaryFileDto> list(UserDiaryDto userDiaryDto, String fileKCd) {
        Map<String, Object> search = new HashMap<>();
        search.put("userId", userDiaryDto.getUserId());
        search.put("userDiarySeq", userDiaryDto.getUserDiarySeq());
        search.put("fileKCd", fileKCd);
        return userDiaryFileRepository.list(search);
    }

    @Override
    public void update(UserDiaryDto userDiaryDto) throws IllegalStateException, IOException {
        Long userDiarySeq = userDiaryDto.getUserDiarySeq();
        String userId = userDiaryDto.getUserId();
        List<Long> fileSeqList = new ArrayList<Long>();
        List<Long> deleteList = null;

        // 삭제할 파일 삭제 ----------------------------------------------------
        // 작업사진
        deleteList = userDiaryDto.getDeleteWorkingFileSeqs();
        if (deleteList != null && deleteList.size() > 0) {
            fileSeqList.addAll(deleteList);
        }

        if (fileSeqList.size() > 0) {
            deleteMultiple(userId, userDiarySeq, fileSeqList);
        }

        // 추가할 파일 ---------------------------------------------------------
        insert(userDiaryDto);
    }

    @Override
    public void deleteMultiple(String userId, Long userDiarySeq, List<Long> fileSeqList) {
        userDiaryFileRepository.deleteMultiple(userId, userDiarySeq, fileSeqList);
        fileInfoService.delete(fileSeqList);
    }

    @Override
    public void deleteByUserDiarySeq(String userId, Long userDiarySeq) {
        //List<Long> fileSeqList = userDiaryFileMapper.listFileSeqByUserDiarySeq(userId, userDiarySeq);
        Map<String, Object> search = new HashMap<>();
        search.put("userId", userId);
        search.put("userDiarySeq", userDiarySeq);
        List<UserDiaryFileDto> userDiaryFileDtoes = userDiaryFileRepository.list(search);
        List<Long> fileSeqList = userDiaryFileDtoes.stream().map(map -> map.getFileSeq()).collect(Collectors.toList());

        userDiaryFileRepository.deleteByUserDiarySeq(userId, userDiarySeq);
        fileInfoService.delete(fileSeqList);
    }

    @Override
    public void deleteByUserDiarySeqs(String userId, Long[] userDiarySeqs) {
        for (Long userDiarySeq : userDiarySeqs) {
            deleteByUserDiarySeq(userId, userDiarySeq);
        }
    }

    @Override
    public void deleteByUserId(String userId) {
       // List<Long> fileSeqList = userDiaryFileMapper.listFileSeqByUserId(userId);

        Map<String, Object> search = new HashMap<>();
        search.put("userId", userId);
        List<UserDiaryFileDto> userDiaryFileDtoes = userDiaryFileRepository.list(search);
        List<Long> fileSeqList = userDiaryFileDtoes.stream().map(map -> map.getFileSeq()).collect(Collectors.toList());

        userDiaryFileRepository.deleteByUserId(userId);
        fileInfoService.delete(fileSeqList);
    }

}
