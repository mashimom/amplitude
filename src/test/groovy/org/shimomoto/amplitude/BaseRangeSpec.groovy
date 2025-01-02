package org.shimomoto.amplitude

import org.shimomoto.amplitude.api.ContinuousRange
import spock.lang.Specification

class BaseRangeSpec extends Specification {

	def "split_At_withContainedLimit_works"() {
		given:
			ContinuousRange<Integer> base = Ranges.of(1, 3)

		expect:
			base.splitAt(2) == [Ranges.of(1, 2), Ranges.of(2, 3)]
	}

	def "split_At_withoutContainedLimit_works"() {
		given:
			ContinuousRange<Integer> base = Ranges.of(1, 3)

		expect:
			base.splitAt(0) == [Ranges.of(1, 3)]
			base.splitAt(3) == [Ranges.of(1, 3)]
	}

	def "union_equal_works"() {
		given:
			ContinuousRange<Integer> base = Ranges.of(1, 3)

		expect:
			base.union(base) == [base]
	}

	def "union_touching_works"() {
		given:
			ContinuousRange<Integer> base = Ranges.of(1, 3)

		expect:
			base.union(Ranges.of(3, 4)) == [Ranges.of(1, 4)]
			base.union(Ranges.of(-10, 1)) == [Ranges.of(-10, 3)]
			base.union(Ranges.of(2, 3)) == [Ranges.of(1, 3)]
			base.union(Ranges.of(1, 2)) == [Ranges.of(1, 3)]
	}

	def "union_overlapping_works"() {
		given:
			ContinuousRange<Integer> base = Ranges.of(1, 3)

		expect:
			base.union(Ranges.of(2, 10)) == [Ranges.of(1, 10)]
			base.union(Ranges.of(2, 2)) == [Ranges.of(1, 3)]
			base.union(Ranges.of(0, 4)) == [Ranges.of(0, 4)]
			base.union(Ranges.of(-10, 2)) == [Ranges.of(-10, 3)]
	}

	def "union_disjoint_works"() {
		given:
			ContinuousRange<Integer> base = Ranges.of(1, 3)

		expect:
			base.union(Ranges.of(4, 5)) == [Ranges.of(1, 3), Ranges.of(4, 5)]
			base.union(Ranges.of(-10, 0)) == [Ranges.of(1, 3), Ranges.of(-10, 0)]
	}

	def "intersection_disjoint_works"() {
		given:
			ContinuousRange<Integer> base = Ranges.of(1, 3)

		expect:
			base.intersection(Ranges.of(3, 4)).isEmpty()
			base.intersection(Ranges.of(0, 1)).isEmpty()
	}

	def "intersection_overlapping_works"() {
		given:
			ContinuousRange<Integer> base = Ranges.of(1, 4)

		expect:
			base.intersection(Ranges.of(2, 5)) == Optional.of(Ranges.of(2, 4))
			base.intersection(Ranges.of(0, 2)) == Optional.of(Ranges.of(1, 2))
			base.intersection(Ranges.of(2, 3)) == Optional.of(Ranges.of(2, 3))
			base.intersection(Ranges.of(2, 2)) == Optional.of(Ranges.of(2, 2))
			base.intersection(Ranges.of(1, 4)) == Optional.of(Ranges.of(1, 4))
			base.intersection(Ranges.of(0, 5)) == Optional.of(Ranges.of(1, 4))
	}

	def "difference_disjoint_works"() {
		given:
			ContinuousRange<Integer> base = Ranges.of(1, 3)

		expect:
			base.difference(Ranges.of(3, 4)) == [base]
			base.difference(Ranges.of(-1, 1)) == [base]
	}

	def "difference_subsetOrEqual_works"() {
		given:
			ContinuousRange<Integer> base = Ranges.of(1, 3)

		expect:
			base.difference(Ranges.of(0, 4)).isEmpty()
			base.difference(Ranges.of(0, 3)).isEmpty()
			base.difference(Ranges.of(1, 4)).isEmpty()
			base.difference(base).isEmpty()
	}

	def "difference_superset_works"() {
		given:
			ContinuousRange<Integer> base = Ranges.of(1, 3)
			ContinuousRange<Integer> largerRange = Ranges.of(-10, 10)

		expect:
			base.difference(Ranges.of(2, 2)) == [Ranges.of(1, 2), Ranges.of(2, 3)]
			largerRange.difference(Ranges.of(-5, 5)) == [Ranges.of(-10, -5), Ranges.of(5, 10)]
			largerRange.difference(Ranges.of(0, 9)) == [Ranges.of(-10, 0), Ranges.of(9, 10)]
			largerRange.difference(Ranges.of(-9, 0)) == [Ranges.of(-10, -9), Ranges.of(0, 10)]
	}

	def "difference_leftOverlap_works"() {
		given:
			def base = Ranges.<Integer> of(1, 3)

		expect:
			base.difference(Ranges.of(0, 2)) == [Ranges.of(2, 3)]
	}

	def "difference_rightOverlap_works"() {
		given:
			ContinuousRange<Integer> base = Ranges.of(1, 3)

		expect:
			base.difference(Ranges.of(2, 4)) == [Ranges.of(1, 2)]
	}
}