package zinsoft.faas.service.impl;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zinsoft.faas.dto.UserInoutDto;
import zinsoft.faas.dto.UserInoutFileDto;
import zinsoft.faas.entity.UserInoutFile;
import zinsoft.faas.repository.UserInoutFileRepository;
import zinsoft.faas.service.UserInoutFileService;
import zinsoft.web.common.service.FileInfoService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserInoutFileServiceImpl extends EgovAbstractServiceImpl implements UserInoutFileService {

    @Resource
    UserInoutFileRepository userInoutFileRepository;

    @Autowired
    ModelMapper modelMapper;

    @Resource
    FileInfoService fileInfoService;

    @Override
    public void insert(UserInoutFileDto dto) {
        UserInoutFile userInoutFile = modelMapper.map(dto, UserInoutFile.class);
        userInoutFileRepository.save(userInoutFile);
    }

    @Override
    public void insert(UserInoutDto userInoutDto) throws IllegalStateException, IOException {
        UserInoutFileDto dto = new UserInoutFileDto(userInoutDto);
        String userId = userInoutDto.getUserId();
        List<MultipartFile> mfList = userInoutDto.getImg();

        if (mfList != null && mfList.size() > 0) {
            List<Long> fileSeqList = fileInfoService.insert(userId, mfList);
            for (Long fileSeq : fileSeqList) {
                dto.setFileSeq(fileSeq);
                insert(dto);
            }
        }
    }

    @Override
    public List<UserInoutFileDto> list(UserInoutDto userInoutDto) {
        Map<String, Object> search = new HashMap<>();
        search.put("userId", userInoutDto.getUserId());
        search.put("userInoutSeq", userInoutDto.getUserInoutSeq());
        return userInoutFileRepository.list(search);
    }

    @Override
    public void update(UserInoutDto userInoutDto) throws IllegalStateException, IOException {
        Long userInoutSeq = userInoutDto.getUserInoutSeq();
        String userId = userInoutDto.getUserId();
        List<Long> fileSeqList = new ArrayList<Long>();
        List<Long> deleteList = null;

        // 삭제할 파일 삭제 ----------------------------------------------------
        deleteList = userInoutDto.getDeleteImgFileSeqs();
        if (deleteList != null && deleteList.size() > 0) {
            fileSeqList.addAll(deleteList);
        }

        if (fileSeqList.size() > 0) {
            deleteMultiple(userId, userInoutSeq, fileSeqList);
        }

        // 추가할 파일 ---------------------------------------------------------
        insert(userInoutDto);
    }

    @Override
    public void deleteMultiple(String userId, Long userInoutSeq, List<Long> fileSeqList) {
        userInoutFileRepository.deleteMultiple(userId, userInoutSeq, fileSeqList);
        fileInfoService.delete(fileSeqList);
    }

    @Override
    public void deleteByUserInoutSeq(String userId, Long userInoutSeq) {
        Map<String, Object> search = new HashMap<>();
        search.put("userId", userId);
        search.put("userInoutSeq", userInoutSeq);
        List<UserInoutFileDto> userInoutFileDtoes = userInoutFileRepository.list(search);
        List<Long> fileSeqList = userInoutFileDtoes.stream().map(map -> map.getFileSeq()).collect(Collectors.toList());

        userInoutFileRepository.deleteByUserInoutSeq(userId, userInoutSeq);
        fileInfoService.delete(fileSeqList);
    }

    @Override
    public void deleteByUserInoutSeqs(String userId, Long[] userInoutSeqs) {
        for (Long userInoutSeq : userInoutSeqs) {
            deleteByUserInoutSeq(userId, userInoutSeq);
        }
    }

    @Override
    public void deleteByUserId(String userId) {

        Map<String, Object> search = new HashMap<>();
        search.put("userId", userId);
        List<UserInoutFileDto> userInoutFileDtoes = userInoutFileRepository.list(search);
        List<Long> fileSeqList = userInoutFileDtoes.stream().map(map -> map.getFileSeq()).collect(Collectors.toList());

        userInoutFileRepository.deleteByUserId(userId);
        fileInfoService.delete(fileSeqList);
    }

}
