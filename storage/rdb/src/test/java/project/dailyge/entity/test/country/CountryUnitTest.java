package project.dailyge.entity.test.country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.dailyge.entity.country.Country;

@DisplayName("[UnitTest] Country 엔티티 테스트")
class CountryUnitTest {

    @Test
    @DisplayName("올바른 인자가 들어오면 Country 객체가 생성된다.")
    void whenValidParameterThenCountryObjShouldBeCreated() {
        final Country newCountry = new Country(1L, "대한민국", "Korea", "410", "KR", "KOR");

        assertAll(
            () -> assertEquals(1L, newCountry.getId()),
            () -> assertEquals("대한민국", newCountry.getKrName()),
            () -> assertEquals("Korea", newCountry.getEnName()),
            () -> assertEquals("410", newCountry.getCode()),
            () -> assertEquals("KR", newCountry.getAlpha2()),
            () -> assertEquals("KOR", newCountry.getAlpha3())
        );
    }

    @Test
    @DisplayName("동일한 코드일 경우 equals가 true를 반환한다.")
    void whenSameCodeThenEqualsShouldReturnTrue() {
        final Country korea = new Country(1L, "대한민국", "Korea", "410", "KR", "KOR");
        final Country sameCountry = new Country(2L, "한국", "South Korea", "410", "KR", "KOR");

        assertThat(korea).isEqualTo(sameCountry);
    }

    @Test
    @DisplayName("서로 다른 코드일 경우 equals가 false를 반환한다.")
    void whenDifferentCodeThenEqualsShouldReturnFalse() {
        final Country korea = new Country(1L, "대한민국", "Korea", "410", "KR", "KOR");
        final Country us = new Country(2L, "미국", "USA", "840", "US", "USA");

        assertThat(korea).isNotEqualTo(us);
    }

    @Test
    @DisplayName("동일한 코드일 경우 해시코드가 동일하다.")
    void whenSameCodeThenHashCodeShouldBeSame() {
        final Country korea = new Country(1L, "대한민국", "Korea", "410", "KR", "KOR");
        final Country sameCountry = new Country(2L, "한국", "South Korea", "410", "KR", "KOR");

        assertThat(korea.hashCode()).isEqualTo(sameCountry.hashCode());
    }

    @Test
    @DisplayName("서로 다른 코드일 경우 해시코드가 다르다.")
    void whenDifferentCodeThenHashCodeShouldBeDifferent() {
        final Country korea = new Country(1L, "대한민국", "Korea", "410", "KR", "KOR");
        final Country us = new Country(2L, "미국", "USA", "840", "US", "USA");

        assertThat(korea.hashCode()).isNotEqualTo(us.hashCode());
    }
}
