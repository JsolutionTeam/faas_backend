package zinsoft.web.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import zinsoft.web.common.entity.BoardFile;
import zinsoft.web.common.entity.BoardFileId;

public interface BoardFileRepository extends JpaRepository<BoardFile, BoardFileId>, BoardFileQueryRepository {

    @Query("SELECT fileSeq FROM BoardFile WHERE articleSeq = :articleSeq")
    List<Long> findFileSeqByArticleSeq(Long articleSeq);

}
