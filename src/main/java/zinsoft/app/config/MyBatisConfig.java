package zinsoft.app.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({ "zinsoft.faas.dao.mapper", "zinsoft.web.common.dao.mapper" })
public class MyBatisConfig {

}
