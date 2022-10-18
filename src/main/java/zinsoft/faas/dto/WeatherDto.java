package zinsoft.faas.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import zinsoft.faas.entity.SmartfarmCrop;

@Getter
@Setter
@ToString
@JsonInclude(Include.NON_NULL)
public class WeatherDto {

    private Long sn;
    @ApiModelProperty(notes = "번호")
    private String no;
    @ApiModelProperty(notes = "관측지점코드")
    private String stnCode;
    @ApiModelProperty(notes = "관측지점명")
    private String stnName;
    @ApiModelProperty(notes = "관측시간")
    private Date date;
    @ApiModelProperty(notes = "현재기온")
    private String temp;
    @ApiModelProperty(notes = "최고기온")
    private String maxTemp;
    @ApiModelProperty(notes = "최저기온")
    private String minTemp;
    @ApiModelProperty(notes = "최저기온")
    private String hum; //습도
    @ApiModelProperty(notes = "풍향")
    private String widdir;
    @ApiModelProperty(notes = "풍속")
    private String wind;
    @ApiModelProperty(notes = "강수량")
    private String rain;
    @ApiModelProperty(notes = "일조시간")
    private String sunTime;
    @ApiModelProperty(notes = "일사량")
    private String sunQy;
    @ApiModelProperty(notes = "결로시간")
    private String condensTime;
    @ApiModelProperty(notes = "초상온도")
    private String grTemp;
    @ApiModelProperty(notes = "지중온도")
    private String soilTemp;
    @ApiModelProperty(notes = "토양수분보정값")
    private String soilWt;

//  //  @NotBlank
//    @ApiModelProperty(notes = "날짜 YYYYMMDD")
//   // @Pattern(regexp = "^\\d{4}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$")
//    private String baseDate;
//    private Integer nx;
//    private Integer ny;
//  //  private String skyTCd;
//    @ApiModelProperty(notes = "현재기온")
//    private Double temp;
//    @ApiModelProperty(notes = "최저기온")
//    private Double tmn;
//    @ApiModelProperty(notes = "최고기온")
//    private Double tmx;
//    @ApiModelProperty(notes = "강수량")
//    private Double rnf;
//    @ApiModelProperty(notes = "습도")
//    private Double reh;
// //   private String skyTCdNm;


}