package zinsoft.faas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.MgrCropDetailResDto;
import zinsoft.faas.repository.MgrCropDetailRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MgrCropDetailService {

    private final MgrCropDetailRepository repository;

    public List<MgrCropDetailResDto> getCropByCodeId(String codeId) {
        return repository.getMgrCropDetailByCodeId(codeId);
    }

    public Boolean isExistByCode(String code) {
        return repository.findAllByCode(code).size() > 0;
    }
}
