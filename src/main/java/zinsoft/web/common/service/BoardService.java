package zinsoft.web.common.service;

import java.util.List;
import java.util.Map;

import zinsoft.web.common.dto.BoardDto;

public interface BoardService {

    List<BoardDto> list();

    Map<String, BoardDto> map();

    void reload();

}
