package project.dailyge.app.core.user.application.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.dailyge.app.core.user.application.UserReadUseCase;
import project.dailyge.app.core.user.application.service.UserWriteService;
//import project.dailyge.app.core.user.dto.request.UserRegisterRequest;
import project.dailyge.app.core.user.exception.UserTypeException;
import project.dailyge.domain.user.UserJpaEntity;

import static project.dailyge.app.core.user.exception.UserCodeAndMessage.USER_EMAIL_CONFLICT;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserReadUseCase userReadUseCase;
    private final UserWriteService userWriteService;

//    @Transactional(rollbackFor = Exception.class)
//    public UserJpaEntity save(final UserRegisterRequest request) {
//        UserJpaEntity findUser = userReadUseCase.findByEmail(request.email());
//        if (findUser != null) {
//            throw UserTypeException.from(USER_EMAIL_CONFLICT);
//        }
//        return userWriteService.save(request.toEntity());
//    }
}
