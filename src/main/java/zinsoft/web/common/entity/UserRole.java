package zinsoft.web.common.entity;

import java.io.Serializable;

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
@Table(name = "vf_user_role")
@IdClass(UserRole.class)
public class UserRole implements Serializable {

    private static final long serialVersionUID = -1134958215495121633L;

    @Id
    private String userId;
    @Id
    private String roleId;

}