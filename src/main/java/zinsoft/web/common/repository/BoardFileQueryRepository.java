package zinsoft.web.common.repository;

import java.util.List;

import zinsoft.web.common.dto.BoardFileDto;

public interface BoardFileQueryRepository {

    List<BoardFileDto> list(Long articleSeq);

    BoardFileDto get(Long fileSeq);

    long delete(Long articleSeq, List<Long> fileSeqList);

}
