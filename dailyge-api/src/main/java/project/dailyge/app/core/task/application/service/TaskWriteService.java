package project.dailyge.app.core.task.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.core.task.application.TaskWriteUseCase;
import project.dailyge.app.core.task.application.command.TaskCreateCommand;
import project.dailyge.app.core.task.application.command.TaskStatusUpdateCommand;
import project.dailyge.app.core.task.application.command.TaskUpdateCommand;
import static project.dailyge.app.core.task.exception.TaskCodeAndMessage.TASK_NOT_FOUND;
import project.dailyge.app.core.task.exception.TaskTypeException;
import static project.dailyge.document.common.UuidGenerator.createTimeBasedUUID;
import project.dailyge.document.task.MonthlyTaskDocument;
import static project.dailyge.document.task.MonthlyTaskDocuments.createMonthlyDocuments;
import project.dailyge.document.task.TaskActivity;
import project.dailyge.document.task.TaskDocument;
import project.dailyge.document.task.TaskDocumentReadRepository;
import project.dailyge.document.task.TaskDocumentWriteRepository;
import project.dailyge.entity.common.EventPublisher;
import static project.dailyge.entity.common.EventType.CREATE;
import project.dailyge.entity.task.TaskEvent;
import static project.dailyge.entity.task.TaskEvent.createEvent;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
class TaskWriteService implements TaskWriteUseCase {

    private final EventPublisher<TaskEvent> eventPublisher;
    private final TaskValidator validator;
    private final TaskDocumentReadRepository taskReadRepository;
    private final TaskDocumentWriteRepository taskWriteRepository;

    @Override
    public void createMonthlyTasks(
        final DailygeUser dailygeUser,
        final LocalDate date
    ) {
        validator.validateMonthlyPlan(dailygeUser.getUserId());
        final List<MonthlyTaskDocument> monthlyTasks = createMonthlyDocuments(dailygeUser.getUserId(), date);
        taskWriteRepository.saveAll(monthlyTasks);
    }

    @Override
    public String save(
        final DailygeUser dailygeUser,
        final TaskCreateCommand command
    ) {
        final TaskDocument newTask = command.toDocument(dailygeUser);
        validator.validateTaskCreation(newTask.getUserId(), newTask.getLocalDate());
        final String newTaskId = taskWriteRepository.save(newTask, newTask.getLocalDate());

        final TaskEvent event = createEvent(newTask.getUserId(), createTimeBasedUUID(), CREATE);
        eventPublisher.publishExternalEvent(event);
        return newTaskId;
    }

    @Override
    public void update(
        final DailygeUser dailygeUser,
        final String taskId,
        final TaskUpdateCommand command
    ) {
        final TaskActivity findTaskActivity = taskReadRepository.findTaskDocumentByIds(
            dailygeUser.getId(), command.monthlyTaskId(), taskId
        ).orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        validator.validateAuth(dailygeUser, findTaskActivity);

        taskWriteRepository.update(
            dailygeUser.getId(),
            command.monthlyTaskId(),
            taskId,
            command.title(),
            command.content(),
            command.date(),
            command.getStatus()
        );
    }

    @Override
    public void updateStatus(
        final DailygeUser dailygeUser,
        final String taskId,
        final TaskStatusUpdateCommand command
    ) {
        final TaskActivity findTaskActivity = taskReadRepository.findTaskDocumentByIds(
            dailygeUser.getId(), command.monthlyTaskId(), taskId
        ).orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
        validator.validateAuth(dailygeUser, findTaskActivity);

        taskWriteRepository.update(dailygeUser.getId(), command.monthlyTaskId(), taskId, command.getStatus());
    }

    @Override
    @Transactional
    public void delete(
        final DailygeUser dailygeUser,
        final Long taskId
    ) {
//        final TaskJpaEntity findTask = taskReadRepository.findById(taskId)
//            .orElseThrow(() -> TaskTypeException.from(TASK_NOT_FOUND));
//        validator.validateAuth(dailygeUser, findTask);
//
//        findTask.delete();
    }
}
