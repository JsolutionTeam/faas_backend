package zinsoft.web.common.service.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import lombok.extern.slf4j.Slf4j;
import zinsoft.util.AppPropertyUtil;
import zinsoft.util.BoardUtil;
import zinsoft.util.CommonUtil;
import zinsoft.util.Constants;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.BoardArticleDto;
import zinsoft.web.common.dto.BoardDto;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.entity.BoardArticle;
import zinsoft.web.common.repository.BoardArticleRepository;
import zinsoft.web.common.service.BoardArticleService;
import zinsoft.web.common.service.BoardCommentService;
import zinsoft.web.common.service.BoardFileService;
import zinsoft.web.common.service.EmailService;
import zinsoft.web.exception.CodeMessageException;

@Service("boardArticleService")
@Transactional
@Slf4j
public class BoardArticleServiceImpl extends EgovAbstractServiceImpl implements BoardArticleService {

    private Type dtoListType = new TypeToken<List<BoardArticleDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    BoardArticleRepository boardArticleRepository;

    @Resource
    BoardFileService boardFileService;

    @Resource
    BoardCommentService boardCommentService;

    @Resource
    EmailService emailService;

    @Override
    public void insert(BoardArticleDto dto, String baseUri) throws IllegalStateException, IOException {
        if (dto.getNoticeYn() == null || !Constants.YN_YES.equals(dto.getNoticeYn())) {
            dto.setNoticeYn(Constants.YN_NO);
        }

        if (StringUtils.isNotBlank(dto.getUserPwd())) {
            dto.setUserPwd(CommonUtil.sha256(dto.getUserPwd()));
        }

        List<MultipartFile> mfList = dto.getFiles();
        int fileCnt = 0;

        if (mfList != null && !mfList.isEmpty()) {
            for (MultipartFile mf : mfList) {
                if (mf != null && !mf.isEmpty()) {
                    fileCnt++;
                }
            }
        }

        dto.setFileCnt(fileCnt);

        BoardArticle article = modelMapper.map(dto, BoardArticle.class);
        boardArticleRepository.save(article);
        dto.setArticleSeq(article.getArticleSeq());
        boardFileService.insert(dto);

        BoardDto boardDto = BoardUtil.getBoard(dto.getBoardId());
        if (Constants.YN_YES.equals(boardDto.getPushYn())) {
            String pushEmails = boardDto.getPushEmails();
            if (pushEmails != null && !pushEmails.isEmpty()) {
                emailService.send(boardDto.getPushEmails(), "new_article", boardDto.getBoardNm() + AppPropertyUtil.get(Constants.MAIL_SUBJECT_NEW_ARTICLE), dto, baseUri);
            }
        }
    }

    @Override
    public DataTablesResponse<BoardArticleDto> page(Map<String, Object> search, Pageable pageable) {
        Page<BoardArticleDto> dtoPage = null;
        List<BoardArticleDto> noticeDtoList = null;
        boolean onlyNotice = Constants.YN_YES.equals(search.get(Constants.BOARD_ONLY_NOTICE));
        boolean withNotice = search.get(Constants.BOARD_WITH_NOTICE) == null || Constants.YN_YES.equals(search.get(Constants.BOARD_WITH_NOTICE));
        boolean withComment = Constants.YN_YES.equals(search.get(Constants.BOARD_WITH_COMMENT));

        if (pageable.getOffset() == 0 && withNotice || onlyNotice) {
            List<BoardArticle> list = boardArticleRepository.findByBoardIdAndStatusCdAndNoticeYn((String) search.get("boardId"), Constants.STATUS_CD_NORMAL, Constants.YN_YES);

            noticeDtoList = modelMapper.map(list, dtoListType);

            if (onlyNotice) {
                dtoPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
            }
        }

        if (!onlyNotice) {
            dtoPage = boardArticleRepository.page(search, pageable);
        }

        if (withComment) {
            boardCommentService.fill(dtoPage.getContent());

            if (noticeDtoList != null) {
                boardCommentService.fill(noticeDtoList);
            }
        }

        return DataTablesResponse.of(dtoPage, noticeDtoList);
    }

    @Override
    public BoardArticleDto get(BoardArticleDto dto, String withComment) {
        return get(dto, withComment, null);
    }

    @Override
    public BoardArticleDto get(BoardArticleDto dto, String withComment, String updateReadCnt) {
        BoardArticle article = boardArticleRepository.findByArticleSeqAndBoardIdAndStatusCd(dto.getArticleSeq(), dto.getBoardId(), Constants.STATUS_CD_NORMAL);

        if (article == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        BoardArticleDto articleDto = modelMapper.map(article, BoardArticleDto.class);

        boardFileService.fill(articleDto);

        if (withComment == null || Constants.YN_YES.equals(withComment)) {
            boardCommentService.fill(articleDto);
        }

        if (updateReadCnt == null || Constants.YN_YES.equals(updateReadCnt)) {
            boardArticleRepository.increaseReadCnt(dto.getArticleSeq());
            articleDto.setReadCnt(articleDto.getReadCnt() + 1);
        }

        return articleDto;
    }

    @Override
    public boolean checkPwd(BoardArticleDto dto) {
        BoardArticle article = getEntity(dto);
        return StringUtils.isNotBlank(article.getUserPwd()) && article.getUserPwd().equals(CommonUtil.sha256(dto.getUserPwd()));
    }

    @Override
    public void update(BoardArticleDto dto) throws IllegalStateException, IOException {
        BoardArticle article = getEntity(dto);
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        String userId = userInfo != null ? userInfo.getUserId() : null;

        if (!(BoardUtil.isPermitRole(dto.getBoardId(), BoardUtil.BoardAct.UPDATE) // role 확인
                || StringUtils.isNotBlank(article.getUserId()) && article.getUserId().equals(userId))) { // 본인 게시물인지 확인
            throw new CodeMessageException(Result.FORBIDDEN);
        } else if (!article.getUserPwd().equals(CommonUtil.sha256(dto.getUserPwd()))) { // 비회원 게시물 비밀번호 확인
            throw new CodeMessageException(Result.PASSWORD_MISMATCH);
        }

        if (!UserInfoUtil.isAdmin(userInfo) || dto.getNoticeYn() == null || !Constants.YN_YES.equals(dto.getNoticeYn())) {
            dto.setNoticeYn(Constants.YN_NO);
        }

        int fileCnt = boardFileService.update(dto);
        dto.setFileCnt(article.getFileCnt() + fileCnt);

        dto.setRegDtm(null);
        dto.setUpdateDtm(new Date());
        dto.setStatusCd(null);
        dto.setBoardId(null);
        dto.setUserId(userId);
        dto.setUserNm(null);
        dto.setUserPwd(null);
        dto.setEmailAddr(null);
        dto.setCommentCnt(null);
        dto.setReadCnt(null);
        dto.setAssentCnt(null);
        dto.setDissentCnt(null);

        modelMapper.map(dto, article);
        //boardArticleRepository.save(article);
    }

    @Override
    public void updateCommentCnt(Long articleSeq) {
        boardArticleRepository.updateCommentCnt(articleSeq);
    }

    @Override
    public void delete(BoardArticleDto dto) {
        BoardArticle article = getEntity(dto);
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        String userId = userInfo != null ? userInfo.getUserId() : null;

        if (!(BoardUtil.isPermitRole(dto.getBoardId(), BoardUtil.BoardAct.DELETE) // role 확인
                || StringUtils.isNotBlank(article.getUserId()) && article.getUserId().equals(userId))) { // 본인 게시물인지 확인
            throw new CodeMessageException(Result.FORBIDDEN);
        } else if (!article.getUserPwd().equals(CommonUtil.sha256(dto.getUserPwd()))) { // 비회원 게시물 비밀번호 확인
            throw new CodeMessageException(Result.PASSWORD_MISMATCH);
        }

        article.setUpdateDtm(new Date());
        article.setStatusCd(Constants.STATUS_CD_DELETE);

        try {
            boardCommentService.delete(dto);
            boardFileService.delete(dto);
        } catch (Exception e) {
            log.warn("BoardArticleServiceImpl.delete", e);
        }
    }

    @Override
    public void delete(BoardArticleDto dto, Long[] articleSeqs) {
        for (Long articleSeq : articleSeqs) {
            dto.setArticleSeq(articleSeq);
            delete(dto);
        }
    }

    private BoardArticle getEntity(BoardArticleDto dto) {
        BoardArticle entity = boardArticleRepository.findByArticleSeqAndBoardIdAndStatusCd(dto.getArticleSeq(), dto.getBoardId(), Constants.STATUS_CD_NORMAL);

        if (entity == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return entity;
    }

}
