package zinsoft.web.common.service.impl;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import lombok.extern.slf4j.Slf4j;
import zinsoft.web.common.service.AppPropertiesService;
import zinsoft.web.common.service.BasicDataService;
import zinsoft.web.common.service.BoardService;
import zinsoft.web.common.service.CommonDataService;
import zinsoft.web.common.service.EmailService;
import zinsoft.web.common.service.MenuService;
import zinsoft.web.security.ReloadableFilterInvocationSecurityMetadataSource;

@Slf4j
@Service("commonDataService")
public class CommonDataServiceImpl extends EgovAbstractServiceImpl implements CommonDataService {

    @Value("${spring.servlet.multipart.location}")
    private String multipartLocation;

    @Autowired
    ReloadableFilterInvocationSecurityMetadataSource reloadableFilterInvocationSecurityMetadataSource;

    @Resource
    AppPropertiesService appPropertiesService;

    @Resource
    MenuService menuService;

    @Resource
    BoardService boardService;

    @Resource
    EmailService emailService;

    @Resource
    BasicDataService basicDataCreateService;

    //@PostConstruct
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        if (multipartLocation != null && !multipartLocation.isEmpty()) {
            File multipartLocationDir = new File(multipartLocation);

            if (!multipartLocationDir.exists()) {
                multipartLocationDir.mkdirs();
            }
        }

        reload();
        log.info("Common data reloaded!");
    }

    @Override
    public void reload() {
        appPropertiesService.reload();
        menuService.reload();
        reloadableFilterInvocationSecurityMetadataSource.reload();
        boardService.reload();
        emailService.init();
        basicDataCreateService.createBasicData();
    }

}
