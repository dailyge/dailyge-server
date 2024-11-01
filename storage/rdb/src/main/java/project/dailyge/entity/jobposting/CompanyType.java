package project.dailyge.entity.jobposting;

public enum CompanyType {
    STARTUP("스타트업"),
    UNICORN("유니콘 기업"),
    SMALL_ENTERPRISE("중소기업"),
    MEDIUM_ENTERPRISE("중견기업"),
    LARGE_ENTERPRISE("대기업");

    private final String kr;

    CompanyType(final String kr) {
        this.kr = kr;
    }

    public String getKr() {
        return kr;
    }
}
