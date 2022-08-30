package zinsoft.web.common.service;

import java.util.List;

import zinsoft.web.common.dto.BoardArticleDto;
import zinsoft.web.common.dto.BoardCommentDto;

public interface BoardCommentService {

    void insert(BoardCommentDto dto);

    List<BoardCommentDto> list(Long articleSeq);

    List<BoardCommentDto> list(Long articleSeq, Integer listOrder);

    BoardCommentDto get(BoardCommentDto dto);

    boolean checkPwd(BoardCommentDto dto);

    void update(BoardCommentDto dto);

    void delete(BoardCommentDto dto);

    void delete(BoardCommentDto dto, Long[] commentSeqs);

    void delete(BoardArticleDto articleDto);

    void fill(BoardArticleDto articleDto);

    void fill(List<BoardArticleDto> articleDtoList);

}
