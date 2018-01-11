package org.hankster.time

import java.time.*
import java.time.temporal.*

val Long.nanos get() = Duration.ofNanos(this)!!
val Long.micros get() = Duration.ofNanos(this * 1000L)
val Long.millis get() = Duration.ofMillis(this)!!
val Long.seconds get() = Duration.ofSeconds(this)!!
val Long.minutes get() = Duration.ofMinutes(this)!!
val Long.hours get() = Duration.ofHours(this)!!
val Int.nanos get() = toLong().nanos
val Int.micros get() = toLong().micros
val Int.millis get() = toLong().millis
val Int.seconds get() = toLong().seconds
val Int.minutes get() = toLong().minutes
val Int.hours get() = toLong().hours
val Double.nanos get() = this.toLong().nanos
val Double.micros get() = (this * 1000).nanos
val Double.millis get() = (this * 1000000).nanos
val Double.seconds: Duration get() = this.toLong().let { s -> s.seconds + ((this - s) * 1000000000).nanos }
val Double.minutes get() = (this * 60).seconds
val Double.hours get() = (this * 3600).seconds

val Int.days get() = Period.ofDays(this)!!
val Int.weeks get() = Period.ofWeeks(this)!!
val Int.months get() = Period.ofMonths(this)!!
val Int.years get() = Period.ofYears(this)!!
val Long.days get() = toInt().days
val Long.weeks get() = toInt().weeks
val Long.months get() = toInt().months
val Long.years get() = toInt().years

val Int.asYear get() = Year.of(this)!!
val Int.asMonth get() = Month.of(this)!!
val Int.asDayOfWeek get() = DayOfWeek.of(this)!!

// java.time.Duration already implements binary +, -, <, <=, ==, >=, >
operator fun Duration.unaryMinus() = this.negated()!!
operator fun Duration.times(multiplicand: Long) = this.multipliedBy(multiplicand)!!
operator fun Duration.div(divisor: Long) = this.dividedBy(divisor)!!
operator fun Duration.component1() = this.seconds
operator fun Duration.component2() = this.nano

operator fun Period.unaryMinus() = this.negated()!!
operator fun Period.times(multiplicand: Int) = this.multipliedBy(multiplicand)!!
operator fun Period.component1() = this.years
operator fun Period.component2() = this.months
operator fun Period.component3() = this.days

operator fun Year.plus(month: Month): YearMonth = atMonth(month)
operator fun Year.plus(monthDay: MonthDay): LocalDate = atMonthDay(monthDay)
operator fun Year.plus(years: Int): Year = plusYears(years.toLong())
operator fun Year.minus(years: Int): Year = minusYears(years.toLong())

operator fun Year.inc(): Year = plusYears(1L)
operator fun Year.dec(): Year = minusYears(1L)
operator fun Month.inc(): Month = plus(1L)
operator fun Month.dec(): Month = minus(1L)
operator fun DayOfWeek.inc(): DayOfWeek = plus(1L)
operator fun DayOfWeek.dec(): DayOfWeek = minus(1L)

val TemporalAmount.ago get() = (OffsetDateTime.now(ZoneOffset.UTC) - this)!!
val TemporalAmount.fromNow get() = (OffsetDateTime.now(ZoneOffset.UTC) + this)!!

// temporal queries

val TemporalAccessor.precision: TemporalUnit? get() = query(TemporalQueries.precision())
val TemporalAccessor.yr: Year? get() = query(Year::from)
val TemporalAccessor.yearMonth: YearMonth? get() = query(YearMonth::from)
val TemporalAccessor.month: Month? get() = query(Month::from)
val TemporalAccessor.monthDay: MonthDay? get() = query(MonthDay::from)
val TemporalAccessor.dayOfWeek: DayOfWeek? get() = query(DayOfWeek::from)
val TemporalAccessor.instant: Instant? get() = query(Instant::from)
val TemporalAccessor.localDate: LocalDate? get() = query(LocalDate::from)
val TemporalAccessor.localTime: LocalTime? get() = query(LocalTime::from)
val TemporalAccessor.localDateTime: LocalDateTime? get() = query(LocalDateTime::from)
val TemporalAccessor.zoneOffset: ZoneOffset? get() = query(ZoneOffset::from)
val TemporalAccessor.offsetTime: OffsetTime? get() = query(OffsetTime::from)
val TemporalAccessor.offsetDateTime: OffsetDateTime? get() = query(OffsetDateTime::from)
val TemporalAccessor.zone: ZoneId? get() = query(TemporalQueries.zone())
val TemporalAccessor.zoneId: ZoneId? get() = query(ZoneId::from)
val TemporalAccessor.zonedDateTime: ZonedDateTime? get() = query(ZonedDateTime::from)

// temporal adjusters

val <T: Temporal> T.atStartOfMonth get() = with(TemporalAdjusters.firstDayOfMonth())!!
val <T: Temporal> T.atEndOfMonth get() = with(TemporalAdjusters.lastDayOfMonth())!!
val <T: Temporal> T.atStartOfNextMonth get() = with(TemporalAdjusters.firstDayOfNextMonth())!!
val <T: Temporal> T.atStartOfYear get() = with(TemporalAdjusters.firstDayOfYear())!!
val <T: Temporal> T.atEndOfYear get() = with(TemporalAdjusters.lastDayOfYear())!!
val <T: Temporal> T.atStartOfNextYear get() = with(TemporalAdjusters.firstDayOfNextYear())!!

infix fun TemporalAccessor.supports(temporalField: TemporalField) = isSupported(temporalField)
infix fun Temporal.supports(temporalUnit: TemporalUnit) = isSupported(temporalUnit)
