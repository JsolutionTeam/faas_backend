package zinsoft.faas.entity;

import javax.persistence.Column;
import java.util.Date;

public class PlantInfo {

    // 인덱스
    @Column(name = "idx")
    private String plantInfoIdx;

    // 농가 ID
    @Column(name = "farmerNo")
    private String farmerNo;

    // 작기 ID
    @Column(name = "cultNo")
    private String cultNo;

    // 기록일자
    @Column(name = "recDate")
    private Date recDate;

    // 주차
    @Column(name = "weeks")
    private Integer weeks;

    // 초장(mm)
    @Column(name = "leafArea")
    private Double leafArea;

    // 생장길이(mm)
    @Column(name = "growthLength")
    private Double growthLength;

    // 줄기굵기(mm)
    @Column(name = "plantWidth")
    private Double plantWidth;

    // 엽장(mm)
    @Column(name = "leafLength")
    private Double leafLength;

    // 엽폭(mm)
    @Column(name = "leafWidth")
    private Double leafWidth;

    // 엽수(개)
    @Column(name = "leafNo")
    private Integer leafNo;

    // 화방높이(mm)
    @Column(name = "flowerHeight")
    private Double flowerHeight;

    // 꽃 수(개)
    @Column(name = "flowerCnt")
    private Double flowerCnt;

    // 만개꽃수(개)
    @Column(name = "flowerOpenCnt")
    private Double flowerOpenCnt;

    // 개화군(점)
    @Column(name = "flowerPoint")
    private Double flowerPoint;

    // 착과군(점)
    @Column(name = "fruitPoint")
    private Double fruitPoint;

    // 개화속도
    @Column(name = "flowerSpd")
    private Double flowerSpd;

    // 착과속도
    @Column(name = "fruitSped")
    private Double fruitSped;
}
