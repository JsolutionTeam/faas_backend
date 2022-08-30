package zinsoft.web.common.service;

import java.util.List;

import zinsoft.web.common.dto.BoardCategoryDto;

public interface BoardCategoryService {

    List<BoardCategoryDto> list(String boardId);

}
