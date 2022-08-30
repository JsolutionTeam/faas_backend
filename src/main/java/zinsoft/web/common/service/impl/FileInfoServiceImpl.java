package zinsoft.web.common.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import zinsoft.util.AppPropertyUtil;
import zinsoft.util.CommonUtil;
import zinsoft.util.Constants;
import zinsoft.util.Result;
import zinsoft.web.common.dto.BoardDto;
import zinsoft.web.common.dto.FileInfoDto;
import zinsoft.web.common.entity.FileInfo;
import zinsoft.web.common.repository.FileInfoRepository;
import zinsoft.web.common.service.FileInfoService;
import zinsoft.web.exception.CodeMessageException;

@Service("fileInfoService")
@Transactional
@Slf4j
public class FileInfoServiceImpl extends EgovAbstractServiceImpl implements FileInfoService {

    private int imageWidth = 800;

    private Type dtoListType = new TypeToken<List<BoardDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    FileInfoRepository fileInfoRepository;

    @PostConstruct
    public void init() {
        String defaultImageWidth = AppPropertyUtil.get(Constants.IMAGE_THUMB_WIDTH);

        try {
            this.imageWidth = Integer.parseInt(defaultImageWidth, 10);
        } catch (Exception e) {
            this.imageWidth = 800;
        }
    }

    @Override
    public Long insert(FileInfoDto dto) {
        FileInfo fileInfo = modelMapper.map(dto, FileInfo.class);
        fileInfoRepository.save(fileInfo);
        return fileInfo.getFileSeq();
    }

    @Override
    public Long insert(String userId, MultipartFile mf) throws IllegalStateException, IOException {
        if (mf == null || mf.isEmpty()) {
            return null;
        }

        String contentType = mf.getContentType();
        File file = CommonUtil.getUploadFile();

        mf.transferTo(file);
        createThumbnail(contentType, file);

        FileInfoDto dto = new FileInfoDto();
        dto.setUserId(userId);
        dto.setSavedNm(CommonUtil.getUploadFilePath(file.getName()) + File.separator + file.getName());
        dto.setFileSize(mf.getSize());
        dto.setFileNm(mf.getOriginalFilename());
        dto.setContentType(contentType);

        return insert(dto);
    }

    @Override
    public List<Long> insert(String userId, List<MultipartFile> mfList) throws IllegalStateException, IOException {
        if (mfList == null || mfList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> fileSeqList = new ArrayList<>(mfList.size());
        Long fileSeq = null;

        for (MultipartFile mf : mfList) {
            fileSeq = insert(userId, mf);
            if (fileSeq != null) {
                fileSeqList.add(fileSeq);
            }
        }

        return fileSeqList;
    }

    @Override
    public List<FileInfoDto> list(List<Long> fileSeqList) {
        List<FileInfo> list = fileInfoRepository.findByFileSeqInAndStatusCd(fileSeqList, Constants.STATUS_CD_NORMAL);
        return modelMapper.map(list, dtoListType);
    }

    @Override
    public FileInfoDto get(Long fileSeq) {
        FileInfo fileInfo = getEntity(fileSeq);
        return modelMapper.map(fileInfo, FileInfoDto.class);
    }

    @Override
    public void update(String userId, Long fileSeq, MultipartFile mf) throws IllegalStateException, IOException {
        if (mf == null || mf.isEmpty()) {
            return;
        }

        FileInfo fileInfo = getEntity(fileSeq);
        String contentType = mf.getContentType();
        File file = new File(AppPropertyUtil.get(Constants.UPLOAD_DIR) + fileInfo.getSavedNm());

        new File(file.getParent()).mkdirs();
        mf.transferTo(file);
        createThumbnail(contentType, file);

        fileInfo.setUpdateDtm(new Date());
        fileInfo.setUserId(userId);
        fileInfo.setFileSize(mf.getSize());
        fileInfo.setFileNm(mf.getOriginalFilename());
        fileInfo.setContentType(contentType);
    }

    @Override
    public void delete(Long fileSeq) {
        FileInfo fileInfo = getEntity(fileSeq);

        fileInfo.setUpdateDtm(new Date());
        fileInfo.setStatusCd(Constants.STATUS_CD_DELETE);

        try {
            CommonUtil.deleteFile(AppPropertyUtil.get(Constants.UPLOAD_DIR) + fileInfo.getSavedNm());
        } catch (IOException e) {
            log.warn("FileInfoServiceImpl.delete", e);
        }
    }

    @Override
    public void delete(List<Long> fileSeqs) {
        for (Long fileId : fileSeqs) {
            delete(fileId);
        }
    }

    private FileInfo getEntity(Long fileSeq) {
        FileInfo entity = fileInfoRepository.findByFileSeqAndStatusCd(fileSeq, Constants.STATUS_CD_NORMAL);

        if (entity == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return entity;
    }

    private void createThumbnail(String contentType, File file) throws IOException {
        if (contentType != null && contentType.toLowerCase().startsWith("image/")) {
            // Thumbnails.of(f).size(imageWidth, imageWidth).toFile(new File(f.getAbsolutePath() + "s"); // thumbnailator에서 강제로 확장자 붙임
            OutputStream os = new FileOutputStream(new File(file.getAbsolutePath() + "_" + imageWidth));
            Thumbnails.of(file).size(imageWidth, imageWidth).toOutputStream(os);
            os.close();
        }
    }

}
