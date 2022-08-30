package zinsoft.web.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.BoardCategory;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {

    List<BoardCategory> findByBoardIdOrderByBoardIdAscExprSeqAscCatSeqAsc(String boardId);

}
