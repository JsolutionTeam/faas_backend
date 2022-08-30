package zinsoft.web.common.service.impl;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.util.Result;
import zinsoft.web.common.dto.BoardArticleDto;
import zinsoft.web.common.dto.BoardFileDto;
import zinsoft.web.common.entity.BoardFile;
import zinsoft.web.common.repository.BoardFileRepository;
import zinsoft.web.common.service.BoardFileService;
import zinsoft.web.common.service.FileInfoService;
import zinsoft.web.exception.CodeMessageException;

@Service("boardFileService")
@Transactional
public class BoardFileServiceImpl extends EgovAbstractServiceImpl implements BoardFileService {

    //private Type dtoListType = new TypeToken<List<BoardFileDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    BoardFileRepository boardFileRepository;

    @Resource
    FileInfoService fileInfoService;

    @Override
    public int insert(BoardArticleDto articleDto) throws IllegalStateException, IOException {
        String boardId = articleDto.getBoardId();
        Long articleSeq = articleDto.getArticleSeq();
        String userId = articleDto.getUserId();
        List<MultipartFile> mfList = articleDto.getFiles();
        int cnt = 0;

        if (mfList != null && !mfList.isEmpty()) {
            List<Long> fileSeqList = fileInfoService.insert(userId, mfList);
            BoardFile boardFile = new BoardFile(boardId, articleSeq);

            for (Long fileSeq : fileSeqList) {
                boardFile.setFileSeq(fileSeq);
                boardFileRepository.save(boardFile);
                cnt++;
            }
        }

        return cnt;
    }

    @Override
    public List<BoardFileDto> list(Long articleSeq) {
        return boardFileRepository.list(articleSeq);
    }

    @Override
    public BoardFileDto get(Long fileSeq) {
        BoardFileDto dto = boardFileRepository.get(fileSeq);

        if (dto == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return dto;
    }

    @Override
    public int update(BoardArticleDto articleDto) throws IllegalStateException, IOException {
        List<Long> delFileSeqList = articleDto.getDelFiles();
        int cnt = 0;

        if (delFileSeqList != null && !delFileSeqList.isEmpty()) {
            cnt -= (int) boardFileRepository.delete(articleDto.getArticleSeq(), delFileSeqList);
            fileInfoService.delete(delFileSeqList);
        }

        cnt += insert(articleDto);

        return cnt;
    }

    @Override
    public void delete(BoardArticleDto articleDto) {
        Long articleSeq = articleDto.getArticleSeq();
        List<Long> fileSeqList = boardFileRepository.findFileSeqByArticleSeq(articleSeq);

        fileInfoService.delete(fileSeqList);
        boardFileRepository.delete(articleSeq, fileSeqList);
    }

    @Override
    public void fill(BoardArticleDto articleDto) {
        if (articleDto != null && articleDto.getFileCnt() > 0) {
            articleDto.setFileList(list(articleDto.getArticleSeq()));
        }
    }

}
