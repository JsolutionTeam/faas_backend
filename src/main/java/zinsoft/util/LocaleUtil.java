package zinsoft.util;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

public class LocaleUtil {

    public static final Locale DEFAULT_LOCALE = Locale.KOREAN;

    private LocaleUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Locale locale() {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            if (locale.getLanguage().isEmpty()) {
                locale = DEFAULT_LOCALE;
            }
            return locale;
        } catch (Exception e) {
            // ignore
        }

        return DEFAULT_LOCALE;
    }

    public static String lang() {
        try {
            Locale locale = LocaleContextHolder.getLocale();
            String lang = locale.getLanguage();
            return lang.isEmpty() ? DEFAULT_LOCALE.getLanguage() : lang;
        } catch (Exception e) {
            // ignore
        }

        return DEFAULT_LOCALE.getLanguage();
    }

}
