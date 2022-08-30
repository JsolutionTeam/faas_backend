package zinsoft.web.common.repository;

import java.util.List;

import zinsoft.web.common.dto.BoardArticleDto;
import zinsoft.web.common.dto.BoardCommentDto;

public interface BoardCommentQueryRepository {

    List<BoardCommentDto> list(Long articleSeq, Integer listOrder);

    Integer nextListOrder(Long articleSeq);

    void update(BoardCommentDto dto);

    void updateListOrder(Long articleSeq, Integer listOrder);

    void delete(BoardArticleDto articleDto);

}
