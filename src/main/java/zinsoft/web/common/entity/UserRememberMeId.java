package zinsoft.web.common.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRememberMeId implements Serializable {

    private static final long serialVersionUID = -349790173705805396L;

    private String userId;
    private String series;
    private String token;

}