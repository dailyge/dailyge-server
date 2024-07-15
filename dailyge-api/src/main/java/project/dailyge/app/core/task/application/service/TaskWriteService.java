package project.dailyge.app.core.task.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.exception.UnAuthorizedException;
import project.dailyge.app.core.common.external.EventPublisher;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.application.command.TaskUpdateCommand;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import project.dailyge.document.task.MonthlyTaskDocument;
import static project.dailyge.document.task.MonthlyTaskDocuments.createMonthlyDocuments;
import project.dailyge.document.task.TaskDocument;
import project.dailyge.document.task.TaskDocumentWriteRepository;
import project.dailyge.entity.common.Event;
import static project.dailyge.entity.common.EventType.CREATE;
import project.dailyge.entity.task.TaskEntityReadRepository;
import static project.dailyge.entity.task.TaskEvent.createEvent;
import project.dailyge.entity.task.TaskJpaEntity;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
class TaskWriteService implements TaskWriteUseCase {

    private final EventPublisher eventPublisher;
    private final TaskValidator validator;
    private final TaskEntityReadRepository taskReadRepository;
    private final TaskDocumentWriteRepository taskWriteRepository;

    @Override
    public void createMonthlyTasks(
        final DailygeUser dailygeUser,
        final LocalDate date
    ) {
        validator.validateYearPlan(dailygeUser.getUserId());
        final List<MonthlyTaskDocument> monthlyTasks = createMonthlyDocuments(dailygeUser.getUserId(), date);
        taskWriteRepository.saveAll(monthlyTasks);
    }

    @Override
    public String save(final TaskDocument task) {
        validator.validateTaskCreation(task.getUserId(), task.getLocalDate());
        final String newTaskId = taskWriteRepository.save(task, task.getLocalDate());

        final Event event = createEvent(task.getUserId(), task.getMonthlyTaskId(), CREATE);
        eventPublisher.publishExternalEvent(event);
        return newTaskId;
    }

    @Override
    @Transactional
    public void update(
        final DailygeUser dailygeUser,
        final Long taskId,
        final TaskUpdateCommand command
    ) {
        final TaskJpaEntity findTask = taskReadRepository.findById(taskId)
            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        if (!findTask.isOwner(dailygeUser.getUserId())) {
            throw new UnAuthorizedException();
        }
        findTask.update(command.title(), command.content(), command.date(), command.status());
    }

    @Override
    @Transactional
    public void delete(
        final DailygeUser dailygeUser,
        final Long taskId
    ) {
        final TaskJpaEntity findTask = taskReadRepository.findById(taskId)
            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        validator.validateAuth(dailygeUser, findTask);

        findTask.delete();
    }
}
