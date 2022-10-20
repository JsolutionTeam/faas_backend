package zinsoft.faas.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties("catalog")
public class ProjectDbSchemaProperties {

//    private String adv;
//    private String bdm;
//    private String mgr;
//    private String supp;
//    private String smartfarm;


    private Map<String, String> config;

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public Map<String, String> getConfig() {
        return config;
    }

}