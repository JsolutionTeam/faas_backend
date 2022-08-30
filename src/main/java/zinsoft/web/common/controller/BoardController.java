package zinsoft.web.common.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import zinsoft.util.BoardUtil;
import zinsoft.util.CommonUtil;
import zinsoft.util.Constants;
import zinsoft.util.DataTablesParam;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.BoardArticleDto;
import zinsoft.web.common.dto.BoardCategoryDto;
import zinsoft.web.common.dto.BoardCommentDto;
import zinsoft.web.common.dto.UserInfoDto;
import zinsoft.web.common.service.BoardArticleService;
import zinsoft.web.common.service.BoardCategoryService;
import zinsoft.web.common.service.BoardCommentService;
import zinsoft.web.exception.CodeMessageException;

@RestController
@RequestMapping("${api.prefix}")
public class BoardController {

    @Resource
    BoardCategoryService boardCategoryService;

    @Resource
    BoardArticleService boardArticleService;

    @Resource
    BoardCommentService boardCommentService;

    @GetMapping("/boardcat/{boardId}")
    public Result listCat(@PathVariable String boardId, @Valid DataTablesParam dtParam, HttpServletRequest request) {
        if (!BoardUtil.isPermitRole(boardId, BoardUtil.BoardAct.INSERT)
                && !BoardUtil.isPermitRole(boardId, BoardUtil.BoardAct.LIST)) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        List<BoardCategoryDto> list = boardCategoryService.list(boardId);

        return new Result(true, Result.OK, list);
    }

    @PostMapping("/board/{boardId}")
    public Result insertArticle(@Valid BoardArticleDto dto, HttpServletRequest request) throws IllegalStateException, IOException {
        if (!BoardUtil.isPermitRole(dto.getBoardId(), BoardUtil.BoardAct.INSERT)) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        if (!UserInfoUtil.isAdmin(request) && Constants.YN_YES.equals(dto.getNoticeYn())) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        if (userInfo != null) {
            dto.setUserId(userInfo.getUserId());
            dto.setUserNm(userInfo.getUserNm());
            dto.setEmailAddr(userInfo.getUserId());
        }

        if (dto.getUserNm() == null || dto.getUserNm().isEmpty() == true) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        boardArticleService.insert(dto, CommonUtil.getFrontEndBaseUri(request));

        return new Result(true, Result.OK);
    }

    @GetMapping("/board/{boardId}")
    public Result pageArticle(@PathVariable String boardId, @RequestParam Map<String, Object> search, @PageableDefault Pageable pageable, HttpServletRequest request) {
        if (!BoardUtil.isPermitRole(boardId, BoardUtil.BoardAct.LIST)) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        search.put("boardId", boardId);

        DataTablesResponse<BoardArticleDto> page = boardArticleService.page(search, pageable);

        return new Result(true, Result.OK, page);
    }

    @GetMapping("/board/{boardId}/{articleSeq}")
    public Result getArticle(BoardArticleDto dto, String withComment, String updateReadCnt, HttpServletRequest request) {
        if (!BoardUtil.isPermitRole(dto.getBoardId(), BoardUtil.BoardAct.DETAIL)) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        BoardArticleDto articleDto = boardArticleService.get(dto, withComment, updateReadCnt);

        return new Result(true, Result.OK, articleDto);
    }

    @PostMapping("/board/{boardId}/{articleSeq}/check-pwd")
    public Result checkPwd(BoardArticleDto dto, HttpServletRequest request, HttpSession session, Model model) throws Exception {
        boolean isMatch = boardArticleService.checkPwd(dto);

        if (!isMatch) {
            throw new CodeMessageException(Result.PASSWORD_MISMATCH);
        }

        return new Result(true, Result.OK);
    }

    @PutMapping("/board/{boardId}/{articleSeq}")
    public Result updateArticle(@Valid BoardArticleDto dto, HttpServletRequest request) throws IllegalStateException, IOException {
        boardArticleService.update(dto);
        return new Result(true, Result.OK);
    }

    @DeleteMapping("/board/{boardId}/{articleSeq}")
    public Result deleteArticle(BoardArticleDto dto, HttpServletRequest request) {
        boardArticleService.delete(dto);
        return new Result(true, Result.OK);
    }

    @DeleteMapping("/board/{boardId}")
    public Result deleteArticle(BoardArticleDto dto, Long[] articleSeqs, HttpServletRequest request) {
        if (articleSeqs == null || articleSeqs.length == 0) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        boardArticleService.delete(dto, articleSeqs);
        return new Result(true, Result.OK);
    }

    @PostMapping("/board/{boardId}/{articleSeq}/comment")
    public Result insertComment(@Valid BoardCommentDto dto, HttpServletRequest request) {
        // 게시물 상세보기 권한이 있는 경우 댓글도 허용
        if (!BoardUtil.isPermitRole(dto.getBoardId(), BoardUtil.BoardAct.DETAIL)) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        UserInfoDto userInfo = UserInfoUtil.getUserInfo();

        if (userInfo != null) {
            dto.setUserId(userInfo.getUserId());
            dto.setUserNm(userInfo.getUserNm());
            dto.setEmailAddr(userInfo.getUserId());
        }

        if (dto.getUserNm() == null || dto.getUserNm().isEmpty() == true) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        boardCommentService.insert(dto);

        return new Result(true, Result.OK);
    }

    @GetMapping("/board/{boardId}/{articleSeq}/comment")
    public Result listComment(@PathVariable String boardId, @PathVariable Long articleSeq, HttpServletRequest request) {
        // 게시물 상세보기 권한이 있는 경우 댓글도 허용
        if (!BoardUtil.isPermitRole(boardId, BoardUtil.BoardAct.DETAIL)) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        List<BoardCommentDto> list = boardCommentService.list(articleSeq);

        return new Result(true, Result.OK, list);
    }

    @GetMapping("/board/{boardId}/{articleSeq}/comment/{commentSeq}")
    public Result getComment(BoardCommentDto dto, HttpServletRequest request) {
        // 게시물 상세보기 권한이 있는 경우 댓글도 허용
        if (!BoardUtil.isPermitRole(dto.getBoardId(), BoardUtil.BoardAct.DETAIL)) {
            throw new CodeMessageException(Result.FORBIDDEN);
        }

        BoardCommentDto comment = boardCommentService.get(dto);
        return new Result(true, Result.OK, comment);
    }

    @PostMapping("/board/{boardId}/{articleSeq}/check-comment-pwd/{commentSeq}")
    public Result checkCommentPwd(BoardCommentDto dto, HttpServletRequest request) throws Exception {
        boolean isMatch = boardCommentService.checkPwd(dto);

        if (!isMatch) {
            throw new CodeMessageException(Result.PASSWORD_MISMATCH);
        }

        return new Result(true, Result.OK);
    }

    @PutMapping("/board/{boardId}/{articleSeq}/comment/{commentSeq}")
    public Result updateComment(@Valid BoardCommentDto dto, HttpServletRequest request) {
        boardCommentService.update(dto);
        return new Result(true, Result.OK);
    }

    @DeleteMapping("/board/{boardId}/{articleSeq}/comment/{commentSeq}")
    public Result deleteComment(BoardCommentDto dto, HttpServletRequest request) {
        boardCommentService.delete(dto);
        return new Result(true, Result.OK);
    }

    @DeleteMapping("/board/{boardId}/{articleSeq}/comment")
    public Result deleteComment(BoardCommentDto dto, Long[] commentSeqs, HttpServletRequest request) {
        if (commentSeqs == null || commentSeqs.length == 0) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        boardCommentService.delete(dto, commentSeqs);
        return new Result(true, Result.OK);
    }

}
