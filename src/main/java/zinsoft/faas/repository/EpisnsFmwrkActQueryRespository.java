package zinsoft.faas.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import zinsoft.faas.dto.EpisNsFmwrkActCalResDto;

import java.util.List;

@RequiredArgsConstructor
public class EpisnsFmwrkActQueryRespository {

    private final JPAQueryFactory query;

//    List<EpisNsFmwrkActCalResDto> findAllByFmwrkActCode(String actCode);

}
