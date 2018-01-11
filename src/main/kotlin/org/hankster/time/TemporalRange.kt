package org.hankster.time

import java.time.temporal.Temporal
import java.time.temporal.TemporalUnit

class TemporalRange<T>(override val start: T, override val endInclusive: T, unit: TemporalUnit? = null, val step: Long = 1L):
    ClosedRange<T>, Iterable<T> where T: Temporal, T: Comparable<T> {

  val unit: TemporalUnit = unit ?: start.precision!!

  override fun iterator(): Iterator<T> = TemporalRangeIterator()

  inner class TemporalRangeIterator : Iterator<T> {
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

    override fun next(): T  {
      val next = current
      current = current.plus(step, unit) as T
      return next
    }
  }

  infix fun unit(newUnit: TemporalUnit?) = TemporalRange(start, endInclusive, newUnit, step)
  infix fun step(newStep: Long) = TemporalRange(start, endInclusive, unit, newStep)
}

operator fun <T> T.rangeTo(other: T): TemporalRange<T> where T: Temporal, T : Comparable<T> = TemporalRange(this, other)

