package zinsoft.faas.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import zinsoft.faas.dto.EpisNsFmwrkActCalResDto;
import zinsoft.faas.dto.QEpisNsFmwrkActCalResDto;
import zinsoft.faas.entity.EpisNsFmwrkActCal;

import static zinsoft.faas.entity.QEpisNsFmwrkAct.episNsFmwrkAct;
import static zinsoft.faas.entity.QEpisNsFmwrkActCal.episNsFmwrkActCal;
import static zinsoft.faas.entity.QEpisNsFmwrkWrkcd.episNsFmwrkWrkcd;

@Repository
public class EpisNsFmwrkActCalQueryRespository extends QuerydslRepositorySupport {

    private final JPAQueryFactory queryFactory;

    public EpisNsFmwrkActCalQueryRespository(JPAQueryFactory queryFactory) {
        super(EpisNsFmwrkActCal.class);
        this.queryFactory = queryFactory;

    }

    public EpisNsFmwrkActCalResDto getFmwrkActCal(String cropCd, String growStep, String fmwrkCd) {
        return queryFactory.select(
                        new QEpisNsFmwrkActCalResDto(episNsFmwrkActCal)
                ).from(episNsFmwrkActCal)
                .leftJoin(episNsFmwrkAct).on(episNsFmwrkAct.fmwrkActCode.eq(episNsFmwrkActCal.fmwrkActCode))
                .leftJoin(episNsFmwrkWrkcd).on(episNsFmwrkWrkcd.fmwrkCd.eq(episNsFmwrkActCal.fmwrkCd))
                .where(
                        episNsFmwrkAct.cropCd.eq(cropCd)
                                .and(episNsFmwrkActCal.growStep.eq(growStep))
                                .and(episNsFmwrkActCal.fmwrkCd.eq(fmwrkCd))
                ).fetchOne();

    }
}
