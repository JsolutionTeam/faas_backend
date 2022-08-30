package zinsoft.web.common.service;

import java.util.List;

import zinsoft.web.common.dto.AppPropertiesDto;

public interface AppPropertiesService {

    void reload();

    List<AppPropertiesDto> list();

    void update(String propId, String propVal);

}