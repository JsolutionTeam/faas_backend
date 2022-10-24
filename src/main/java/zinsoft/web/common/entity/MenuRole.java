package zinsoft.web.common.entity;

import java.io.Serializable;

import javax.persistence.*;

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
@Entity
@Table(name = "tf_menu_role")
@IdClass(MenuRole.class)
public class MenuRole implements Serializable {

    private static final long serialVersionUID = -1695530510133033315L;

    @Id
    @Column(name="menu_id")
    private String menuId;

    @Id
    @Column(name="role_id")
    private String roleId;

    @Id
    @Column(name="act_cd")
    private String actCd;

}