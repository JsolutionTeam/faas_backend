package zinsoft.web.common.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import zinsoft.util.DataTablesResponse;
import zinsoft.web.common.dto.BoardArticleDto;

public interface BoardArticleService {

    void insert(BoardArticleDto dto, String baseUri) throws IllegalStateException, IOException;

    DataTablesResponse<BoardArticleDto> page(Map<String, Object> search, Pageable pageable);

    BoardArticleDto get(BoardArticleDto dto, String commentYn);

    BoardArticleDto get(BoardArticleDto dto, String commentYn, String updateReadCnt);

    boolean checkPwd(BoardArticleDto dto);

    void update(BoardArticleDto dto) throws IllegalStateException, IOException;

    void updateCommentCnt(Long articleSeq);

    void delete(BoardArticleDto dto);

    void delete(BoardArticleDto dto, Long[] articleSeqs);

}
