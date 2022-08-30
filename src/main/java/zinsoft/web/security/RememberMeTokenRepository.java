package zinsoft.web.security;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.extern.slf4j.Slf4j;
import zinsoft.web.common.dto.UserRememberMeDto;
import zinsoft.web.common.service.UserRememberMeService;

@Slf4j
public class RememberMeTokenRepository implements PersistentTokenRepository {

    @Resource
    UserRememberMeService userRememberMeService;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        userRememberMeService.insert(new UserRememberMeDto(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate()));
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        userRememberMeService.update(new UserRememberMeDto(series, tokenValue, lastUsed));
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {
        try {
            UserRememberMeDto dto = userRememberMeService.get(series);

            if (dto != null) {
                return new PersistentRememberMeToken(dto.getUserId(), dto.getSeries(), dto.getToken(), dto.getLastUsed());
            }
        } catch (Exception e) {
            log.error("Failed to load token for series " + series, e);
        }

        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        userRememberMeService.delete(username);
    }

}
