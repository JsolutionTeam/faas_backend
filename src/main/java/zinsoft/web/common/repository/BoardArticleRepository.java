package zinsoft.web.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import zinsoft.web.common.entity.BoardArticle;

public interface BoardArticleRepository extends JpaRepository<BoardArticle, Long>, BoardArticleQueryRepository {

    BoardArticle findByArticleSeqAndBoardIdAndStatusCd(Long articleSeq, String boardId, String statusCd);

    List<BoardArticle> findByBoardIdAndStatusCdAndNoticeYn(String boardId, String statusCd, String noticeYn);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE BoardArticle a SET a.readCnt = a.readCnt + 1 WHERE a.articleSeq = :articleSeq")
    void increaseReadCnt(Long articleSeq);

}
