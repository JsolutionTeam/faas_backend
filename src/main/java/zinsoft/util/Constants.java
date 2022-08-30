package zinsoft.util;

import java.text.SimpleDateFormat;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String WEB_SECURITY_LOGIN_PAGE = "/page/error?code=" + Result.UNAUTHORIZED;
    public static final String WEB_SECURITY_LOGIN_PROCESSING_URL = "/login";
    public static final String WEB_SECURITY_LOGOUT_URL = "/logout";
    public static final String WEB_SECURITY_USERNAME_PARAMETER = "userId";
    public static final String WEB_SECURITY_PASSWORD_PARAMETER = "userPwd";

    public static final String[] SITE_CDS = { "WEB" };
    public static final String SITE_CD = SITE_CDS[0];

    public static final String BOOLEAN_TRUE = "true";
    public static final String BOOLEAN_FALSE = "false";

    public static final String YN_YES = "Y";
    public static final String YN_NO = "N";

    public static final String STATUS_CD_NORMAL = "N";
    public static final String STATUS_CD_DELETE = "D";

    public static final String WORK_CD_INSERT = "I";
    public static final String WORK_CD_UPDATE = "U";
    public static final String WORK_CD_DELETE = "D";

    public static final String BOARD_ONLY_NOTICE = "onlyNotice";
    public static final String BOARD_WITH_NOTICE = "withNotice";
    public static final String BOARD_WITH_COMMENT = "withComment";

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat DATE_MS_FORMATTER = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    public static final String TIME_MS_ED = "235959999";

    public static final String SESSION_USER_INFO = "USER_INFO";
    public static final String SESSION_FARMER_INFO = "FARMER_INFO";

    //tb_app_properties.prop_id
    public static final String VERSION_BASIC_DATA = "version.basic_data";
    public static final String PROJECT_WITH_FRONTEND = "project.with-frontend";
    public static final String DEFAULT_ROLE_ID = "default.role-id";

    public static final String SMTP_HOST = "smtp.host";
    public static final String SMTP_PORT = "smtp.port";
    public static final String SMTP_USERNAME = "smtp.username";
    public static final String SMTP_PASSWORD = "smtp.password";
    public static final String SMTP_AUTH = "smtp.auth";
    public static final String SMTP_TLS = "smtp.tls";
    public static final String MAIL_USE = "mail.use";
    public static final String MAIL_FROM = "mail.from";
    public static final String MAIL_FROM_NAME = "mail.from.name";
    public static final String MAIL_SUBJECT_JOIN = "mail.subject.join";
    public static final String MAIL_SUBJECT_APPROVAL = "mail.subject.approval";
    public static final String MAIL_SUBJECT_FIND_PWD = "mail.subject.find-pwd";
    public static final String MAIL_SUBJECT_NEW_ARTICLE = "mail.subject.new-article";
    public static final String UPLOAD_DIR = "upload.dir";
    public static final String IMAGE_THUMB_WIDTH = "image.thumb.width";
    public static final String EXCEL_TO_PDF = "excel.to.pdf";
    public static final String EXCEL_DEFAULT = "excel.default";

    public static final String XLS_TO_PDF = "xls.to.pdf";

}
