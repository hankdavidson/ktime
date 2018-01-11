package org.hankster.time

import java.time.temporal.Temporal
import java.time.temporal.TemporalUnit

abstract class TemporalRange<T>(override val start: T, override val endInclusive: T, val step: Long = 1L) : ClosedRange<T>, Iterable<T>
    where T : Temporal, T : Comparable<T> {
  abstract infix fun unit(newUnit: TemporalUnit?): TemporalRange<T>
  abstract infix fun step(newStep: Long): TemporalRange<T>
}

class TemporalRangeInclusive<T>(start: T, endInclusive: T, unit: TemporalUnit? = null, step: Long = 1L) :
    TemporalRange<T>(start, endInclusive, step) where T : Temporal, T : Comparable<T> {

  val unit: TemporalUnit = unit ?: start.precision!!

  override fun iterator(): Iterator<T> = TemporalRangeIteratorInclusive()

  inner class TemporalRangeIteratorInclusive : Iterator<T> {
    private var current = start

    init {
      when {
        step == 0L -> throw IllegalArgumentException("Step is 0")
        step < 0L && start < endInclusive -> throw IllegalArgumentException("Step incompatible with ordering of start and end values")
        step > 0L && start > endInclusive -> throw IllegalArgumentException("Start and end values out of order")
      }

      if (!start.isSupported(unit)) {
        throw IllegalArgumentException("Value does not support unit $unit")
      }
    }

    override fun hasNext() = if (step < 0) current >= endInclusive else current <= endInclusive

    override fun next(): T {
      val next = current
      @Suppress("UNCHECKED_CAST")
      current = current.plus(step, unit) as T
      return next
    }
  }

  override infix fun unit(newUnit: TemporalUnit?) = TemporalRangeInclusive(start, endInclusive, newUnit, step)
  override infix fun step(newStep: Long) = TemporalRangeInclusive(start, endInclusive, unit, newStep)
}

class TemporalRangeExclusive<T>(start: T, endInclusive: T, unit: TemporalUnit? = null, step: Long = 1L) :
    TemporalRange<T>(start, endInclusive, step) where T : Temporal, T : Comparable<T> {

  val unit: TemporalUnit = unit ?: start.precision!!

  override fun iterator(): Iterator<T> = TemporalRangeIteratorExclusive()

  inner class TemporalRangeIteratorExclusive : Iterator<T> {
    private var current = start

    init {
      when {
        step == 0L -> throw IllegalArgumentException("Step is 0")
        step < 0L && start < endInclusive -> throw IllegalArgumentException("Step incompatible with ordering of start and end values")
        step > 0L && start > endInclusive -> throw IllegalArgumentException("Start and end values out of order")
      }

      if (!start.isSupported(unit)) {
        throw IllegalArgumentException("Value does not support unit $unit")
      }
    }

    override fun hasNext() = if (step < 0) current > endInclusive else current < endInclusive

    override fun next(): T {
      val next = current
      @Suppress("UNCHECKED_CAST")
      current = current.plus(step, unit) as T
      return next
    }
  }

  override infix fun unit(newUnit: TemporalUnit?) = TemporalRangeExclusive(start, endInclusive, newUnit, step)
  override infix fun step(newStep: Long) = TemporalRangeExclusive(start, endInclusive, unit, newStep)
}

operator fun <T> T.rangeTo(other: T): TemporalRange<T> where T : Temporal, T : Comparable<T> = TemporalRangeInclusive(this, other)
infix fun <T> T.until(other: T): TemporalRange<T> where T : Temporal, T : Comparable<T> = TemporalRangeExclusive(this, other)
