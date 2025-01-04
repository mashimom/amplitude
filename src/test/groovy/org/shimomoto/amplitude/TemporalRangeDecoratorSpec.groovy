package org.shimomoto.amplitude

import org.shimomoto.amplitude.api.TemporalRange
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.UnsupportedTemporalTypeException

class TemporalRangeDecoratorSpec extends Specification {

	def "splitting on exact fitting interval works"() {
		given:
			def start = LocalDateTime.of(2025, 1, 3, 0, 0, 0)
			def end = LocalDateTime.of(2025, 1, 3, 12, 0, 0)
			@Subject
			TemporalRange<LocalDateTime> range = Ranges.temporalRange(start, end, ChronoUnit.HOURS, 4L)
			var splitRanges = (0..12).step(4).collate(2, 1, false)
					.collect { l, r ->
						Ranges.temporalRange(
								start.plus(l as long, ChronoUnit.HOURS),
								start.plus(r as long, ChronoUnit.HOURS),
								ChronoUnit.HOURS,
								4L)
					}
		expect:
			range.split().toList() == splitRanges
	}

	def "splitting on larger than fitting interval has a smaller range at the end"() {
		given:
			def start = LocalDateTime.of(2025, 1, 3, 0, 0, 0)
			def end = LocalDateTime.of(2025, 1, 3, 14, 0, 0)
			@Subject
			TemporalRange<LocalDateTime> range = Ranges.temporalRange(start, end, ChronoUnit.HOURS, 4L)
			var splitRanges = (0..12).step(4).collate(2, 1, false)
					.collect { l, r ->
						Ranges.temporalRange(
								start.plus(l as long, ChronoUnit.HOURS),
								start.plus(r as long, ChronoUnit.HOURS),
								ChronoUnit.HOURS,
								4L)
					}
			def extraSplit = Ranges.temporalRange(
					start.plus(12L, ChronoUnit.HOURS),
					start.plus(14L, ChronoUnit.HOURS),
					ChronoUnit.HOURS,
					4L)
			splitRanges += extraSplit
		expect:
			range.split().toList() == splitRanges
	}

	def "splitting on fails if the unit would change the Temporal type"() {
		when:
			//noinspection GroovyUnusedAssignment
			@Subject
			TemporalRange<LocalDate> invalidRangeUnit = Ranges.temporalRange(
					LocalDate.of(2025, 1, 1),
					LocalDate.of(2025, 1, 10),
					ChronoUnit.HOURS, // Unit smaller than a day
					1L
			)

		then:
			def e = thrown(UnsupportedTemporalTypeException)
			e.message == "Unsupported unit: Hours"
	}
}
