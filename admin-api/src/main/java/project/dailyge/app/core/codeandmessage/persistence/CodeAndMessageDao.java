package project.dailyge.app.core.codeandmessage.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityReadRepository;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityWriteRepository;
import project.dailyge.entity.codeandmessage.CodeAndMessageJpaEntity;
import static project.dailyge.entity.codeandmessage.QCodeAndMessageJpaEntity.codeAndMessageJpaEntity;

import java.util.List;

@Repository
@RequiredArgsConstructor
class CodeAndMessageDao implements CodeAndMessageEntityReadRepository, CodeAndMessageEntityWriteRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CodeAndMessageJpaEntity> findAll() {
        return queryFactory.selectFrom(codeAndMessageJpaEntity)
            .fetch();
    }

    @Override
    public List<CodeAndMessageJpaEntity> saveAll(final List<CodeAndMessageJpaEntity> codeAndMessages) {
        for (final CodeAndMessageJpaEntity entity : codeAndMessages) {
            entityManager.persist(entity);
        }
        return codeAndMessages;
    }
}
