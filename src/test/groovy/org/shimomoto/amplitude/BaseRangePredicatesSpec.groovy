package org.shimomoto.amplitude

import org.shimomoto.amplitude.api.ContinuousRange
import spock.lang.Specification

class BaseRangePredicatesSpec extends Specification {

	def "constructor works"() {
		given:
			ContinuousRange<Integer> range = Ranges.of(1, 2)

		expect:
			range.min == 1
			range.max == 2

		when:
			Ranges.of(2, 1)

		then:
			def e = thrown(IllegalArgumentException)
			e.message == "Inverted range is not accepted: min: 2, max: 1"
	}

	def "test isEmpty"() {
		expect:
			Ranges.of(5, 5).isEmpty()
			!Ranges.of(5, 6).isEmpty()
	}

	def "test containsValue"() {
		given:
			ContinuousRange<Integer> range = Ranges.of(1, 10)

		expect:
			range.containsValue(1)
			range.containsValue(5)
			range.containsValue(9)
			!range.containsValue(10)
			!range.containsValue(0)
	}

	def "test isDisjoint"() {
		given:
			ContinuousRange<Integer> range1 = Ranges.of(1, 5)
			ContinuousRange<Integer> range2 = Ranges.of(5, 10)
			ContinuousRange<Integer> range3 = Ranges.of(0, 1)
			ContinuousRange<Integer> overlappingRange = Ranges.of(4, 6)

		expect:
			range1.isDisjoint(range2)
			range1.isDisjoint(range3)
			!range1.isDisjoint(overlappingRange)
	}

	def "test isTouching"() {
		given:
			ContinuousRange<Integer> range1 = Ranges.of(1, 5)
			ContinuousRange<Integer> range2 = Ranges.of(5, 10)
			ContinuousRange<Integer> range3 = Ranges.of(0, 1)
			ContinuousRange<Integer> overlapping = Ranges.of(3, 10)
			ContinuousRange<Integer> overlapping2 = Ranges.of(0, 3)

		expect:
			range1.isTouching(range2)
			range1.isTouching(range3)
			!range1.isTouching(overlapping)
			!range1.isTouching(overlapping2)
	}

	def "test isOverlapping"() {
		given:
			ContinuousRange<Integer> range1 = Ranges.of(1, 5)
			ContinuousRange<Integer> range2 = Ranges.of(5, 10)
			ContinuousRange<Integer> range3 = Ranges.of(0, 1)
			ContinuousRange<Integer> overlapping = Ranges.of(3, 10)
			ContinuousRange<Integer> overlapping2 = Ranges.of(0, 3)

		expect:
			!range1.isOverlapping(range2)
			!range1.isOverlapping(range3)
			range1.isOverlapping(overlapping)
			range1.isOverlapping(overlapping2)
	}

	def "test isSubsetOrEqualTo"() {
		given:
			ContinuousRange<Integer> range1 = Ranges.of(1, 5)
			ContinuousRange<Integer> range2 = Ranges.of(5, 10)
			ContinuousRange<Integer> subset1a = Ranges.of(2, 4)
			ContinuousRange<Integer> subset1b = Ranges.of(1, 4)
			ContinuousRange<Integer> subset2 = Ranges.of(6, 9)

		expect:
			!range1.isSubsetOrEqualTo(range2)
			!range2.isSubsetOrEqualTo(range1)
			range1.isSubsetOrEqualTo(range1)
			range2.isSubsetOrEqualTo(range2)
			subset1a.isSubsetOrEqualTo(range1)
			subset1b.isSubsetOrEqualTo(range1)
			subset2.isSubsetOrEqualTo(range2)
	}

	def "test isProperSubsetOf"() {
		given:
			ContinuousRange<Integer> range1 = Ranges.of(1, 5)
			ContinuousRange<Integer> range2 = Ranges.of(5, 10)
			ContinuousRange<Integer> subset1a = Ranges.of(2, 4)
			ContinuousRange<Integer> subset1b = Ranges.of(1, 4)
			ContinuousRange<Integer> subset2a = Ranges.of(6, 9)
			ContinuousRange<Integer> subset2b = Ranges.of(6, 10)

		expect:
			!range1.isProperSubsetOf(range2)
			!range2.isProperSubsetOf(range1)
			!range1.isProperSubsetOf(range1)
			!range2.isProperSubsetOf(range2)
			!subset1b.isProperSubsetOf(range1)
			!subset2b.isProperSubsetOf(range2)
			subset1a.isProperSubsetOf(range1)
			subset2a.isProperSubsetOf(range2)
	}

	def "test isSuperSetOf"() {
		given:
			ContinuousRange<Integer> range1 = Ranges.of(1, 5)
			ContinuousRange<Integer> range2 = Ranges.of(0, 10)
			ContinuousRange<Integer> same1 = Ranges.of(1, 5)
			ContinuousRange<Integer> notSuperSet1 = Ranges.of(2, 20)
			ContinuousRange<Integer> superset1 = Ranges.of(0, 6)
			ContinuousRange<Integer> superset2 = Ranges.of(-1, 11)

		expect:
			range2.isSuperSetOf(range1)
			same1.isSuperSetOf(range1)
			range2.isSuperSetOf(range2)
			superset1.isSuperSetOf(range1)
			superset2.isSuperSetOf(range2)
			superset2.isSuperSetOf(range1)
			!range1.isSuperSetOf(range2)
			!superset1.isSuperSetOf(range2)
			!range1.isSuperSetOf(range2)
			!notSuperSet1.isSuperSetOf(range1)
			!notSuperSet1.isSuperSetOf(range2)
	}
}