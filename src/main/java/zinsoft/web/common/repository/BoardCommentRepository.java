package zinsoft.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.BoardComment;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>, BoardCommentQueryRepository {

    BoardComment findByCommentSeqAndBoardIdAndStatusCd(Long commentSeq, String boardId, String statusCd);

    BoardComment findByCommentSeqAndBoardIdAndArticleSeqAndStatusCd(Long commentSeq, String boardId, Long articleSeq, String statusCd);

}
