package zinsoft.web.common.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;

@Getter
@Setter
@ToString
public class UserRememberMeId implements Serializable {

    private static final long serialVersionUID = -349790173705805396L;

    @Column(name="user_id")
    private String userId;

    @Column(name="series")
    private String series;

    @Column(name="token")
    private String token;

}