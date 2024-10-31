package project.dailyge.app.core.event.persistence;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.document.event.EventDocument;
import project.dailyge.document.event.EventDocumentWriteRepository;

@Repository
public class EventDocumentWriteDao implements EventDocumentWriteRepository {

    private final MongoTemplate mongoTemplate;

    public EventDocumentWriteDao(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String save(final EventDocument event) {
        mongoTemplate.save(event);
        return event.getEventId();
    }
}
