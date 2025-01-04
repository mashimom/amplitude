package org.shimomoto.amplitude

import org.shimomoto.amplitude.api.Range
import spock.lang.Specification

class BaseRangePredicatesSpec extends Specification {

	def "constructor works"() {
		given:
			Range<Integer> range = Ranges.of(1, 2)

		expect:
			range.min() == 1
			range.max() == 2

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
			Range<Integer> range = Ranges.of(1, 10)

		expect:
			range.containsValue(1)
			range.containsValue(5)
			range.containsValue(9)
			!range.containsValue(10)
			!range.containsValue(0)
	}

	def "test isDisjoint"() {
		given:
			Range<Integer> range1 = Ranges.of(1, 5)
			Range<Integer> range2 = Ranges.of(5, 10)
			Range<Integer> range3 = Ranges.of(0, 1)
			Range<Integer> overlappingRange = Ranges.of(4, 6)

		expect:
			range1.isDisjoint(range2)
			range1.isDisjoint(range3)
			!range1.isDisjoint(overlappingRange)
	}

	def "test isTouching"() {
		given:
			Range<Integer> range1 = Ranges.of(1, 5)
			Range<Integer> range2 = Ranges.of(5, 10)
			Range<Integer> range3 = Ranges.of(0, 1)
			Range<Integer> overlapping = Ranges.of(3, 10)
			Range<Integer> overlapping2 = Ranges.of(0, 3)

		expect:
			range1.isTouching(range2)
			range1.isTouching(range3)
			!range1.isTouching(overlapping)
			!range1.isTouching(overlapping2)
	}

	def "test isOverlapping"() {
		given:
			Range<Integer> range1 = Ranges.of(1, 5)
			Range<Integer> range2 = Ranges.of(5, 10)
			Range<Integer> range3 = Ranges.of(0, 1)
			Range<Integer> overlapping = Ranges.of(3, 10)
			Range<Integer> overlapping2 = Ranges.of(0, 3)

		expect:
			!range1.isOverlapping(range2)
			!range1.isOverlapping(range3)
			range1.isOverlapping(overlapping)
			range1.isOverlapping(overlapping2)
	}

	def "test isSubsetOrEqualTo"() {
		given:
			Range<Integer> range1 = Ranges.of(1, 5)
			Range<Integer> range2 = Ranges.of(5, 10)
			Range<Integer> subset1a = Ranges.of(2, 4)
			Range<Integer> subset1b = Ranges.of(1, 4)
			Range<Integer> subset2 = Ranges.of(6, 9)

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
			Range<Integer> range_1_5 = Ranges.of(1, 5)
			Range<Integer> range_5_10 = Ranges.of(5, 10)
			Range<Integer> subset_2_4 = Ranges.of(2, 4)
			Range<Integer> subset_1_4 = Ranges.of(1, 4)
			Range<Integer> subset_6_9 = Ranges.of(6, 9)
			Range<Integer> subset_6_10 = Ranges.of(6, 10)

		expect:
			!range_1_5.isProperSubsetOf(range_5_10)
			!range_5_10.isProperSubsetOf(range_1_5)
			!range_1_5.isProperSubsetOf(range_1_5)
			!range_5_10.isProperSubsetOf(range_5_10)
		and:
			Ranges.of(1, 3).isProperSubsetOf(Ranges.of(0, 3))
			Ranges.of(1, 3).isProperSubsetOf(Ranges.of(1, 4))
			subset_1_4.isProperSubsetOf(range_1_5)
			subset_2_4.isProperSubsetOf(range_1_5)
			subset_6_9.isProperSubsetOf(range_5_10)
			subset_6_10.isProperSubsetOf(range_5_10)
	}

	def "test isSuperSetOf"() {
		given:
			Range<Integer> range1 = Ranges.of(1, 5)
			Range<Integer> range2 = Ranges.of(0, 10)
			Range<Integer> same1 = Ranges.of(1, 5)
			Range<Integer> notSuperSet1 = Ranges.of(2, 20)
			Range<Integer> superset1 = Ranges.of(0, 6)
			Range<Integer> superset2 = Ranges.of(-1, 11)

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