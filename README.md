# 농정원 스마트팜역신밸리 빅데이터 센터 (상주/김제) Back-end

## 개발환경
* Eclipse 2021-03
* Java 1.8 (JavaSE-1.8)
* Tomcat 9
* lombok

## 프로젝트 실행
* eclipse에서 `[File->Import->General->Existing Projects into Workspace]`로 `SMART_SANGJU_SUP(SMART_SANGJU_ADV)` 프로젝트 import
* eclipse에서 `[File->Import->Maven->Existing Maven Projects]`로 `SFIV_API(sfiv)` 프로젝트 import
* eclipse에서 Tomcat 9 생성
  * start timeout을 300으로 수정
  * `SMART_SANGJU_ADV`의 path를 `/`로 수정
  * server.xml에서 `SMART_SANGJU_ADV` context에 아래 추가
    ```
	          <Resource auth="Container" driverClassName="org.mariadb.jdbc.Driver" initialSize="5" maxIdle="30" maxTotal="50" maxWaitMillis="300" minIdle="5" name="jdbc/sangju_supp" password="tkdwn12#$" type="javax.sql.DataSource" url="jdbc:mariadb://59.25.177.42:3355/sangju_supp" username="sangju_supp"/>
    ``` 
  * Spring profile을 `loc`로 설정 (Tomcat VM arguments에 아래 추가)
    ```
	 -Dspring.profiles.active=loc
	```

pom.xml 우클릭 > Maven > Generate Sources and Update Folders 클릭