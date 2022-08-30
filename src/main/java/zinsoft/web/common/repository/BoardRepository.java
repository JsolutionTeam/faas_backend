package zinsoft.web.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import zinsoft.web.common.entity.Board;

public interface BoardRepository extends JpaRepository<Board, String> {

    List<Board> findByStatusCd(String statusCd);

}
