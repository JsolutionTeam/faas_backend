package zinsoft.web.common.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.util.BoardUtil;
import zinsoft.util.CommonUtil;
import zinsoft.util.Constants;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.BoardArticleDto;
import zinsoft.web.common.dto.BoardCommentDto;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.entity.BoardComment;
import zinsoft.web.common.repository.BoardCommentRepository;
import zinsoft.web.common.service.BoardArticleService;
import zinsoft.web.common.service.BoardCommentService;
import zinsoft.web.exception.CodeMessageException;

@Service("boardCommentService")
@Transactional
public class BoardCommentServiceImpl extends EgovAbstractServiceImpl implements BoardCommentService {

    //private Type dtoListType = new TypeToken<List<BoardCommentDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    BoardCommentRepository boardCommentRepository;

    @Resource
    BoardArticleService boardArticleService;

    @Override
    public void insert(BoardCommentDto dto) {
        Long articleSeq = dto.getArticleSeq();

        if (dto.getUpCommentSeq() != null && dto.getUpCommentSeq() > 0) {
            BoardComment parent = getEntity(new BoardCommentDto(dto.getUpCommentSeq(), dto.getBoardId(), articleSeq));
            Short pDepth = parent.getDepth();
            Integer pListOrder = parent.getListOrder();
            Integer listOrder = null;
            List<BoardCommentDto> dtoList = list(articleSeq, pListOrder);

            for (BoardCommentDto bcDto : dtoList) {
                if (pDepth >= bcDto.getDepth()) {
                    listOrder = bcDto.getListOrder();
                    break;
                }
            }

            if (listOrder == null) {
                listOrder = boardCommentRepository.nextListOrder(dto.getArticleSeq());
            }

            boardCommentRepository.updateListOrder(articleSeq, listOrder);
            dto.setListOrder(listOrder);
            dto.setDepth((short) (pDepth + 1));
        } else {
            dto.setListOrder(boardCommentRepository.nextListOrder(dto.getArticleSeq()));
            dto.setDepth((short) 0);
        }

        if (StringUtils.isNotBlank(dto.getUserPwd())) {
            dto.setUserPwd(CommonUtil.sha256(dto.getUserPwd()));
        }

        BoardComment comment = modelMapper.map(dto, BoardComment.class);
        boardCommentRepository.save(comment);
        dto.setCommentSeq(comment.getCommentSeq());

        boardArticleService.updateCommentCnt(articleSeq);
    }

    @Override
    public List<BoardCommentDto> list(Long articleSeq) {
        return list(articleSeq, null);
    }

    @Override
    public List<BoardCommentDto> list(Long articleSeq, Integer listOrder) {
        return boardCommentRepository.list(articleSeq, listOrder);
    }

    @Override
    public BoardCommentDto get(BoardCommentDto dto) {
        BoardComment comment = getEntity(dto);
        return modelMapper.map(comment, BoardCommentDto.class);
    }

    @Override
    public boolean checkPwd(BoardCommentDto dto) {
        BoardComment comment = getEntity(dto);
        return StringUtils.isNotBlank(comment.getUserPwd()) && comment.getUserPwd().equals(CommonUtil.sha256(dto.getUserPwd()));
    }

    @Override
    public void update(BoardCommentDto dto) {
        BoardComment comment = getEntity(dto);
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        String userId = userInfo != null ? userInfo.getUserId() : null;

        // 게시물 상세보기 권한이 있는 경우 댓글도 허용
        if (!(BoardUtil.isPermitRole(dto.getBoardId(), BoardUtil.BoardAct.DETAIL) // role 확인
                || StringUtils.isNotBlank(comment.getUserId()) && comment.getUserId().equals(userId))) { // 본인인지 확인
            throw new CodeMessageException(Result.FORBIDDEN);
        } else if (!comment.getUserPwd().equals(CommonUtil.sha256(dto.getUserPwd()))) { // 비회원 댓글 비밀번호 확인
            throw new CodeMessageException(Result.PASSWORD_MISMATCH);
        }

        comment.setUpdateDtm(new Date());
        comment.setContent(dto.getContent());
    }

    @Override
    public void delete(BoardCommentDto dto) {
        BoardComment comment = getEntity(dto);
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        String userId = userInfo != null ? userInfo.getUserId() : null;

        // 게시물 상세보기 권한이 있는 경우 댓글도 허용
        if (!(BoardUtil.isPermitRole(dto.getBoardId(), BoardUtil.BoardAct.DETAIL) // role 확인
                || StringUtils.isNotBlank(comment.getUserId()) && comment.getUserId().equals(userId))) { // 본인인지 확인
            throw new CodeMessageException(Result.FORBIDDEN);
        } else if (!comment.getUserPwd().equals(CommonUtil.sha256(dto.getUserPwd()))) { // 비회원 댓글 비밀번호 확인
            throw new CodeMessageException(Result.PASSWORD_MISMATCH);
        }

        comment.setUpdateDtm(new Date());
        comment.setStatusCd(Constants.STATUS_CD_DELETE);
        boardArticleService.updateCommentCnt(dto.getArticleSeq());
    }

    @Override
    public void delete(BoardCommentDto dto, Long[] commentSeqs) {
        for (Long commentSeq : commentSeqs) {
            dto.setCommentSeq(commentSeq);
            delete(dto);
        }
    }

    @Override
    public void delete(BoardArticleDto articleDto) {
        boardCommentRepository.delete(articleDto);
    }

    @Override
    public void fill(BoardArticleDto articleDto) {
        if (articleDto != null && articleDto.getCommentCnt() > 0) {
            articleDto.setCommentList(list(articleDto.getArticleSeq()));
        }
    }

    @Override
    public void fill(List<BoardArticleDto> articleDtoList) {
        if (articleDtoList != null) {
            for (BoardArticleDto articleDto : articleDtoList) {
                if (articleDto.getCommentCnt() > 0) {
                    articleDto.setCommentList(list(articleDto.getArticleSeq()));
                }
            }
        }
    }

    private BoardComment getEntity(BoardCommentDto dto) {
        BoardComment entity = null;

        if (dto.getArticleSeq() == null) {
            entity = boardCommentRepository.findByCommentSeqAndBoardIdAndStatusCd(dto.getCommentSeq(), dto.getBoardId(), Constants.STATUS_CD_NORMAL);
        } else {
            entity = boardCommentRepository.findByCommentSeqAndBoardIdAndArticleSeqAndStatusCd(dto.getCommentSeq(), dto.getBoardId(), dto.getArticleSeq(), Constants.STATUS_CD_NORMAL);
        }

        if (entity == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return entity;
    }

}
