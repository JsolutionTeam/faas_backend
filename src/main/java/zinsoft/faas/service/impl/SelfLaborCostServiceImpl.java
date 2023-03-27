package zinsoft.faas.service.impl;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.SelfLaborCostDto;
import zinsoft.faas.entity.SelfLaborCost;
import zinsoft.faas.repository.SelfLaborCostRepository;
import zinsoft.faas.service.SelfLaborCostService;
import zinsoft.faas.vo.Page;
import zinsoft.faas.vo.PagingParam;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.exception.CodeMessageException;

import java.util.*;

@Service
public class SelfLaborCostServiceImpl extends EgovAbstractServiceImpl implements SelfLaborCostService {

    @Autowired
    SelfLaborCostRepository selfLaborCostRepository;

    @Autowired
    ModelMapper modelMapper;

    private SelfLaborCost getEntity(Long id) {
        Optional<SelfLaborCost> data = selfLaborCostRepository.findById(id);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    @Override
    public void insert(SelfLaborCostDto dto) {
        SelfLaborCost selfLaborCost = modelMapper.map(dto, SelfLaborCost.class);
        selfLaborCostRepository.save(selfLaborCost);
    }

    @Override
    public SelfLaborCostDto get(Long selfLaborSeq) {
        SelfLaborCost selfLaborCost = null;
        selfLaborCost = selfLaborCostRepository.findBySelfLaborSeqAndStatusCd(selfLaborSeq, "N");
        if (selfLaborCost == null)
            return null;

        return modelMapper.map(selfLaborCost, SelfLaborCostDto.class);
    }

    @Override
    public SelfLaborCostDto getByYear(String year) {
        SelfLaborCost selfLaborCost = null;

        if (StringUtils.isBlank(year)) {
            Date now = new Date();
            year = now.getYear() + "";
        }
        selfLaborCost = selfLaborCostRepository.findByYearAndStatusCd(year, "N");
        if (selfLaborCost == null)
            return null;

        return modelMapper.map(selfLaborCost, SelfLaborCostDto.class);
    }

    @Override
    public int checkValidYear(String year) {
        Long cnt = selfLaborCostRepository.countByYearAndStatusCd(year, "N");
        return cnt.intValue();
    }

    @Override
    public int checkValidYear(Long selfLaborSeq, String year) {
        Long cnt = selfLaborCostRepository.countByYearAndStatusCdAndSelfLaborSeqNot(year, "N", selfLaborSeq);
        return cnt.intValue();
    }

    @Override
    public List<SelfLaborCostDto> list(Map<String, Object> map) {
        return selfLaborCostRepository.list(map);
    }

    @Override
    public Page<SelfLaborCostDto> page(PagingParam pagingParam) {
        Page<SelfLaborCostDto> page = new Page<SelfLaborCostDto>();
        return page;
    }

    @Override
    public DataTablesResponse<SelfLaborCostDto> page(Map<String, Object> search, Pageable pageable) {
        org.springframework.data.domain.Page<SelfLaborCostDto> dtoPage = selfLaborCostRepository.page(search, pageable);
        return DataTablesResponse.of(dtoPage);
    }

    @Override
    public void update(SelfLaborCostDto dto) {
        SelfLaborCost selfLaborCost = getEntity(dto.getSelfLaborSeq());

        modelMapper.map(dto, selfLaborCost);
        selfLaborCostRepository.save(selfLaborCost);
    }

    @Override
    public void delete(Long selfLaborSeq) {
        selfLaborCostRepository.deleteBySelfLaborSeqs(new Long[]{selfLaborSeq});
    }

    @Override
    public void delete(Long[] selfLaborSeqs) {
        selfLaborCostRepository.deleteBySelfLaborSeqs(selfLaborSeqs);
    }

    @Override
    public List<Integer> yearList() {
        List<String> yearList = selfLaborCostRepository.yearList();
        List<Integer> result = new ArrayList<Integer>();

        int min = 0;
        int max = 0;

        if (Integer.parseInt(yearList.get(0)) < Integer.parseInt(yearList.get(1))) {
            min = Integer.parseInt(yearList.get(0));
            max = Integer.parseInt(yearList.get(1));
        } else {
            min = Integer.parseInt(yearList.get(1));
            max = Integer.parseInt(yearList.get(0));
        }

        int size = (max - min) + 1;
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                result.add(min);
            } else if (i == (size - 1)) {
                result.add(max);
            } else {
                result.add(min + i);
            }
        }

        return result;
    }

}
