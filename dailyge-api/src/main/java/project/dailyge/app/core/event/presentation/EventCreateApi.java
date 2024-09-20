package project.dailyge.app.core.event.presentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.dailyge.app.codeandmessage.CommonCodeAndMessage;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import project.dailyge.app.core.event.facade.EventFacade;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static project.dailyge.app.common.utils.CookieUtils.createResponseCookie;

@PresentationLayer
@RequestMapping("/api")
public class EventCreateApi {

    private final String env;
    private final EventFacade eventFacade;

    public EventCreateApi(
        @Value("local") final String env,
        final EventFacade eventFacade
    ) {
        this.env = env;
        this.eventFacade = eventFacade;
    }

    @PostMapping(path = {"/events/{eventId}"})
    public ApiResponse<Void> createCouponEvent(
        @LoginUser final DailygeUser dailygeUser,
        @PathVariable(name = "eventId") final Long eventId
    ) {
        eventFacade.participateEvent(dailygeUser, eventId);
        final HttpHeaders headers = new HttpHeaders();
        headers.add(SET_COOKIE, createResponseCookie("isParticipated", "true", "/", 7L * 60L * 60L, true, env));
        return ApiResponse.from(CommonCodeAndMessage.CREATED, headers, null);
    }
}
