package project.dailyge.app.core.event.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import project.dailyge.document.event.EventDocument;
import project.dailyge.document.event.EventDocumentWriteRepository;

@Repository
@RequiredArgsConstructor
public class EventWriteDao implements EventDocumentWriteRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public String save(final EventDocument event) {
        mongoTemplate.save(event);
        return event.getEventId();
    }
}
