package zinsoft.faas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zinsoft.faas.repository.MgrCropTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MgrCropTypeService {

    private final MgrCropTypeRepository repository;

    public List<String> getMgrCropKindListByCropCd(String cropCd) {
        return repository.findAllByCodeId(cropCd);
    }

//    public List<MgrCropDetailResDto> getCropByCodeId(String codeId) {
//        return repository.getMgrCropDetailByCodeId(codeId);
//    }
//
//    public Boolean isExistByCode(String code) {
//        return repository.findAllByCode(code).size() > 0;
//    }
}
