package zinsoft.web.common.service.impl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import zinsoft.util.AppPropertyUtil;
import zinsoft.web.common.dto.AppPropertiesDto;
import zinsoft.web.common.entity.AppProperties;
import zinsoft.web.common.repository.AppPropertiesRepository;
import zinsoft.web.common.service.AppPropertiesService;

@Service("appPropertiesService")
@Transactional
public class AppPropertiesServiceImpl extends EgovAbstractServiceImpl implements AppPropertiesService {

    private Type dtoListType = new TypeToken<List<AppPropertiesDto>>() {}.getType();

    @Autowired
    Environment env;

    @Autowired
    ModelMapper modelMapper;

    @Resource
    AppPropertiesRepository appPropertiesRepository;

    @Override
    public void reload() {
        Map<String, String> envProperties = new HashMap<>();

        if (env instanceof ConfigurableEnvironment) {
            for (PropertySource<?> propertySource : ((ConfigurableEnvironment) env).getPropertySources()) {
                if (propertySource instanceof OriginTrackedMapPropertySource) {
                    for (String key : ((EnumerablePropertySource<?>) propertySource).getPropertyNames()) {
                        envProperties.computeIfAbsent(key, k -> propertySource.getProperty(k).toString());
                    }
                }
            }
        }

        AppPropertyUtil.clear();
        List<AppPropertiesDto> dtoList = list();

        for (AppPropertiesDto dto : dtoList) {
            AppPropertyUtil.set(dto.getPropId(), dto.getPropVal());
        }

        AppPropertyUtil.setAll(envProperties);
    }

    @Override
    public List<AppPropertiesDto> list() {
        List<AppProperties> list = appPropertiesRepository.findAll();
        return modelMapper.map(list, dtoListType);
    }

    @Override
    public void update(String propId, String propVal) {
        AppPropertyUtil.set(propId, propVal);
        appPropertiesRepository.save(new AppProperties(propId, propVal));
    }

}
