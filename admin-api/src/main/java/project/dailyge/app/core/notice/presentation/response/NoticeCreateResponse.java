package project.dailyge.app.core.notice.presentation.response;

public record NoticeCreateResponse(Long noticeId) {

    @Override
    public String toString() {
        return String.format("{\"noticeId\":\"%s\"}", noticeId);
    }
}
