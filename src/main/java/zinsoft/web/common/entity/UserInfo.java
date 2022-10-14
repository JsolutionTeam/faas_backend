package zinsoft.web.common.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "vf_user_info")
@DynamicInsert
@DynamicUpdate
public class UserInfo {

    @Id
    private String userId;
    private Date regDtm;
    private Date updateDtm;
    private String statusCd;
    private String userPwd;
    private Date lastLoginDtm;
    private Date userPwdDtm;
    private Date userPwdNotiDtm;
    private Date lastLoginFailDtm;
    private Short loginFailCnt;
    private String userNm;
    private String mobileNum;
    private String zipcode;
    private String addr1;
    private String addr2;
    private String emailAddr;
    private String companyNm;
    private String note;
    private String admCd;
    private String farmCode;

    @OneToMany(mappedBy = "userId")
    private List<UserRole> userRoleList;

}

/*
create or replace definer = sangju_supp@`%` view vf_user_info as
select `a`.`EMPLYR_ID`    AS `user_id`,
       `a`.`SBSCRB_DE`    AS `reg_dtm`,
       NULL               AS `update_dtm`,
       'N'                AS `status_cd`,
       `a`.`PWD`          AS `user_pwd`,
       `a`.`LAST_LOGIN`   AS `last_login_dtm`,
       NULL               AS `user_pwd_dtm`,
       NULL               AS `user_pwd_noti_dtm`,
       NULL               AS `last_login_fail_dtm`,
       0                  AS `login_fail_cnt`,
       `a`.`USER_NM`      AS `user_nm`,
       `a`.`MBTLNUM`      AS `mobile_num`,
       `a`.`ZIP`          AS `zipcode`,
       `a`.`HOUSE_ADRES`  AS `addr1`,
       `a`.`DETAIL_ADRES` AS `addr2`,
       `a`.`EMAIL_ADRES`  AS `email_addr`,
       `a`.`USER_NM`      AS `company_nm`,
       `a`.`BRTHDY`       AS `birth_dt`,
       `a`.`SEXDSTN_CODE` AS `gender_t_cd`,
       `a`.`OFFM_TELNO`   AS `tel_num`,
       0                  AS `point`,
       NULL               AS `perform`,
       NULL               AS `account_year`,
       NULL               AS `crop_seq`,
       NULL               AS `adm_cd`,
       'USR'              AS `note`,
       farm.FARM_CODE     as `farm_code`
from `sangju_mgr`.`comtnemplyrinfo` `a`
         left join `sangju_mgr`.epis_farm_manage_info_member farm
                   on `a`.EMPLYR_ID = FARM.EMPLYR_ID
union all
select `a`.`MBER_ID`          AS `user_id`,
       `a`.`SBSCRB_DE`        AS `reg_dtm`,
       NULL                   AS `update_dtm`,
       'N'                    AS `status_cd`,
       `a`.`PASSWORD`         AS `user_pwd`,
       NULL                   AS `last_login_dtm`,
       NULL                   AS `user_pwd_dtm`,
       NULL                   AS `user_pwd_noti_dtm`,
       NULL                   AS `last_login_fail_dtm`,
       0                      AS `login_fail_cnt`,
       `a`.`MBER_NM`          AS `user_nm`,
       `a`.`MBTLNUM`          AS `mobile_num`,
       `a`.`ZIP`              AS `zipcode`,
       `a`.`ADRES`            AS `addr1`,
       `a`.`DETAIL_ADRES`     AS `addr2`,
       `a`.`MBER_EMAIL_ADRES` AS `email_addr`,
       `a`.`MBER_NM`          AS `company_nm`,
       NULL                   AS `birth_dt`,
       `a`.`SEXDSTN_CODE`     AS `gender_t_cd`,
       NULL                   AS `tel_num`,
       0                      AS `point`,
       NULL                   AS `perform`,
       NULL                   AS `account_year`,
       NULL                   AS `crop_seq`,
       NULL                   AS `adm_cd`,
       'GNR'                  AS `note`,
       NULL                   AS `farm_code`
from `sangju_mgr`.`comtngnrlmber` `a`
where !(`a`.`MBER_ID` in (select `z`.`EMPLYR_ID` from `sangju_mgr`.`comtnemplyrinfo` `z`))
union all
select `a`.`USER_ID`             AS `USER_ID`,
       `a`.`REG_DTM`             AS `REG_DTM`,
       `a`.`UPDATE_DTM`          AS `UPDATE_DTM`,
       `a`.`STATUS_CD`           AS `STATUS_CD`,
       `a`.`USER_PWD`            AS `USER_PWD`,
       `a`.`LAST_LOGIN_DTM`      AS `LAST_LOGIN_DTM`,
       `a`.`USER_PWD_DTM`        AS `USER_PWD_DTM`,
       `a`.`USER_PWD_NOTI_DTM`   AS `USER_PWD_NOTI_DTM`,
       `a`.`LAST_LOGIN_FAIL_DTM` AS `LAST_LOGIN_FAIL_DTM`,
       `a`.`LOGIN_FAIL_CNT`      AS `LOGIN_FAIL_CNT`,
       `a`.`USER_NM`             AS `USER_NM`,
       `a`.`MOBILE_NUM`          AS `MOBILE_NUM`,
       `a`.`ZIPCODE`             AS `ZIPCODE`,
       `a`.`ADDR1`               AS `ADDR1`,
       `a`.`ADDR2`               AS `ADDR2`,
       `a`.`EMAIL_ADDR`          AS `EMAIL_ADDR`,
       `a`.`COMPANY_NM`          AS `COMPANY_NM`,
       `a`.`BIRTH_DT`            AS `BIRTH_DT`,
       `a`.`GENDER_T_CD`         AS `GENDER_T_CD`,
       `a`.`TEL_NUM`             AS `TEL_NUM`,
       `a`.`POINT`               AS `POINT`,
       `a`.`PERFORM`             AS `PERFORM`,
       `a`.`ACCOUNT_YEAR`        AS `ACCOUNT_YEAR`,
       `a`.`CROP_SEQ`            AS `CROP_SEQ`,
       `a`.`ADM_CD`              AS `ADM_CD`,
       `a`.`NOTE`                AS `NOTE`,
       NULL                      AS `farm_code`
from `sangju_supp`.`tf_user_info` `a`
where !(`a`.`USER_ID` in (select `z`.`EMPLYR_ID`
                          from `sangju_mgr`.`comtnemplyrinfo` `z`
                          union all
                          select `z`.`MBER_ID`
                          from `sangju_mgr`.`comtngnrlmber` `z`));


 */