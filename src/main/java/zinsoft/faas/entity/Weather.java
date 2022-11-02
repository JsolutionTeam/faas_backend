package zinsoft.faas.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "getweathertenminlist")
@DynamicInsert
@DynamicUpdate
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sn;
    private String no; //번호
    private String stnCode; //관측지점코드
    private String stnName; //관측지점명
    private Date date; //관측시간
    private String temp; //기온
    private String maxTemp; //최고기온
    private String minTemp; //최저기온
    private String hum; //습도
    private String widdir; //푸향
    private String wind; //풍속
    private String rain; //강수량
    private String sunTime; //일조시간
    private String sunQy; //일사량
    private String condensTime; //결로시간
    private String grTemp; //초상온도
    private String soilTemp; //지중온도
    private String soilWt; //토양수분보정값

}