package org.shimomoto.amplitude;

import org.junit.jupiter.api.Test;
import org.shimomoto.amplitude.BaseRange;
import org.shimomoto.amplitude.api.ContinuousRange;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseRangeTest {

    //---------- Operations ----------
    @Test
    void split_At_withContainedLimit_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 3);
        assertEquals(List.of(Ranges.of(1, 2), Ranges.of(2, 3)), base.splitAt(2));
    }

    @Test
    void split_At_withoutContainedLimit_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 3);
        assertEquals(List.of(Ranges.of(1, 3)), base.splitAt(0));
        assertEquals(List.of(Ranges.of(1, 3)), base.splitAt(3));
    }

    @Test
    void union_equal_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 3);
        assertEquals(List.of(base), base.union(base));
    }

    @Test
    void union_touching_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 3);
        assertEquals(List.of(Ranges.of(1, 4)), base.union(Ranges.of(3, 4)));
        assertEquals(List.of(Ranges.of(-10, 3)), base.union(Ranges.of(-10, 1)));
        assertEquals(List.of(Ranges.of(1, 3)), base.union(Ranges.of(2, 3)));
        assertEquals(List.of(Ranges.of(1, 3)), base.union(Ranges.of(1, 2)));
    }

    @Test
    void union_overlapping_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 3);
        assertEquals(List.of(Ranges.of(1, 10)), base.union(Ranges.of(2, 10)));
        assertEquals(List.of(Ranges.of(1, 3)), base.union(Ranges.of(2, 2)));
        assertEquals(List.of(Ranges.of(0, 4)), base.union(Ranges.of(0, 4)));
        assertEquals(List.of(Ranges.of(-10, 3)), base.union(Ranges.of(-10, 2)));
    }

    @Test
    void union_disjoint_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 3);
        assertEquals(List.of(Ranges.of(1, 3), Ranges.of(4, 5)), base.union(Ranges.of(4, 5)));
        assertEquals(List.of(Ranges.of(1, 3), Ranges.of(-10, 0)), base.union(Ranges.of(-10, 0)));
    }

    @Test
    void intersection_disjoint_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 3);
        assertTrue(base.intersection(Ranges.of(3, 4)).isEmpty());
        assertTrue(base.intersection(Ranges.of(0, 1)).isEmpty());
    }

    @Test
    void intersection_overlapping_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 4);
        assertEquals(Optional.of(Ranges.of(2, 4)), base.intersection(Ranges.of(2, 5)));
        assertEquals(Optional.of(Ranges.of(1, 2)), base.intersection(Ranges.of(0, 2)));
        assertEquals(Optional.of(Ranges.of(2, 3)), base.intersection(Ranges.of(2, 3)));
        assertEquals(Optional.of(Ranges.of(2, 2)), base.intersection(Ranges.of(2, 2)));
        assertEquals(Optional.of(Ranges.of(1, 4)), base.intersection(Ranges.of(1, 4)));
        assertEquals(Optional.of(Ranges.of(1, 4)), base.intersection(Ranges.of(0, 5)));
    }

    @Test
    void difference_disjoint_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 3);
        assertEquals(List.of(base), base.difference(Ranges.of(3, 4)));
        assertEquals(List.of(base), base.difference(Ranges.of(-1, 1)));
    }

    @Test
    void difference_subsetOrEqual_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 3);
        assertTrue(base.difference(Ranges.of(0, 4)).isEmpty());
        assertTrue(base.difference(Ranges.of(0, 3)).isEmpty());
        assertTrue(base.difference(Ranges.of(1, 4)).isEmpty());
        assertTrue(base.difference(base).isEmpty());
    }

    @Test
    void difference_superset_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 3);
        assertEquals(List.of(Ranges.of(1, 2), Ranges.of(2, 3)), base.difference(Ranges.of(2, 2)));
        ContinuousRange<Integer> largerRange = Ranges.of(-10, 10);
        assertEquals(List.of(Ranges.of(-10, -5), Ranges.of(5, 10)), largerRange.difference(Ranges.of(-5, 5)));
        assertEquals(List.of(Ranges.of(-10, 0), Ranges.of(9, 10)), largerRange.difference(Ranges.of(0, 9)));
        assertEquals(List.of(Ranges.of(-10, -9), Ranges.of(0, 10)), largerRange.difference(Ranges.of(-9, 0)));
    }

    @Test
    void difference_leftOverlap_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 3);
        assertEquals(List.of(Ranges.of(2, 3)), base.difference(Ranges.of(0, 2)));
    }

    @Test
    void difference_rightOverlap_works() {
        ContinuousRange<Integer> base = Ranges.of(1, 3);
        assertEquals(List.of(Ranges.of(1, 2)), base.difference(Ranges.of(2, 4)));
    }
}