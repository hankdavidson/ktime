package org.hankster.time

import org.junit.Test
import java.time.*
import java.time.DayOfWeek.*
import java.time.Month.*
import java.time.temporal.ChronoField
import java.time.temporal.ChronoField.*
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.*
import kotlin.test.*

infix  fun <T> T.shouldEqual(expected: T) { expect(expected) { this }}
class TimeTest {

  @Test
  fun `conversion expectations`() {
    1.nanos shouldEqual Duration.ofNanos(1)
    1.micros shouldEqual Duration.ofNanos(1000)
    1.millis shouldEqual Duration.ofMillis(1)
    1.seconds shouldEqual Duration.ofSeconds(1)
    1.minutes shouldEqual Duration.ofMinutes(1)
    1.hours shouldEqual Duration.ofHours(1)
    1.days shouldEqual Period.ofDays(1)
    1.weeks shouldEqual Period.ofWeeks(1)
    1.months shouldEqual Period.ofMonths(1)
    1.years shouldEqual Period.ofYears(1)
    1L.nanos shouldEqual Duration.ofNanos(1)
    1L.micros shouldEqual Duration.ofNanos(1000L)
    1L.millis shouldEqual Duration.ofMillis(1)
    1L.seconds shouldEqual Duration.ofSeconds(1)
    1L.minutes shouldEqual Duration.ofMinutes(1)
    1L.hours shouldEqual Duration.ofHours(1)
    1L.days shouldEqual Period.ofDays(1)
    1L.weeks shouldEqual Period.ofWeeks(1)
    1L.months shouldEqual Period.ofMonths(1)
    1L.years shouldEqual Period.ofYears(1)
    2.5.nanos shouldEqual Duration.ofNanos(2)
    2.5.micros shouldEqual Duration.ofNanos(2500)
    2.5.millis shouldEqual Duration.ofNanos(2500000)
    2.5.seconds shouldEqual Duration.ofSeconds(2, 500000000)
    2.5.minutes shouldEqual Duration.ofSeconds(150)
    2.5.hours shouldEqual Duration.ofMinutes(150)
  }

  @Test
  fun `TemporalAmount operator expectations`() {
    -(1.seconds) shouldEqual Duration.ofSeconds(-1)
    (-1).seconds shouldEqual Duration.ofSeconds(-1)
    1.seconds * -1 shouldEqual Duration.ofSeconds(-1)
    2.hours + 3.minutes + 4.seconds + 5.millis + 6.nanos shouldEqual Duration.ofNanos(7384005000006)
    1.seconds * 2 shouldEqual Duration.ofSeconds(2)
    2.seconds / 2 shouldEqual Duration.ofSeconds(1)
    -(1.days) shouldEqual Period.ofDays(-1)
    (-1).days shouldEqual Period.ofDays(-1)
    1.days * -1 shouldEqual Period.ofDays(-1)
    1.years + 1.months + 1.weeks + 1.days shouldEqual Period.of(1, 1, 8)
    1.years * 2 shouldEqual Period.ofYears(2)

    // Duration is comparable, Period is not
    assertTrue { 1.minutes < 1.hours }
    assertTrue { 1.minutes <= 60.seconds }
    assertTrue { 1000.nanos == 1.micros }
  }

  @Test
  fun `conversions to OffsetDateTime`() {
    assertTrue { 2.hours.ago < OffsetDateTime.now(ZoneOffset.UTC) }
    assertTrue { 2.hours.fromNow > OffsetDateTime.now(ZoneOffset.UTC) }
    assertTrue { 2.days.ago < 2.hours.ago }
    assertTrue { 2.days.fromNow > 2.hours.fromNow }
  }

  @Test
  fun `destructuring assignment`() {
    val (sec, nano) = 2.5.seconds
    sec shouldEqual 2
    nano shouldEqual 500000000

    val (y, m, d) = 1.years + 2.months + 3.days
    y shouldEqual 1
    m shouldEqual 2
    d shouldEqual 3
  }

  @Test
  fun `operations on Year`() {

    val aYear = Year.of(2016)!!

    2016.asYear shouldEqual Year.of(2016)

    aYear.precision shouldEqual YEARS
    aYear.yr shouldEqual Year.of(2016)

    aYear + JUNE shouldEqual YearMonth.of(2016, 6)

    aYear + 1 shouldEqual Year.of(2017)
    aYear - 1 shouldEqual Year.of(2015)

    aYear + 1.years shouldEqual Year.of(2017)
    aYear - 1.years shouldEqual Year.of(2015)

    aYear[YEAR] shouldEqual 2016

    var aYearVar = Year.of(2016)!!
    ++aYearVar shouldEqual Year.of(2017)
    aYearVar shouldEqual Year.of(2017)

    aYearVar = Year.of(2016)!!
    aYearVar++ shouldEqual Year.of(2016)
    aYearVar shouldEqual Year.of(2017)

    aYearVar = Year.of(2016)!!
    --aYearVar shouldEqual Year.of(2015)
    aYearVar shouldEqual Year.of(2015)

    aYearVar = Year.of(2016)!!
    aYearVar-- shouldEqual Year.of(2016)
    aYearVar shouldEqual Year.of(2015)

    assertTrue { aYear supports ChronoField.YEAR }
    assertTrue { aYear supports ChronoUnit.YEARS }
    assertFalse { aYear supports ChronoField.MONTH_OF_YEAR }
    assertFalse { aYear supports ChronoUnit.MONTHS }
  }

  @Test
  fun `operations on YearMonth`() {

    val aYearMonth = YearMonth.of(2016, 6)!!

    aYearMonth.precision shouldEqual MONTHS
    aYearMonth.yr shouldEqual Year.of(2016)
    aYearMonth.month shouldEqual JUNE
    aYearMonth.yearMonth shouldEqual YearMonth.of(2016, 6)

    aYearMonth + 1.years + 1.months shouldEqual YearMonth.of(2017, 7)

    aYearMonth[YEAR] shouldEqual 2016
    aYearMonth[MONTH_OF_YEAR] shouldEqual 6

    assertTrue { aYearMonth supports ChronoField.YEAR }
    assertTrue { aYearMonth supports ChronoUnit.YEARS }
    assertTrue { aYearMonth supports ChronoField.MONTH_OF_YEAR }
    assertTrue { aYearMonth supports ChronoUnit.MONTHS }
    assertFalse { aYearMonth supports ChronoField.DAY_OF_MONTH }
    assertFalse { aYearMonth supports ChronoUnit.DAYS }
  }

  @Test
  fun `operations on Month`() {

    6.asMonth shouldEqual JUNE

    JUNE.precision shouldEqual MONTHS
    JUNE.month shouldEqual JUNE

    JUNE + 1 shouldEqual JULY
    JUNE - 1 shouldEqual MAY
    DECEMBER + 1 shouldEqual JANUARY
    JANUARY - 1 shouldEqual DECEMBER

    JUNE[MONTH_OF_YEAR] shouldEqual 6

    var aMonthVar = JUNE
    ++aMonthVar shouldEqual JULY
    aMonthVar shouldEqual JULY

    aMonthVar = JUNE
    aMonthVar++ shouldEqual JUNE
    aMonthVar shouldEqual JULY

    aMonthVar = JUNE
    --aMonthVar shouldEqual MAY
    aMonthVar shouldEqual MAY

    aMonthVar = JUNE
    aMonthVar-- shouldEqual JUNE
    aMonthVar shouldEqual MAY

    assertFalse { JUNE supports ChronoField.YEAR }
    assertTrue { JUNE supports ChronoField.MONTH_OF_YEAR }
  }

  @Test
  fun `operations on MonthDay`() {

    val aMonthDay = MonthDay.of(6, 15)!!

    aMonthDay.month shouldEqual JUNE
    aMonthDay.dayOfMonth shouldEqual 15

    aMonthDay[MONTH_OF_YEAR] shouldEqual 6
    aMonthDay[DAY_OF_MONTH] shouldEqual 15

    assertFalse { aMonthDay supports ChronoField.YEAR }
    assertTrue { aMonthDay supports ChronoField.MONTH_OF_YEAR }
    assertTrue { aMonthDay supports ChronoField.DAY_OF_MONTH }
  }

  @Test
  fun `operations on DayOfWeek`() {

    3.asDayOfWeek shouldEqual WEDNESDAY

    MONDAY.precision shouldEqual DAYS

    MONDAY + 1 shouldEqual TUESDAY
    MONDAY - 1 shouldEqual SUNDAY

    MONDAY.dayOfWeek shouldEqual MONDAY

    MONDAY[DAY_OF_WEEK] shouldEqual 1

    assertTrue { MONDAY supports ChronoField.DAY_OF_WEEK }
    assertFalse { MONDAY supports ChronoField.DAY_OF_MONTH }

    var aDayOfWeekVar = WEDNESDAY
    ++aDayOfWeekVar shouldEqual THURSDAY
    aDayOfWeekVar shouldEqual THURSDAY

    aDayOfWeekVar = WEDNESDAY
    aDayOfWeekVar++ shouldEqual WEDNESDAY
    aDayOfWeekVar shouldEqual THURSDAY

    aDayOfWeekVar = WEDNESDAY
    --aDayOfWeekVar shouldEqual TUESDAY
    aDayOfWeekVar shouldEqual TUESDAY

    aDayOfWeekVar = WEDNESDAY
    aDayOfWeekVar-- shouldEqual WEDNESDAY
    aDayOfWeekVar shouldEqual TUESDAY

  }

  @Test
  fun `operations on Instant`() {

    val anInstant = OffsetDateTime.of(2017, 1, 2, 13, 4, 5, 6000000, ZoneOffset.UTC).toInstant()!!

    anInstant.precision shouldEqual NANOS
    anInstant.instant shouldEqual OffsetDateTime.of(2017, 1, 2, 13, 4, 5, 6000000, ZoneOffset.UTC).toInstant()

    anInstant + 1.seconds shouldEqual OffsetDateTime.of(2017, 1, 2, 13, 4, 6, 6000000, ZoneOffset.UTC).toInstant()

    anInstant[NANO_OF_SECOND] shouldEqual 6000000

    assertTrue { anInstant supports ChronoField.NANO_OF_SECOND }
    assertTrue { anInstant supports ChronoField.MICRO_OF_SECOND }
    assertTrue { anInstant supports ChronoField.MILLI_OF_SECOND }
    assertTrue { anInstant supports ChronoField.INSTANT_SECONDS }
    assertFalse { anInstant supports ChronoField.SECOND_OF_MINUTE }
    assertTrue { anInstant supports ChronoUnit.NANOS }
    assertTrue { anInstant supports ChronoUnit.MICROS }
    assertTrue { anInstant supports ChronoUnit.MILLIS }
    assertTrue { anInstant supports ChronoUnit.SECONDS }
    assertTrue { anInstant supports ChronoUnit.MINUTES }
    assertTrue { anInstant supports ChronoUnit.HOURS }
    assertTrue { anInstant supports ChronoUnit.DAYS }
    assertFalse { anInstant supports ChronoUnit.MONTHS }
  }

  @Test
  fun `operations on LocalDate`() {
    val aLocalDate = LocalDate.of(2017, 1, 2)!!

    aLocalDate.precision shouldEqual DAYS

    aLocalDate.localDate shouldEqual aLocalDate
    aLocalDate.yr shouldEqual 2017.asYear
    aLocalDate.yearMonth shouldEqual 2017.asYear + JANUARY
    aLocalDate.month shouldEqual JANUARY
    aLocalDate.monthDay shouldEqual MonthDay.of(1, 2)
    aLocalDate.dayOfWeek shouldEqual MONDAY
    aLocalDate.localDate shouldEqual LocalDate.of(2017, 1, 2)

    aLocalDate.atStartOfMonth shouldEqual LocalDate.of(2017, 1, 1)
    aLocalDate.atStartOfNextMonth shouldEqual LocalDate.of(2017, 2, 1)
    aLocalDate.atEndOfMonth shouldEqual LocalDate.of(2017, 1, 31)
    aLocalDate.atStartOfYear shouldEqual LocalDate.of(2017, 1, 1)
    aLocalDate.atStartOfNextYear shouldEqual LocalDate.of(2018, 1, 1)
    aLocalDate.atEndOfYear shouldEqual LocalDate.of(2017, 12, 31)

    aLocalDate + 1.years + 2.months + 3.days shouldEqual LocalDate.of(2018, 3, 5)

    aLocalDate[YEAR] shouldEqual 2017
    aLocalDate[MONTH_OF_YEAR] shouldEqual 1
    aLocalDate[DAY_OF_MONTH] shouldEqual 2

    assertFalse { aLocalDate supports ChronoUnit.HOURS }
    assertFalse { aLocalDate supports ChronoField.HOUR_OF_DAY }
    assertTrue { aLocalDate supports ChronoUnit.DAYS }
    assertTrue { aLocalDate supports ChronoField.DAY_OF_MONTH }
    assertTrue { aLocalDate supports ChronoUnit.MONTHS }
    assertTrue { aLocalDate supports ChronoField.MONTH_OF_YEAR }
    assertTrue { aLocalDate supports ChronoUnit.YEARS }
    assertTrue { aLocalDate supports ChronoField.YEAR }
  }

  @Test
  fun `operations on LocalTime`() {
    val aLocalTime = LocalTime.of(13, 4, 5)!!

    aLocalTime.precision shouldEqual NANOS

    aLocalTime.localTime shouldEqual LocalTime.of(13, 4, 5)

    aLocalTime + 1.hours + 2.minutes + 3.seconds shouldEqual LocalTime.of(14, 6, 8)

    aLocalTime[HOUR_OF_DAY] shouldEqual 13
    aLocalTime[HOUR_OF_AMPM] shouldEqual 1
    aLocalTime[AMPM_OF_DAY] shouldEqual 1
    aLocalTime[MINUTE_OF_HOUR] shouldEqual 4
    aLocalTime[SECOND_OF_MINUTE] shouldEqual 5

    assertTrue { aLocalTime supports ChronoUnit.SECONDS }
    assertTrue { aLocalTime supports ChronoField.SECOND_OF_MINUTE }
    assertTrue { aLocalTime supports ChronoUnit.MINUTES }
    assertTrue { aLocalTime supports ChronoField.MINUTE_OF_HOUR }
    assertTrue { aLocalTime supports ChronoUnit.HOURS }
    assertTrue { aLocalTime supports ChronoField.HOUR_OF_DAY }
    assertFalse { aLocalTime supports ChronoUnit.DAYS }
    assertFalse { aLocalTime supports ChronoField.DAY_OF_MONTH }
    assertFalse { aLocalTime supports ChronoField.OFFSET_SECONDS }
  }

  @Test
  fun `operations on LocalDateTime`() {
    val aLocalDateTime = LocalDateTime.of(2017, 1, 2, 13, 4, 5)!!

    aLocalDateTime.precision shouldEqual NANOS

    aLocalDateTime.yr shouldEqual 2017.asYear
    aLocalDateTime.yearMonth shouldEqual 2017.asYear + JANUARY
    aLocalDateTime.month shouldEqual JANUARY
    aLocalDateTime.monthDay shouldEqual MonthDay.of(1, 2)
    aLocalDateTime.dayOfWeek shouldEqual MONDAY
    aLocalDateTime.localDate shouldEqual LocalDate.of(2017, 1, 2)
    aLocalDateTime.localTime shouldEqual LocalTime.of(13, 4, 5)
    aLocalDateTime.localDateTime shouldEqual LocalDateTime.of(2017, 1, 2, 13, 4, 5)

    aLocalDateTime.atStartOfMonth shouldEqual LocalDateTime.of(2017, 1, 1, 13, 4, 5)
    aLocalDateTime.atStartOfNextMonth shouldEqual LocalDateTime.of(2017, 2, 1, 13, 4, 5)
    aLocalDateTime.atEndOfMonth shouldEqual LocalDateTime.of(2017, 1, 31, 13, 4, 5)
    aLocalDateTime.atStartOfYear shouldEqual LocalDateTime.of(2017, 1, 1, 13, 4, 5)
    aLocalDateTime.atStartOfNextYear shouldEqual LocalDateTime.of(2018, 1, 1, 13, 4, 5)
    aLocalDateTime.atEndOfYear shouldEqual LocalDateTime.of(2017, 12, 31, 13, 4, 5)

    aLocalDateTime + 1.years + 2.months + 3.days +
        4.hours + 5.minutes + 6.seconds shouldEqual LocalDateTime.of(2018, 3, 5, 17, 9, 11)

    aLocalDateTime[YEAR] shouldEqual 2017
    aLocalDateTime[MONTH_OF_YEAR] shouldEqual 1
    aLocalDateTime[DAY_OF_MONTH] shouldEqual 2
    aLocalDateTime[HOUR_OF_DAY] shouldEqual 13
    aLocalDateTime[HOUR_OF_AMPM] shouldEqual 1
    aLocalDateTime[AMPM_OF_DAY] shouldEqual 1
    aLocalDateTime[MINUTE_OF_HOUR] shouldEqual 4
    aLocalDateTime[SECOND_OF_MINUTE] shouldEqual 5

    assertTrue { aLocalDateTime supports ChronoUnit.SECONDS }
    assertTrue { aLocalDateTime supports ChronoField.SECOND_OF_MINUTE }
    assertTrue { aLocalDateTime supports ChronoUnit.MINUTES }
    assertTrue { aLocalDateTime supports ChronoField.MINUTE_OF_HOUR }
    assertTrue { aLocalDateTime supports ChronoUnit.HOURS }
    assertTrue { aLocalDateTime supports ChronoField.HOUR_OF_DAY }
    assertTrue { aLocalDateTime supports ChronoUnit.DAYS }
    assertTrue { aLocalDateTime supports ChronoField.DAY_OF_MONTH }
    assertTrue { aLocalDateTime supports ChronoUnit.MONTHS }
    assertTrue { aLocalDateTime supports ChronoField.MONTH_OF_YEAR }
    assertTrue { aLocalDateTime supports ChronoUnit.YEARS }
    assertTrue { aLocalDateTime supports ChronoField.YEAR }
    assertFalse { aLocalDateTime supports ChronoField.OFFSET_SECONDS }
  }

  @Test
  fun `operations on OffsetTime`() {
    val anOffsetTime = OffsetTime.of(13, 4, 5, 6, ZoneOffset.ofHours(-7))!!

    anOffsetTime.precision shouldEqual NANOS

    anOffsetTime.localTime shouldEqual LocalTime.of(13, 4, 5, 6)
    anOffsetTime.offsetTime shouldEqual OffsetTime.of(13, 4, 5, 6, ZoneOffset.ofHours(-7))

    anOffsetTime + 1.hours + 2.minutes + 3.seconds shouldEqual OffsetTime.of(14, 6, 8, 6, ZoneOffset.ofHours(-7))

    anOffsetTime[HOUR_OF_DAY] shouldEqual 13
    anOffsetTime[HOUR_OF_AMPM] shouldEqual 1
    anOffsetTime[AMPM_OF_DAY] shouldEqual 1
    anOffsetTime[MINUTE_OF_HOUR] shouldEqual 4
    anOffsetTime[SECOND_OF_MINUTE] shouldEqual 5
    anOffsetTime[OFFSET_SECONDS] shouldEqual -7 * 60 * 60

    assertTrue { anOffsetTime supports ChronoUnit.SECONDS }
    assertTrue { anOffsetTime supports ChronoField.SECOND_OF_MINUTE }
    assertTrue { anOffsetTime supports ChronoUnit.MINUTES }
    assertTrue { anOffsetTime supports ChronoField.MINUTE_OF_HOUR }
    assertTrue { anOffsetTime supports ChronoUnit.HOURS }
    assertTrue { anOffsetTime supports ChronoField.HOUR_OF_DAY }
    assertFalse { anOffsetTime supports ChronoUnit.DAYS }
    assertFalse { anOffsetTime supports ChronoField.DAY_OF_MONTH }
    assertTrue { anOffsetTime supports ChronoField.OFFSET_SECONDS }
  }

  @Test
  fun `operations on OffsetDateTime`() {
    val anOffsetDateTime = OffsetDateTime.of(2017, 1, 2, 13, 4, 5, 6, ZoneOffset.ofHours(-7))!!

    anOffsetDateTime.precision shouldEqual NANOS

    anOffsetDateTime.yr shouldEqual 2017.asYear
    anOffsetDateTime.yearMonth shouldEqual 2017.asYear + JANUARY
    anOffsetDateTime.month shouldEqual Month.of(1)
    anOffsetDateTime.monthDay shouldEqual MonthDay.of(1, 2)
    anOffsetDateTime.dayOfWeek shouldEqual MONDAY
    anOffsetDateTime.instant shouldEqual Instant.ofEpochSecond(1483387445, 6)
    anOffsetDateTime.localTime shouldEqual LocalTime.of(13, 4, 5, 6)
    anOffsetDateTime.localDate shouldEqual LocalDate.of(2017, 1, 2)
    anOffsetDateTime.localDateTime shouldEqual LocalDateTime.of(2017, 1, 2, 13, 4, 5, 6)
    anOffsetDateTime.offsetTime shouldEqual OffsetTime.of(13, 4, 5, 6, ZoneOffset.ofHours(-7))
    anOffsetDateTime.offsetDateTime shouldEqual OffsetDateTime.of(2017, 1, 2, 13, 4, 5, 6, ZoneOffset.ofHours(-7))
    anOffsetDateTime.zoneOffset shouldEqual ZoneOffset.ofHours(-7)
    anOffsetDateTime.zoneId shouldEqual ZoneOffset.ofHours(-7)
    anOffsetDateTime.zone shouldEqual ZoneOffset.ofHours(-7)

    anOffsetDateTime.atStartOfMonth shouldEqual OffsetDateTime.of(2017, 1, 1, 13, 4, 5, 6, ZoneOffset.ofHours(-7))
    anOffsetDateTime.atStartOfNextMonth shouldEqual OffsetDateTime.of(2017, 2, 1, 13, 4, 5, 6, ZoneOffset.ofHours(-7))
    anOffsetDateTime.atEndOfMonth shouldEqual OffsetDateTime.of(2017, 1, 31, 13, 4, 5, 6, ZoneOffset.ofHours(-7))
    anOffsetDateTime.atStartOfYear shouldEqual OffsetDateTime.of(2017, 1, 1, 13, 4, 5, 6, ZoneOffset.ofHours(-7))
    anOffsetDateTime.atStartOfNextYear shouldEqual OffsetDateTime.of(2018, 1, 1, 13, 4, 5, 6, ZoneOffset.ofHours(-7))
    anOffsetDateTime.atEndOfYear shouldEqual OffsetDateTime.of(2017, 12, 31, 13, 4, 5, 6, ZoneOffset.ofHours(-7))

    anOffsetDateTime + 1.years + 2.months + 3.days +
        4.hours + 5.minutes + 6.seconds shouldEqual OffsetDateTime.of(2018, 3, 5, 17, 9, 11, 6,
        ZoneOffset.ofHours(-7))
    anOffsetDateTime[YEAR] shouldEqual 2017
    anOffsetDateTime[MONTH_OF_YEAR] shouldEqual 1
    anOffsetDateTime[DAY_OF_MONTH] shouldEqual 2
    anOffsetDateTime[HOUR_OF_DAY] shouldEqual 13
    anOffsetDateTime[HOUR_OF_AMPM] shouldEqual 1
    anOffsetDateTime[AMPM_OF_DAY] shouldEqual 1
    anOffsetDateTime[MINUTE_OF_HOUR] shouldEqual 4
    anOffsetDateTime[SECOND_OF_MINUTE] shouldEqual 5
    anOffsetDateTime[OFFSET_SECONDS] shouldEqual -7 * 60 * 60

    assertTrue { anOffsetDateTime supports ChronoUnit.SECONDS }
    assertTrue { anOffsetDateTime supports ChronoField.SECOND_OF_MINUTE }
    assertTrue { anOffsetDateTime supports ChronoUnit.MINUTES }
    assertTrue { anOffsetDateTime supports ChronoField.MINUTE_OF_HOUR }
    assertTrue { anOffsetDateTime supports ChronoUnit.HOURS }
    assertTrue { anOffsetDateTime supports ChronoField.HOUR_OF_DAY }
    assertTrue { anOffsetDateTime supports ChronoUnit.DAYS }
    assertTrue { anOffsetDateTime supports ChronoField.DAY_OF_MONTH }
    assertTrue { anOffsetDateTime supports ChronoUnit.MONTHS }
    assertTrue { anOffsetDateTime supports ChronoField.MONTH_OF_YEAR }
    assertTrue { anOffsetDateTime supports ChronoUnit.YEARS }
    assertTrue { anOffsetDateTime supports ChronoField.YEAR }
    assertTrue { anOffsetDateTime supports ChronoField.OFFSET_SECONDS }
  }

  @Test
  fun `operations on ZoneOffset`() {
    val aZoneOffset = ZoneOffset.ofHours(-7)!!

    aZoneOffset.zoneOffset shouldEqual ZoneOffset.ofHours(-7)
    aZoneOffset.zone shouldEqual ZoneOffset.ofHours(-7)

    assertTrue { aZoneOffset supports ChronoField.OFFSET_SECONDS }
  }

  @Test
  fun `operations on ZonedDateTime`() {
    val aZonedDateTime = ZonedDateTime.of(2017, 1, 2, 13, 4, 5, 6, ZoneId.of("America/Denver"))!!

    aZonedDateTime.precision shouldEqual NANOS

    aZonedDateTime.yr shouldEqual 2017.asYear
    aZonedDateTime.yearMonth shouldEqual 2017.asYear + JANUARY
    aZonedDateTime.month shouldEqual Month.of(1)
    aZonedDateTime.monthDay shouldEqual MonthDay.of(1, 2)
    aZonedDateTime.dayOfWeek shouldEqual MONDAY
    aZonedDateTime.instant shouldEqual Instant.ofEpochSecond(1483387445, 6)
    aZonedDateTime.localTime shouldEqual LocalTime.of(13, 4, 5, 6)
    aZonedDateTime.localDate shouldEqual LocalDate.of(2017, 1, 2)
    aZonedDateTime.localDateTime shouldEqual LocalDateTime.of(2017, 1, 2, 13, 4, 5, 6)
    aZonedDateTime.offsetTime shouldEqual OffsetTime.of(13, 4, 5, 6, ZoneOffset.ofHours(-7))
    aZonedDateTime.offsetDateTime shouldEqual OffsetDateTime.of(2017, 1, 2, 13, 4, 5, 6, ZoneOffset.ofHours(-7))
    aZonedDateTime.zoneOffset shouldEqual ZoneOffset.ofHours(-7)
    aZonedDateTime.zoneId shouldEqual ZoneId.of("America/Denver")
    aZonedDateTime.zone shouldEqual ZoneId.of("America/Denver")
    aZonedDateTime.zonedDateTime shouldEqual ZonedDateTime.of(2017, 1, 2, 13, 4, 5, 6, ZoneId.of("America/Denver"))

    aZonedDateTime.atStartOfMonth shouldEqual ZonedDateTime.of(2017, 1, 1, 13, 4, 5, 6, ZoneId.of("America/Denver"))
    aZonedDateTime.atStartOfNextMonth shouldEqual ZonedDateTime.of(2017, 2, 1, 13, 4, 5, 6, ZoneId.of("America/Denver"))
    aZonedDateTime.atEndOfMonth shouldEqual ZonedDateTime.of(2017, 1, 31, 13, 4, 5, 6, ZoneId.of("America/Denver"))
    aZonedDateTime.atStartOfYear shouldEqual ZonedDateTime.of(2017, 1, 1, 13, 4, 5, 6, ZoneId.of("America/Denver"))
    aZonedDateTime.atStartOfNextYear shouldEqual ZonedDateTime.of(2018, 1, 1, 13, 4, 5, 6, ZoneId.of("America/Denver"))
    aZonedDateTime.atEndOfYear shouldEqual ZonedDateTime.of(2017, 12, 31, 13, 4, 5, 6, ZoneId.of("America/Denver"))

    aZonedDateTime + 1.years + 2.months + 3.days +
        4.hours + 5.minutes + 6.seconds shouldEqual ZonedDateTime.of(2018, 3, 5, 17, 9, 11, 6, ZoneId.of("America/Denver"))
    aZonedDateTime[YEAR] shouldEqual 2017
    aZonedDateTime[MONTH_OF_YEAR] shouldEqual 1
    aZonedDateTime[DAY_OF_MONTH] shouldEqual 2
    aZonedDateTime[HOUR_OF_DAY] shouldEqual 13
    aZonedDateTime[HOUR_OF_AMPM] shouldEqual 1
    aZonedDateTime[AMPM_OF_DAY] shouldEqual 1
    aZonedDateTime[MINUTE_OF_HOUR] shouldEqual 4
    aZonedDateTime[SECOND_OF_MINUTE] shouldEqual 5
    aZonedDateTime[OFFSET_SECONDS] shouldEqual -7 * 60 * 60
    
    assertTrue { aZonedDateTime supports ChronoUnit.SECONDS }
    assertTrue { aZonedDateTime supports ChronoField.SECOND_OF_MINUTE }
    assertTrue { aZonedDateTime supports ChronoUnit.MINUTES }
    assertTrue { aZonedDateTime supports ChronoField.MINUTE_OF_HOUR }
    assertTrue { aZonedDateTime supports ChronoUnit.HOURS }
    assertTrue { aZonedDateTime supports ChronoField.HOUR_OF_DAY }
    assertTrue { aZonedDateTime supports ChronoUnit.DAYS }
    assertTrue { aZonedDateTime supports ChronoField.DAY_OF_MONTH }
    assertTrue { aZonedDateTime supports ChronoUnit.MONTHS }
    assertTrue { aZonedDateTime supports ChronoField.MONTH_OF_YEAR }
    assertTrue { aZonedDateTime supports ChronoUnit.YEARS }
    assertTrue { aZonedDateTime supports ChronoField.YEAR }
    assertTrue { aZonedDateTime supports ChronoField.OFFSET_SECONDS }
  }

}
