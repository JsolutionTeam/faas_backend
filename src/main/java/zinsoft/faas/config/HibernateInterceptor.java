package zinsoft.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://life-journey.tistory.com/45
 * spring data jpa  @Entity schema, catalog  properties처리하기.
 */
@Component
@Slf4j
public class HibernateInterceptor extends EmptyInterceptor {
    private static Environment env;

    public HibernateInterceptor() {
    }

    @Autowired
    public HibernateInterceptor(Environment env) {
        this.env = env;
    }

    @Override
    public String onPrepareStatement(String sql) {
        if (null != env) {
            String regEx = "\\$\\{([\\\\.\\w_-]+)\\}";
            Pattern pat = Pattern.compile(regEx);
            Matcher match = pat.matcher(sql);
            while (match.find()) {
                sql = sql.replace(match.group(), env.getProperty(match.group(1)));
            }
            return super.onPrepareStatement(sql);
        } else {
            return super.onPrepareStatement(sql);
        }
    }


}