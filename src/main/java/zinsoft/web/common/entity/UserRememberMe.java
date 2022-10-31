package zinsoft.web.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_user_remember_me")
@IdClass(UserRememberMeId.class)
public class UserRememberMe implements Serializable {

    private static final long serialVersionUID = -7642259459249245565L;

    @Id
    @Column(name="user_id")
    private String userId;

    @Id
    @Column(name="series")
    private String series;

    @Id
    @Column(name="token")
    private String token;

    @Column(name="last_used")
    private Date lastUsed;

}