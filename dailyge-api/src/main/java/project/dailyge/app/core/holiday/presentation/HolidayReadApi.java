package project.dailyge.app.core.holiday.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static project.dailyge.app.codeandmessage.CommonCodeAndMessage.OK;
import project.dailyge.app.common.annotation.PresentationLayer;
import project.dailyge.app.common.auth.DailygeUser;
import project.dailyge.app.common.auth.LoginUser;
import project.dailyge.app.common.response.ApiResponse;
import static project.dailyge.app.common.utils.DateUtils.getYearEndDate;
import static project.dailyge.app.common.utils.DateUtils.getYearStartDate;
import project.dailyge.app.core.holiday.application.HolidayReadService;
import project.dailyge.app.core.holiday.presentation.response.HolidayResponse;
import project.dailyge.app.core.holiday.presentation.validator.HolidayClientValidator;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(path = "/api/holidays")
@PresentationLayer(value = "HolidayReadApi")
public class HolidayReadApi {

    private final HolidayClientValidator validator;
    private final HolidayReadService holidayReadService;

    @GetMapping
    public ApiResponse<List<HolidayResponse>> searchHolidays(
        @LoginUser final DailygeUser dailygeUser,
        @RequestParam(name = "year") final int year
    ) {
        validator.validateYear(year);
        final List<HolidayResponse> payload =
            holidayReadService.findHolidaysByDate(dailygeUser.getCountryId(), getYearStartDate(year), getYearEndDate(year)).stream()
                .map(HolidayResponse::new)
                .toList();
        return ApiResponse.from(OK, payload);
    }
}
