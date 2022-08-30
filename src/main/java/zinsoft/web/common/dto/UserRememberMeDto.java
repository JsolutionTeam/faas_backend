package zinsoft.web.common.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserRememberMeDto {

    private String userId;
    private String series;
    private String token;
    private Date lastUsed;

    public UserRememberMeDto(String series, String token, Date lastUsed) {
        this.series = series;
        this.token = token;
        this.lastUsed = lastUsed;
    }

}