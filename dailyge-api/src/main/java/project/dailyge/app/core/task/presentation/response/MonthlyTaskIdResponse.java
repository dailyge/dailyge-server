package project.dailyge.app.core.task.presentation.response;

import lombok.Getter;
import project.dailyge.document.task.MonthlyTaskDocument;

@Getter
public class MonthlyTaskIdResponse {

    private String monthlyTaskId;

    private MonthlyTaskIdResponse() {
    }

    public MonthlyTaskIdResponse(final MonthlyTaskDocument monthlyTaskDocument) {
        this.monthlyTaskId = monthlyTaskDocument.getId();
    }

    @Override
    public String toString() {
        return String.format("{\"monthlyTaskId\":\"%s\"}", monthlyTaskId);
    }
}
