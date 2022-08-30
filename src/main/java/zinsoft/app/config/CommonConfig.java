package zinsoft.app.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import zinsoft.util.HierarchicalMenu;
import zinsoft.web.common.dto.BoardDto;
import zinsoft.web.common.dto.MenuDto;

@Configuration
public class CommonConfig {

    public static final long BOOT_TIME = System.currentTimeMillis();

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasenames("classpath:/messages/message");
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(60); // -1: never reload

        return messageSource;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            //.setMatchingStrategy(MatchingStrategies.LOOSE)
            //.setAmbiguityIgnored(true)
            //.setSkipNullEnabled(true)
            .setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }

    @Bean
    public Map<String, ArrayList<MenuDto>> menuListMap() {
        return new HashMap<String, ArrayList<MenuDto>>();
    }

    @Bean
    public Map<String, HierarchicalMenu> hierarchicalMenuMap() {
        return new HashMap<String, HierarchicalMenu>();
    }

    @Bean
    public Map<String, BoardDto> boardMap() {
        return new HashMap<String, BoardDto>();
    }

}
