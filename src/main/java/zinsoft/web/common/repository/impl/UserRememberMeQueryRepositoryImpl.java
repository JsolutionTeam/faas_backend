package zinsoft.web.common.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.web.common.entity.QUserRememberMe;
import zinsoft.web.common.repository.UserRememberMeQueryRepository;

@RequiredArgsConstructor
public class UserRememberMeQueryRepositoryImpl implements UserRememberMeQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public void deleteByUserId(String userId) {
        final QUserRememberMe userRememberMe = QUserRememberMe.userRememberMe;

        query.delete(userRememberMe)
                .where(userRememberMe.userId.eq(userId))
                .execute();
    }

}
