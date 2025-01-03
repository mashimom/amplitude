import org.shimomoto.amplitude.Ranges
import org.shimomoto.amplitude.api.TemporalRange
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.UnsupportedTemporalTypeException

@Narrative("""
This demonstrates the functionality of the library,
making clear usage examples.
""")
class TemporalDemoSpec extends Specification {

	def "A basic usage with LocalDate"() {
		given: "A TemporalRange with LocalDate and ChronoUnit.DAYS"
			@Subject
			TemporalRange<LocalDate> range = Ranges.chronoRange(
					LocalDate.of(2025, 1, 1),
					LocalDate.of(2025, 1, 4),
					ChronoUnit.DAYS,
					1L
			)

		when: "Splitting the range"
			def splitRanges = range.split().toList()

		then: "The range is split into daily intervals"
			splitRanges.size() == 3
			splitRanges.every { TemporalRange<LocalDate> r -> r.unit() == ChronoUnit.DAYS }
			splitRanges.every { TemporalRange<LocalDate> r -> r.step() == 1L }
		and: "These are the intervals"
			splitRanges[0].min() == LocalDate.of(2025, 1, 1)
			splitRanges[0].max() == LocalDate.of(2025, 1, 2)

			splitRanges[1].min() == LocalDate.of(2025, 1, 2)
			splitRanges[1].max() == LocalDate.of(2025, 1, 3)

			splitRanges[2].min() == LocalDate.of(2025, 1, 3)
			splitRanges[2].max() == LocalDate.of(2025, 1, 4)
	}

	def "An advanced usage with LocalDateTime"() {
		given: "A TemporalRange with LocalDateTime and ChronoUnit.HOURS"
			@Subject
			TemporalRange<LocalDateTime> range = Ranges.chronoRange(
					LocalDateTime.of(2025, 1, 1, 0, 0),
					LocalDateTime.of(2025, 1, 1, 14, 0),
					ChronoUnit.HOURS,
					3L
			)

		when: "Splitting the range"
			def splitRanges = range.split().toList()

		then: "The range is split into 3-hour intervals plus the remainder 2-hour interval"
			splitRanges.size() == 5
			splitRanges.every { it.unit == ChronoUnit.HOURS }
			splitRanges.every { it.step == 3L }
		and: "the last reduced range"
			splitRanges[-1].min() == LocalDateTime.of(2025, 1, 1, 12, 0)
			splitRanges[-1].max() == LocalDateTime.of(2025, 1, 1, 14, 0)
	}

	def "But if the TemporalUnit provided is a fraction of the Temporal"() {
		when: "Creating a TemporalRange with LocalDate and ChronoUnit.HOURS"
			@Subject
			TemporalRange<LocalDate> invalidRangeUnit = Ranges.chronoRange(
					LocalDate.of(2025, 1, 1),
					LocalDate.of(2025, 1, 10),
					ChronoUnit.HOURS, // Unit smaller than a day
					1L
			)

		then: "An UnsupportedTemporalTypeException is thrown"
			def e = thrown(UnsupportedTemporalTypeException)
			e.message == "Unsupported unit: Hours"
	}

	def "Union of 2 ranges on LocalDateTime"() {
		given: "Two overlapping ChronoRanges with LocalDateTime"
			@Subject
			TemporalRange<LocalDateTime> range1 = Ranges.chronoRange(
					LocalDateTime.of(2025, 1, 1, 0, 0),
					LocalDateTime.of(2025, 1, 1, 6, 0),
					ChronoUnit.HOURS,
					1L
			)
			TemporalRange<LocalDateTime> range2 = Ranges.chronoRange(
					LocalDateTime.of(2025, 1, 1, 3, 0),
					LocalDateTime.of(2025, 1, 1, 9, 0),
					ChronoUnit.HOURS,
					1L
			)

		when: "Combining the ranges"
			def combinedRange = range1.union(range2)

		then: "The combined range covers the entire period from the earliest start to the latest end"
			combinedRange.size() == 1
			combinedRange[0].min() == LocalDateTime.of(2025, 1, 1, 0, 0)
			combinedRange[0].max() == LocalDateTime.of(2025, 1, 1, 9, 0)
	}
}
