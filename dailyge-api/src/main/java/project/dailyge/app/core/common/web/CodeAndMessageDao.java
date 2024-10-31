package project.dailyge.app.core.common.web;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityReadRepository;
import project.dailyge.entity.codeandmessage.CodeAndMessageEntityWriteRepository;
import project.dailyge.entity.codeandmessage.CodeAndMessageJpaEntity;

import java.util.List;

@Service
public class CodeAndMessageDao implements CodeAndMessageEntityReadRepository, CodeAndMessageEntityWriteRepository {

    private static final String SELECT_ALL = "SELECT c FROM code_and_message c";
    private final EntityManager entityManager;

    public CodeAndMessageDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveAll(final List<CodeAndMessageJpaEntity> codeAndMessages) {
        for (final CodeAndMessageJpaEntity entity : codeAndMessages) {
            entityManager.persist(entity);
        }
    }

    @Override
    public List<CodeAndMessageJpaEntity> findAll() {
        return entityManager.createQuery(SELECT_ALL, CodeAndMessageJpaEntity.class)
            .getResultList();
    }
}
