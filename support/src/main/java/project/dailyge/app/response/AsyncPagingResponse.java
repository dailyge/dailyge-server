package project.dailyge.app.response;

import java.util.List;

public record AsyncPagingResponse<T>(
    List<T> data,
    int totalCount
) {
}
