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
import zinsoft.faas.dto.UserDiaryFileDto;
import zinsoft.faas.entity.QUserDiary;
import zinsoft.faas.entity.QUserDiaryFile;
import zinsoft.faas.repository.UserDiaryFileQueryRepository;
import zinsoft.util.Result;
import zinsoft.web.common.entity.QFileInfo;
import zinsoft.web.exception.CodeMessageException;

@RequiredArgsConstructor
public class UserDiaryFileQueryRepositoryImpl implements UserDiaryFileQueryRepository {

    private final JPAQueryFactory query;

    private QBean<UserDiaryFileDto> allFields = null;

    private final QUserDiaryFile userDiaryFile = QUserDiaryFile.userDiaryFile;
    private final QFileInfo fileInfo = QFileInfo.fileInfo;
    private final QUserDiary userDiary = QUserDiary.userDiary;

    @Value("${spring.data.web.pageable.size-parameter:}")
    String pageSizeParameter;

    @PostConstruct
    public void init() {
        List<Expression<?>> fieldList = Arrays.stream(QUserDiaryFile.class.getDeclaredFields())
                .filter(field->!(Modifier.isStatic(field.getModifiers())
                        || ListPath.class.isAssignableFrom(field.getType())
                        || MapPath.class.isAssignableFrom(field.getType())
                        || ArrayPath.class.isAssignableFrom(field.getType())))
                .map(field-> {
                    try {
                        return (Expression<?>) field.get(QUserDiaryFile.userDiaryFile);
                    } catch (final Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());

        // @formatter:off
        fieldList.add( fileInfo.fileNm.as("fileNm") );
       // @formatter:on

        allFields = Projections.fields(UserDiaryFileDto.class, fieldList.toArray(new Expression<?>[0]));
    }

    @Override
    public List<UserDiaryFileDto> list(Map<String, Object> search) {
     // @formatter:off
        QueryResults<UserDiaryFileDto> result = query.select(allFields)
                                                    .from(userDiaryFile)
                                                    .join(userDiary)
                                                    .on(userDiaryFile.userDiarySeq.eq(userDiary.userDiarySeq))
                                                    .join(fileInfo)
                                                    .on(userDiaryFile.fileSeq.eq(fileInfo.fileSeq))
                                                    .where(pageCondition(search))
                                                    .orderBy(userDiaryFile.userDiarySeq.asc(), userDiaryFile.fileSeq.asc())
                                                    .fetchResults();
     // @formatter:on

        return result.getResults();
    }

    private BooleanExpression pageCondition(Map<String, Object> search) {
        String userId = (String) search.get("userId");
        if (StringUtils.isNotBlank(userId) == false) {
            throw new CodeMessageException(Result.BAD_REQUEST);
        }

        BooleanExpression condition = userDiary.userId.eq(userId);
        condition = condition.and(fileInfo.userId.eq(userId));

        if (search.get("userDiarySeq") != null) {
            Long userDiarySeq = (Long) search.get("userDiarySeq");
            if (userDiarySeq > 0) {
                condition = condition.and(userDiaryFile.userDiarySeq.eq(userDiarySeq));
            }
        }

        String fileKCd = (String) search.get("fileKCd");
        if (StringUtils.isNotBlank(fileKCd)) {
            condition = condition.and(userDiaryFile.fileKCd.eq(fileKCd));
        }

        return condition;
    }
}
