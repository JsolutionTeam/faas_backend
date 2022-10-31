package zinsoft.web.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "tf_app_properties")
public class AppProperties {

    @Id
    @Column(name = "prop_id")
    private String propId;

    @Column(name = "prop_val")
    private String propVal;

}