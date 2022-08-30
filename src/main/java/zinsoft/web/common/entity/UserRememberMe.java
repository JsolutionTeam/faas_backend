package zinsoft.web.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

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
    private String userId;
    @Id
    private String series;
    @Id
    private String token;
    private Date lastUsed;

}