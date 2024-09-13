package project.dailyge.app.coupon.exception;

import project.dailyge.app.codeandmessage.CodeAndMessage;

public enum CouponCodeAndMessage implements CodeAndMessage {
    DUPLICATED_WINNER_SELECTION(409, "이미 실행된 당첨자 선정 작업입니다."),
    COUPON_UN_RESOLVED_EXCEPTION(500, "쿠폰 관련 작업이 실패했습니다.");
    private final int code;
    private final String message;

    CouponCodeAndMessage(
        final int code,
        final String message
    ) {
        this.code = code;
        this.message = message;
    }


    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
