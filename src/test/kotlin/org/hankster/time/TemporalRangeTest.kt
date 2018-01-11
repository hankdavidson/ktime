package org.hankster.time

import org.junit.Test
import java.time.*
import java.time.DayOfWeek.*
import java.time.Month.*
import java.time.temporal.ChronoUnit.*
import java.time.temporal.Temporal
import kotlin.test.*

class TemporalRangeTest {

  @Test
  fun `range operations`() {
    assertTrue { WEDNESDAY in MONDAY..FRIDAY }
    assertTrue { JUNE in JUNE..JULY }
    assertTrue { JULY in JUNE..JULY }
    assertFalse { AUGUST in JUNE..JULY }
    assertTrue { 2010.asYear in 2000.asYear..2020.asYear }
    assertTrue { OffsetDateTime.now() in 1.hours.ago..1.hours.fromNow }
    (2010.asYear..2020.asYear).count() shouldEqual 11
    (2010.asYear until 2020.asYear).count() shouldEqual 10
    (LocalDate.of(2010, 1, 1)..LocalDate.of(2020, 12, 31) unit YEARS).count() shouldEqual 11
    (LocalDate.of(2010, 1, 1)..LocalDate.of(2020, 12, 31) unit MONTHS).count() shouldEqual 11 * 12
    (LocalDate.of(2010, 1, 1)..LocalDate.of(2020, 12, 31)).count() shouldEqual 11 * 365 + 3

    fun isWeekday(temporal: Temporal) = temporal.dayOfWeek!! in MONDAY..FRIDAY
    fun isHoliday(temporal: Temporal) = temporal.monthDay in listOf(
        MonthDay.of(1, 1), MonthDay.of(5, 31), MonthDay.of(12, 25))

    val workdays2017: Int = (LocalDate.of(2017, 1, 1)..LocalDate.of(2017, 12, 31))
        .filter(::isWeekday).filter { !isHoliday(it) }.count()
    workdays2017 shouldEqual 258
  }

  @Test
  fun `attempting to iterate invalid ranges should throw`() {
    expect("Step is 0") {
      assertFailsWith<IllegalArgumentException> { (2010.asYear..2020.asYear step 0).iterator() }.message
    }
    expect("Step incompatible with ordering of start and end values") {
      assertFailsWith<IllegalArgumentException> { (2010.asYear..2020.asYear step -1).iterator() }.message
    }
    expect("Start and end values out of order") {
      assertFailsWith<IllegalArgumentException> { (2010.asYear..2000.asYear step 1).iterator() }.message
    }
    expect("Value does not support unit Months") {
      assertFailsWith<IllegalArgumentException> { (2010.asYear..2020.asYear unit MONTHS).iterator() }.message
    }
  }

  @Test
  fun `For loop over a date range should allow a unit to be specified`() {
    var counter = 0
    val range = LocalDate.of(2017, 3, 1)!!..LocalDate.of(2017, 6, 20)!!
    for (d in range unit MONTHS) {
      counter++
    }
    counter shouldEqual 4
  }

  @Test
  fun `For loop over a date range with start == end should iterate once`() {

    var counter = 0
    val start = LocalDate.of(2017, 3, 1)!!
    val end = LocalDate.of(2017, 3, 1)!!

    for (d in start..end unit YEARS) {
      counter++
    }

    counter shouldEqual 1
  }

  @Test
  fun `For loop should allow step and unit to be specified`() {

    var counter = 0
    val start = LocalDate.of(2000, 3, 1)!!
    val end = LocalDate.of(2009, 3, 1)!!

    for (d in start..end unit YEARS step 2) {
      counter++
    }

    counter shouldEqual 5
  }

  @Test
  fun `For loop over a date range with start less than end and negative step should iterate backwards`() {
    var counter = 0
    val start = LocalDate.of(2017, 3, 3)!!
    val end = LocalDate.of(2017, 3, 1)!!

    for (d in start..end unit DAYS step -1) {
      counter++
    }

    counter shouldEqual 3
  }

  @Test
  fun `Range should use the Temporal's native precision as the default unit in a for loop`() {
    val start = LocalDate.of(2017, 3, 1)!!
    val end = LocalDate.of(2017, 3, 4)!!
    var counter = 0

    // default precision of LocalDate is DAYS
    for (d in start..end) {
      counter++
    }

    counter shouldEqual 4
  }
}
