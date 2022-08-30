package zinsoft.faas.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.faas.dto.CropDto;
import zinsoft.faas.entity.Crop;
import zinsoft.faas.repository.CropRepository;
import zinsoft.faas.service.CropService;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;
import zinsoft.web.common.service.BasicDataService;
import zinsoft.web.exception.CodeMessageException;

@Service
public class CropServiceImpl extends EgovAbstractServiceImpl implements CropService {

    @Resource
    CropRepository cropRepository;

    @Autowired
    ModelMapper modelMapper;

    @Resource
    BasicDataService basicDataService;

    private Crop getEntity(Long id) {
        Optional<Crop> data = cropRepository.findById(id);

        if (!data.isPresent()) {
            throw new CodeMessageException(Result.NO_DATA);
        }

        return data.get();
    }

    @Override
    public void insert(CropDto dto) {
        if (dto.getMaLife() == null) {
            dto.setMaLife(0L);
        }

        dto.setExprYn("Y");
        dto.setUpdateYn("Y");
        dto.setDeleteYn("N");
        dto.setAssetYn("N");
        dto.setCropingYn("N");
        //cropMapper.insert(dto);
        Crop crop = modelMapper.map(dto, Crop.class);

        cropRepository.save(crop);

        basicDataService.createBasicData();
    }

    @Override
    public List<CropDto> list() {
        //return cropMapper.list(null);
        return cropRepository.list(null);
    }

    @Override
    public List<CropDto> list(Map<String, Object> param) {
        return cropRepository.list(param);
        //return cropMapper.list(param);
    }

    @Override
    public CropDto get(Long cropSeq) {
        return cropRepository.get(cropSeq);
    }

    @Override
    public boolean isExistCropId(Long cropSeq) {
        return cropRepository.existsByCropSeqAndStatusCd(cropSeq, "N");
    }

    @Override
    public DataTablesResponse<CropDto> page(Map<String, Object> search, Pageable pageable) {
        org.springframework.data.domain.Page<CropDto> dtoPage = cropRepository.page(search, pageable);
        return DataTablesResponse.of(dtoPage);
    }

    @Override
    public void update(CropDto dto) {
        if (dto.getMaLife() == null) {
            dto.setMaLife(0L);
        }
        Crop crop = getEntity(dto.getCropSeq());
        dto.setUpdateDtm(new Date());

        modelMapper.map(dto, crop);
        cropRepository.save(crop);

        basicDataService.createBasicData();
    }

    @Override
    public void delete(Long[] cropSeqs) {

        cropRepository.deleteAllById(cropSeqs);
        //cropMapper.delete(cropSeqs);
        basicDataService.createBasicData();
    }

}
