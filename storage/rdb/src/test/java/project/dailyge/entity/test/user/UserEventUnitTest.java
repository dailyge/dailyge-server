package project.dailyge.entity.test.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.user.UserEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static project.dailyge.entity.common.EventType.CREATE;

@DisplayName("[UnitTest] UserEvent 단위 테스트")
public class UserEventUnitTest {

    private static final long PUBLISHER = 1L;
    private static final String EVENT_ID = "eventId";
    private static final String NICKNAME = "nickname";
    private static final String EMAIL = "dailyge@gmail.com";
    private static final String PROFILE_IMAGE_URL = "user.jpg";

    private UserEvent userEvent;

    @BeforeEach
    void setUp() {
        userEvent = UserEvent.createEvent(PUBLISHER, EVENT_ID, CREATE, NICKNAME, EMAIL, PROFILE_IMAGE_URL);
    }

    @Test
    @DisplayName("사용자 이벤트를 생성하면, 정상적으로 생성된다.")
    void whenCreateUserEventThenEventShouldBeGeneratedNormally() {
        assertAll(
            () -> assertEquals(PUBLISHER, userEvent.getPublisher()),
            () -> assertEquals(EVENT_ID, userEvent.getEventId()),
            () -> assertEquals(CREATE, userEvent.getEventType()),
            () -> assertEquals(NICKNAME, userEvent.getNickname()),
            () -> assertEquals(EMAIL, userEvent.getEmail()),
            () -> assertEquals(PROFILE_IMAGE_URL, userEvent.getProfileImageUrl()),
            () -> assertEquals("user", userEvent.getDomain())
        );
    }

    @Test
    @DisplayName("publisher가 비어있을 경우, IllegalArgumentException이 발생한다.")
    void whenPublisherIsNullThenIllegalArgumentExceptionShouldBeHappen() {
        assertThatThrownBy(() -> UserEvent.createEvent(null, EVENT_ID, CREATE, NICKNAME, EMAIL, PROFILE_IMAGE_URL))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(userEvent.getInvalidPublisherIdErrorMessage());
    }

    @Test
    @DisplayName("eventId가 비어있을 경우, IllegalArgumentException이 발생한다.")
    void whenEventIdIsNullThenIllegalArgumentExceptionShouldBeHappen() {
        assertThatThrownBy(() -> UserEvent.createEvent(PUBLISHER, null, CREATE, NICKNAME, EMAIL, PROFILE_IMAGE_URL))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(userEvent.getInvalidEventIdErrorMessage());
    }

    @Test
    @DisplayName("eventType이 비어있을 경우, IllegalArgumentException이 발생한다.")
    void whenEventTypeIsNullThenIllegalArgumentExceptionShouldBeHappen() {
        assertThatThrownBy(() -> UserEvent.createEvent(PUBLISHER, EVENT_ID, null, NICKNAME, EMAIL, PROFILE_IMAGE_URL))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(userEvent.getInvalidEventTypeErrorMessage());
    }

    @Test
    @DisplayName("toString 메서드는 정의한 형식대로 JSON 문자열을 반환한다.")
    void whenToStringThenReturnJsonString() {
        final String expectedString = "{"
            + "\"publisher\":\"" + PUBLISHER + "\","
            + "\"eventId\":\"" + EVENT_ID + "\","
            + "\"eventType\":\"" + CREATE + "\","
            + "\"nickname\":\"" + NICKNAME + "\","
            + "\"email\":\"" + EMAIL + "\","
            + "\"profileImageUrl\":\"" + PROFILE_IMAGE_URL + "\","
            + "\"domain\":\"" + "user" + "\","
            + "\"createdAt\":\"" + userEvent.getCreatedAt() + "\""
            + "}";

        assertThat(userEvent.toString()).hasToString(expectedString);
    }
}
