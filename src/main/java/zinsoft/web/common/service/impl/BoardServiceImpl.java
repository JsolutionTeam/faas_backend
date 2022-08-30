package zinsoft.web.common.service.impl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.util.Constants;
import zinsoft.web.common.dto.BoardDto;
import zinsoft.web.common.entity.Board;
import zinsoft.web.common.repository.BoardRepository;
import zinsoft.web.common.service.BoardService;

@Service("boardService")
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardService {

    private Type dtoListType = new TypeToken<List<BoardDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    Map<String, BoardDto> boardMap;

    @Resource
    BoardRepository boardRepository;

    @Override
    public List<BoardDto> list() {
        List<Board> list = boardRepository.findByStatusCd(Constants.STATUS_CD_NORMAL);
        return modelMapper.map(list, dtoListType);
    }

    @Override
    public Map<String, BoardDto> map() {
        List<BoardDto> dtoList = list();
        Map<String, BoardDto> map = new HashMap<>();

        if (dtoList != null) {
            for (BoardDto dto : dtoList) {
                map.put(dto.getBoardId(), dto);
            }
        }

        return map;
    }

    @Override
    public void reload() {
        boardMap.clear();
        boardMap.putAll(map());
    }

}
