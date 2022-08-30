package zinsoft.web.common.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

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
    private String menuId;
    @Id
    private String roleId;
    @Id
    private String actCd;

}