package zinsoft;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import zinsoft.faas.config.ProjectDbSchemaProperties;

@SpringBootApplication
@Slf4j
//@EnableConfigurationProperties(ProjectDbSchemaProperties.class)
public class FaasApplication {

    public static void main(String[] args) {
        log.info("시스템 런");
        SpringApplication.run(FaasApplication.class, args);
    }

}
