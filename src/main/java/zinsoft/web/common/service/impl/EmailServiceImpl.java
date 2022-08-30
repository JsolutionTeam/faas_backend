package zinsoft.web.common.service.impl;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import lombok.extern.slf4j.Slf4j;
import zinsoft.util.AppPropertyUtil;
import zinsoft.util.Constants;
import zinsoft.web.common.service.EmailService;

@Service("emailService")
@Slf4j // ExceptionHandlerControllerAdvice.class
public class EmailServiceImpl extends EgovAbstractServiceImpl implements EmailService {

    private static final ExecutorService mailThreadPool = Executors.newFixedThreadPool(5);

    @Autowired
    TemplateEngine templateEngine;

    private JavaMailSender mailSender;

    @Override
    public void init() {
        String smtpHost = AppPropertyUtil.get(Constants.SMTP_HOST);

        if (smtpHost == null) {
            return;
        }

        JavaMailSenderImpl defaultMailSender = new JavaMailSenderImpl();
        Properties prop = new Properties();
        String auth = AppPropertyUtil.get(Constants.SMTP_AUTH);
        String tls = AppPropertyUtil.get(Constants.SMTP_TLS);

        defaultMailSender.setHost(smtpHost);
        defaultMailSender.setPort(Integer.parseInt(AppPropertyUtil.get(Constants.SMTP_PORT)));
        defaultMailSender.setDefaultEncoding("UTF-8");

        if ("true".equals(auth)) {
            defaultMailSender.setUsername(AppPropertyUtil.get(Constants.SMTP_USERNAME));
            defaultMailSender.setPassword(AppPropertyUtil.get(Constants.SMTP_PASSWORD));

            prop.setProperty("mail.smtp.auth", auth);
            prop.setProperty("mail.smtp.starttls.enable", tls);
            prop.setProperty("mail.smtp.socketFactory.port", AppPropertyUtil.get(Constants.SMTP_PORT));

            if ("true".equals(tls)) {
                prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                prop.setProperty("mail.smtp.socketFactory.fallback", "false");
            }

            defaultMailSender.setJavaMailProperties(prop);
        }

        this.mailSender = defaultMailSender;
    }

    @Override
    @PreDestroy
    public void destroy() {
        mailThreadPool.shutdown();

        try {
            mailThreadPool.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void send(final String to, final String template, final String subject, final Object var, final String baseUri) {
        if (!"true".equals(AppPropertyUtil.get(Constants.MAIL_USE))) {
            return;
        }

        mailThreadPool.execute(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                Context context = new Context();

                context.setVariable("baseUri", baseUri);
                context.setVariable("title", "경상북도 우리집RE100 통합관제센터 TOC");
                context.setVariable("var", var);

                final String content = templateEngine.process(template, context);

                helper.setFrom(new InternetAddress(AppPropertyUtil.get(Constants.MAIL_FROM), AppPropertyUtil.get(Constants.MAIL_FROM_NAME), "utf-8"));
                helper.setSubject(subject);
                helper.setText(content, true);

                String[] emails = to.split(",");
                for (String email : emails) {
                    helper.setTo(email);
                    mailSender.send(message);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    @Override
    public void send(final String from, final String to, final String subject, final String text) {
        if (!"true".equals(AppPropertyUtil.get(Constants.MAIL_USE))) {
            return;
        }

        mailThreadPool.execute(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(text, true);

                mailSender.send(message);
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

}
