package zinsoft.web.common.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tf_menu_api")
@IdClass(MenuApi.class)
public class MenuApi implements Serializable {

    private static final long serialVersionUID = 1981236647129976993L;

    @Id
    @Column(name="menu_id")
    private String menuId;

    @Id
    @Column(name="act_cd")
    private String actCd;

    @Id
    @Column(name="path_pattern")
    private String pathPattern;

    @Id
    @Column(name="method")
    private String method;

}