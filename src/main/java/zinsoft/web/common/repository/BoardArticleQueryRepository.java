package zinsoft.web.common.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import zinsoft.web.common.dto.BoardArticleDto;

public interface BoardArticleQueryRepository {

    Page<BoardArticleDto> page(Map<String, Object> search, Pageable pageable);

    void updateCommentCnt(Long articleSeq);

}
