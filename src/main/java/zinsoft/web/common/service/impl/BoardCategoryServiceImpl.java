package zinsoft.web.common.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.web.common.dto.BoardCategoryDto;
import zinsoft.web.common.entity.BoardCategory;
import zinsoft.web.common.repository.BoardCategoryRepository;
import zinsoft.web.common.service.BoardCategoryService;

@Service("boardCategoryService")
@Transactional
public class BoardCategoryServiceImpl extends EgovAbstractServiceImpl implements BoardCategoryService {

    private Type dtoListType = new TypeToken<List<BoardCategoryDto>>() {}.getType();

    @Autowired
    ModelMapper modelMapper;

    @Resource
    BoardCategoryRepository boardCategoryRepository;

    @Override
    public List<BoardCategoryDto> list(String boardId) {
        List<BoardCategory> list = boardCategoryRepository.findByBoardIdOrderByBoardIdAscExprSeqAscCatSeqAsc(boardId);
        return modelMapper.map(list, dtoListType);
    }

}
