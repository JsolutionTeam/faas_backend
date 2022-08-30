package zinsoft.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;
import zinsoft.util.CommonUtil;

@Slf4j
public class Sha256PasswordEncoder implements PasswordEncoder {

    @Value("${spring.profiles.active:null}")
    private String activeProfile;

    @Override
    public String encode(CharSequence rawPassword) {
        String userId = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest().getParameter("userId");
        return CommonUtil.sha256(rawPassword.toString(), userId, "BASE64");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || rawPassword == null) {
            return false;
        }

        String enc = encode(rawPassword);

        if (!encodedPassword.equals(enc) // 신라시스템 암호화
                && !encodedPassword.equals(CommonUtil.sha256(rawPassword.toString()))) { // FAAS 암호화
            if ("loc".equals(activeProfile)) {
                log.error(enc);
            }

            return false;
        }

        return true;
    }

}
