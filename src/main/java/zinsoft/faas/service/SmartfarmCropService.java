package zinsoft.faas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.SmarfarmCropResDto;
import zinsoft.faas.dto.WeatherDto;
import zinsoft.faas.repository.SmartfarmCropRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SmartfarmCropService {

    @Autowired
    private SmartfarmCropRepository smartfarmCropRepository;

    public List<SmarfarmCropResDto> getCropCodeList() {
        return smartfarmCropRepository.findAll(
                        Sort.by("idx").ascending()
                ).stream()
                .map(SmarfarmCropResDto::new)
                .collect(Collectors
                        .toList()
                );
    }
}
