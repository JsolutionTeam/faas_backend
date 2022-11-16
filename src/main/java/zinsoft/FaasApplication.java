package zinsoft;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class FaasApplication {

    public static void main(String[] args) {
        log.info("시스템 런");
        SpringApplication.run(FaasApplication.class, args);
    }

//    //원래 다른서버(톰캣이라면 톰캣의 server.xml에 추가설정 필요, was별로 다름)와 세션공유를 위해 web.xml 에 <distributable/> 설정해주는 부분 대체
//    private static boolean distributable;
//
//
//    public static boolean getDistributable() {
//        return distributable;
//    }
//
//    @Bean
//    public ServletWebServerFactory tomcatFactory() {
//        return new TomcatServletWebServerFactory() {
//            @Override
//            protected void postProcessContext(Context context) {
//                FaasApplication.distributable = context.getDistributable();
//                log.info("distributable is :"+distributable);
//            }
//        };
//    }
//    //원래 다른서버(톰캣이라면 톰캣의 server.xml에 추가설정 필요, was별로 다름)와 세션공유를 위해 web.xml 에 <distributable/> 설정해주는 부분 대체 끝
}
