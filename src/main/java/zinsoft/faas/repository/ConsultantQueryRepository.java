package zinsoft.faas.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import zinsoft.faas.dto.ConsultantResDto;
import zinsoft.faas.dto.QConsultantResDto;
import zinsoft.faas.entity.Consultant;
import zinsoft.faas.entity.QConsultant;
import zinsoft.util.UserInfoUtil;
import zinsoft.web.common.dto.UserInfoDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

import static zinsoft.faas.entity.QConsultant.consultant;


@Repository
@Slf4j
public class ConsultantQueryRepository extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public ConsultantQueryRepository(JPAQueryFactory queryFactory) {
        super(Consultant.class);
        this.queryFactory = queryFactory;
    }

    public List<ConsultantResDto> findMyConsultant(){
        UserInfoDto userInfo = UserInfoUtil.getUserInfo();
        String farmCode = userInfo.getFarmCode();
        List<ConsultantResDto> consults = queryFactory.select(
                        new QConsultantResDto(consultant)
                ).from(consultant)
                .where(
                        isFarmerNoEq(farmCode),
                        isNowBetweenStartAndEnd()
                ).fetch();
        return consults;
    }

    private BooleanExpression isFarmerNoEq(String farmCode){
        return consultant.farmerNo.eq(farmCode);
    }

    private BooleanExpression isNowBetweenStartAndEnd() {
        Date now = new Date();

        return isAfterStartDate(now)
                .and(
                        isBeforeEndDate(now)
                );

    }

    private BooleanExpression isAfterStartDate(Date now){
        if(now == null)now = new Date();
        return consultant.startdate.before(now);
    }

    private BooleanExpression isBeforeEndDate(Date now){
        if(now == null)now = new Date();
        return consultant.endDate.after(now);
    }
}
