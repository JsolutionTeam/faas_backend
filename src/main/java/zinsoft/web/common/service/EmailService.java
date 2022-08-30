package zinsoft.web.common.service;

public interface EmailService {

    void init();

    void destroy();

    void send(final String to, final String template, final String subject, final Object var, final String baseUri);

    void send(String from, String to, String subject, String text);

}