package zinsoft.faas.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties("catalog")
public class ProjectDbSchemaProperties {

    private Map<String, String> config;

    public void setConfig(Map<String,String> config) {
        this.config = config;
    }

    public Map<String,String> getConfig() {
        return config;
    }
}