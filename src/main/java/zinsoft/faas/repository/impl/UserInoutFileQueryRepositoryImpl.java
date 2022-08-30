package zinsoft.faas.repository.impl;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.ArrayPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import zinsoft.faas.dto.UserInoutFileDto;
import zinsoft.faas.entity.QUserInout;
import zinsoft.faas.entity.QUserInoutFile;
import zinsoft.faas.repository.UserInoutFileQueryRepository;
import zinsoft.util.Result;
import zinsoft.web.common.entity.QFileInfo;
import zinsoft.web.exception.CodeMessageException;

@RequiredArgsConstructor
public class UserInoutFileQueryRepositoryImpl implements UserInoutFileQueryRepository {

    private final JPAQueryFactory query;

    private QBean<UserInoutFileDto> allFields = null;

    private final QUserInoutFile userInoutFile = QUserInoutFile.userInoutFile;
    private final QFileInfo fileInfo = QFileInfo.fileInfo;
    private final QUserInout userInout = QUserInout.userInout;

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserInoutFile.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QUserInoutFile.userInoutFile);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        // @formatter:off
        fieldList.add( fileInfo.fileNm.as("fileNm") );
       // @formatter:on

        allFields = Projections.fields(UserInoutFileDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public List<UserInoutFileDto> list(Map<String, Object> search) {
     // @formatter:off
        QueryResults<UserInoutFileDto> result = query.select(allFields)
                                                    .from(userInoutFile)
                                                    .join(userInout)
                                                    .on(userInoutFile.userInoutSeq.eq(userInout.userInoutSeq))
                                                    .join(fileInfo)
                                                    .on(userInoutFile.fileSeq.eq(fileInfo.fileSeq))
                                                    .where(pageCondition(search))
                                                    .orderBy(userInoutFile.userInoutSeq.asc(), userInoutFile.fileSeq.asc())
                                                    .fetchResults();
     // @formatter:on

        return result.getResults();
    }

    private BooleanExpression pageCondition(Map<String, Object> search) {
        String userId = (String) search.get("userId");
        if (StringUtils.isNotBlank(userId) == false) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        BooleanExpression condition = userInout.userId.eq(userId);
        condition = condition.and(fileInfo.userId.eq(userId));

        if (search.get("userInoutSeq") != null) {
            Long userInoutSeq = (Long) search.get("userInoutSeq");
            if (userInoutSeq > 0) {
                condition = condition.and(userInoutFile.userInoutSeq.eq(userInoutSeq));
            }
        }

        return condition;
    }
}
