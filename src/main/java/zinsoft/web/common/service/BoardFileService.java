package zinsoft.web.common.service;

import java.io.IOException;
import java.util.List;

import zinsoft.web.common.dto.BoardArticleDto;
import zinsoft.web.common.dto.BoardFileDto;

public interface BoardFileService {

    int insert(BoardArticleDto boardArticle) throws IllegalStateException, IOException;

    List<BoardFileDto> list(Long articleSeq);

    BoardFileDto get(Long fileSeq);

    int update(BoardArticleDto boardArticle) throws IllegalStateException, IOException;

    void delete(BoardArticleDto boardArticle);

    void fill(BoardArticleDto articleDto);

}
