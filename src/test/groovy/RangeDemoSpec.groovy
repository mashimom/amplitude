import org.shimomoto.amplitude.Ranges
import org.shimomoto.amplitude.api.Range
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalTime

@Narrative("""
This demonstrates the versatility of the library using different types that implement Comparable.
It covers all methods and explains the intention behind the results.
""")
class RangeDemoSpec extends Specification {

	def "A basic usage with Integer"() {
		given: "A Range [1,10) with Integer values"
			@Subject
			Range<Integer> range = Ranges.of(1, 10)

		when: "Checking the min and max values"
			def min = range.min()
			def max = range.max()

		then: "The min and max values are correct"
			min == 1
			max == 10
		and: "The range is closed-open"
			range.containsValue(1)
			!range.containsValue(10)
	}

	def "A basic usage with Characters"() {
		given: "A Range with [a,z) Characters values"
			@Subject
			Range<Character> range = Ranges.of('a' as Character, 'z' as Character)

		when: "split at the character 'e'"
			def result = range.splitAt('e' as Character)

		then: "Checking the min and max values"
			range.min() == 'a' as Character
			range.max() == 'z' as Character

		and: "The resulting split"
			result.size() == 2
			result[0].min() == 'a' as Character
			result[0].max() == 'e' as Character
			result[1].min() == 'e' as Character
			result[1].max() == 'z' as Character
	}

	def "Checking if two Ranges overlap each other"() {
		given: "A Range [1.5,9.5) "
			@Subject
			Range<Double> range = Ranges.of(1.5D, 9.5D)

		expect: "Overlaps a Range [5.0,10.0)"
			range.isOverlapping(Ranges.of(5.0D, 10.0D))
	}

	def "A basic usage with LocalTime"() {
		given: "A Range with LocalTime values"
			@Subject
			Range<LocalTime> range = Ranges.of(LocalTime.of(23, 37), LocalTime.of(23, 59))

		expect: "Checking the min and max values"
			range.min() == LocalTime.of(23, 37)
			range.max() == LocalTime.of(23, 59)
	}

	def "basic usage with Custom Comparable type"() {
		given: "A Range with a custom Comparable type"
			@Subject
			Range<CustomComparable> range = Ranges.of(new CustomComparable(1), new CustomComparable(10))

		when: "Checking the min and max values"
			def min = range.min()
			def max = range.max()

		then: "The min and max values are correct"
			min == new CustomComparable(1)
			max == new CustomComparable(10)
	}

	static class CustomComparable implements Comparable<CustomComparable> {
		int value

		CustomComparable(int value) {
			this.value = value
		}

		@Override
		int compareTo(CustomComparable other) {
			return Integer.compare(this.value, other.value)
		}

		@Override
		boolean equals(Object o) {
			if (this == o) return true
			if (o == null || getClass() != o.getClass()) return false
			CustomComparable that = (CustomComparable) o
			return value == that.value
		}

		@Override
		int hashCode() {
			return Objects.hash(value)
		}
	}
}