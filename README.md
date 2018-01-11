# ktime

Kotlin extensions to the java8 time library

# Examples
## Conversion from numbers to `Duration`
`Int`, `Long` and `Double` values can be suffixed with a time unit to create a `Duration`

    val delay = 30.seconds              // equal to Duration.ofSeconds(30)
    val longDelay = 2.5.minutes         // equal to Duration.ofSeconds(150)
    
Available `Duration` suffixes: `nanos`, `micros`, `millis`, `seconds`, `minutes`, `hours`

## Conversion from numbers to `Period`

`Int` and `Long` values can be suffixed with a date unit to create a `Period`

    val interval = 30L.days
    
Available `Period` suffixes: `days`, `weeks`, `months`, `years`

## Mathematical operations on `Duration` and `Period`
### addition, subtraction, negation

    val duration = 2.hours + 30.minutes
    val period = 2.months + 14.days            
    val duration = 1.hours - 1.minutes
    val negativeDuration = -30.seconds

### Scaling

    val fullDuration = 2.5.hours
    val doubledDuration = 2.5.hours * 2
    val halfDuration = 2.5.hours / 2
    
### Comparisons on `Duration`
Note that `Period` is not `Comparable`

    1.hours > 1.minutes
    1000.nanos == 1.micros    

## Destructuring

    val (sec, nano) = 2.5.seconds
    val (y, m, d) = 1.years + 2.months + 3.days

## Offsets from `now`
These produce an `OffsetDateTime` with a UTC offset:

    val lastHour = 1.hours.ago
    val thisTimeTomorrow = 1.days.fromNow
    
## Operations on times and dates
### Add or subtract `Duration` or `Period` from date/time types
    val startTime = 30.seconds.fromNow
    val stopTime = startTime + 1.minutes                                 // add Durations & Periods to dates & times
    
### TemporalQueries
Most TemporalQueries involve extracting a data type that contains a subset of the data for the current data type.  For example, you can go 
from OffsetDateTime to LocalDateTime, but not vice-versa because LocalDateTime doesn't have zoneOffset.
    
    val thisYear: Year = 0.seconds.ago.year
    val thisMonth: Month = 0.seconds.ago.month
    val dayOfWeek: DayOfWeek = 0.seconds.ago.dayOfWeek
    val currentTime: localTime = 0.seconds.ago.localTime
    
### TemporalAdjusters

    val startOfThisMonth = 0.seconds.ago.atStartOfThisMonth
    val startOfNextMonth = 0.seconds.ago.atStartOfNextMonth
    val endOfThisMonth = 0.seconds.ago.atEndOfThisMonth
    val startOfThisYear = 0.seconds.ago.atStartOfThisYear
    val startOfNextYear = 0.seconds.ago.atStartOfNextYear
    val endOfThisYear = 0.seconds.ago.atEndOfThisYear
    
## Temporal Ranges
Although java 8's time library doesn't support the idea of a Range, this library does. In addition to offering a `step` modifier, these 
ranges also offer a `unit` modifier. The unit modifier is used to specify the unit (`MINUTES`, `DAYS`, etc.) to use if you iterate over
the range. If no unit is specified, the native precision is used (e.g. the native precision for a date is `DAYS`, the native precision for 
DateTimes is `NANOS`)

Inclusive ranges
    
     (2010.asYear..2020.asYear).count() // should be equal to 11
    
     val inOfferPeriod = 0.seconds.ago in offer.start..offer.end
     
     for (d in date1..date2) {...}
     for (d in time1..time2 step 10 unit MINUTES) {...}
     for (d in dateTime1..dateTime2 unit DAYS) {...}

Exclusive ranges
     
     (2010.asYear until 2020.asYear).count() // should be equal to 10
     for (d in date1 until date2) {...}
     for (d in time1 until time2 step 10 unit MINUTES) {...}
     for (d in dateTime1 until dateTime2 unit DAYS) {...}
