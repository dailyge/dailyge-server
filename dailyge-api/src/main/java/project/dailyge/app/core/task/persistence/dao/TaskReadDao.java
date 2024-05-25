package project.dailyge.app.core.task.persistence.dao;

import org.springframework.stereotype.*;
import project.dailyge.app.core.task.persistence.*;
import project.dailyge.domain.user.*;

@Repository
public class TaskReadDao implements TaskEntityReadRepository {

    @Override
    public User findById(Long userId) {
        return null;
    }
}
