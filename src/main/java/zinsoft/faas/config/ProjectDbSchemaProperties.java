package zinsoft.faas.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties("catalog")
// application.properties 혹은 .yaml 파일에서 catalog.config.AAA=BBB 형태로 값을 넣어주면
// Entity 파일에서 schema = "AAA" 형태로 사용할 수 있음 => BBB 값이 schema 값으로 들어감
public class ProjectDbSchemaProperties {

    private Map<String, String> config;

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public Map<String, String> getConfig() {
        return config;
    }

}