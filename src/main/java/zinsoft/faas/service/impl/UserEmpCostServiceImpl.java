package zinsoft.faas.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dto.UserEmpCostDto;
import zinsoft.faas.entity.UserEmpCost;
import zinsoft.faas.repository.UserEmpCostRepository;
import zinsoft.faas.service.UserEmpCostService;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.exception.CodeMessageException;

@Service
public class UserEmpCostServiceImpl extends EgovAbstractServiceImpl implements UserEmpCostService {

    @Resource
    UserEmpCostRepository userEmpCostRepository;

    @Autowired
    ModelMapper modelMapper;

    private UserEmpCost getEntity(Long id) {
        Optional<UserEmpCost> data = userEmpCostRepository.findById(id);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    private UserEmpCost getEntity(String userId, Long userEmpCostSeq) {
        if (StringUtils.isBlank(userId)) {
            return getEntity(userEmpCostSeq);
        }

        UserEmpCost userEmpCost = userEmpCostRepository.findByUserIdAndUserEmpCostSeq(userId, userEmpCostSeq);

        if (userEmpCost == null) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return userEmpCost;
    }

    @Override
    public void insert(UserEmpCostDto dto) {
        dto.setUserEmpCostSeq(null);
        UserEmpCost userEmpCost = modelMapper.map(dto, UserEmpCost.class);
        userEmpCostRepository.save(userEmpCost);
    }

    @Override
    public UserEmpCostDto getByUserId(String userId) {
        UserEmpCost userEmpCost = userEmpCostRepository.findByUserIdAndStatusCd(userId, "N");

        if (userEmpCost == null) {
            return new UserEmpCostDto(userId);
        }
        return modelMapper.map(userEmpCost, UserEmpCostDto.class);
    }

    @Override
    public int checkValidYear(String year, String userId) {
        if(year == null) {
            year = new Date().getYear() + "";
        }
        Long cnt = userEmpCostRepository.countByYearAndUserIdAndStatusCd(year, userId, "N");
        return cnt.intValue();
    }

    @Override
    public int checkValidYear(Long userEmpCostSeq, String year, String userId) {
        if(year == null) {
            year = new Date().getYear() + "";
        }
        Long cnt = userEmpCostRepository.countByYearAndUserIdAndStatusCdAndUserEmpCostSeqNot(year, userId, "N", userEmpCostSeq);
        return cnt.intValue();
    }

    @Override
    public UserEmpCostDto get(String userId, Long userEmpCostSeq) {
        return userEmpCostRepository.get( userId, userEmpCostSeq);
    }

    @Override
    public UserEmpCostDto getByYear(String userId, String year) {
        UserEmpCost userEmpCost = userEmpCostRepository.findByUserIdAndYearAndStatusCd(userId, year, "N");

        if (userEmpCost == null) {
            return new UserEmpCostDto(userId);
        }

        return modelMapper.map(userEmpCost, UserEmpCostDto.class);
    }

    @Override
    public DataTablesResponse<UserEmpCostDto> page(Map<String, Object> search, Pageable pageable) {
        Page<UserEmpCostDto> dtoPage = userEmpCostRepository.page(search, pageable);
        return DataTablesResponse.of(dtoPage);
    }

    @Override
    public List<UserEmpCostDto> list(Map<String, Object> param) {
        return userEmpCostRepository.list(param);
    }

    @Override
    public void update(UserEmpCostDto dto) {
        UserEmpCost userEmpCost = getEntity(dto.getUserId(),  dto.getUserEmpCostSeq());

        dto.setUpdateDtm(new Date());
        modelMapper.map(dto, userEmpCost);
        userEmpCostRepository.save(userEmpCost);
    }

    @Override
    public void deleteByUserId(String userId) {
        userEmpCostRepository.deleteByUserId(userId);
    }

    @Override
    public void delete(String userId, Long userEmpCostSeq) {
        UserEmpCost userEmpCost = getEntity(userId, userEmpCostSeq);

        userEmpCostRepository.deleteById(userEmpCostSeq);
    }

    @Override
	public List<Integer> yearList(String userId) {
		List<String> yearList = userEmpCostRepository.yearList(userId);
		List<Integer> result = new ArrayList<Integer>();

		int min = 0;
		int max = 0;

		if(yearList.get(0) != null) {

			if(Integer.parseInt(yearList.get(0)) < Integer.parseInt(yearList.get(1))) {
				min = Integer.parseInt(yearList.get(0));
				max = Integer.parseInt(yearList.get(1));
			}else {
				min = Integer.parseInt(yearList.get(1));
				max = Integer.parseInt(yearList.get(0));
			}

		}

		int size = (max - min) + 1;
		for(int i = 0; i < size; i++) {
			if(i == 0) {
				result.add(min);
			}else if(i == (size - 1)) {
				result.add(max);
			}else {
				result.add(min + i);
			}
		}

		return result;
	}

}
