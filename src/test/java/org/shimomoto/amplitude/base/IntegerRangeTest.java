package org.shimomoto.amplitude.base;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegerRangeTest {

    //---------- Operations ----------
    @Test
    void split_At_withContainedLimit_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(new IntegerRange(1, 2), new IntegerRange(2, 3)), base.splitAt(2));
    }

    @Test
    void split_At_withoutContainedLimit_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(new IntegerRange(1, 3)), base.splitAt(0));
        assertEquals(List.of(new IntegerRange(1, 3)), base.splitAt(3));
    }

    @Test
    void union_equal_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(base), base.union(base));
    }

    @Test
    void union_touching_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(new IntegerRange(1, 4)), base.union(new IntegerRange(3, 4)));
        assertEquals(List.of(new IntegerRange(-10, 3)), base.union(new IntegerRange(-10, 1)));
        assertEquals(List.of(new IntegerRange(1, 3)), base.union(new IntegerRange(2, 3)));
        assertEquals(List.of(new IntegerRange(1, 3)), base.union(new IntegerRange(1, 2)));
    }

    @Test
    void union_overlapping_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(new IntegerRange(1, 10)), base.union(new IntegerRange(2, 10)));
        assertEquals(List.of(new IntegerRange(1, 3)), base.union(new IntegerRange(2, 2)));
        assertEquals(List.of(new IntegerRange(0, 4)), base.union(new IntegerRange(0, 4)));
        assertEquals(List.of(new IntegerRange(-10, 10)), base.union(new IntegerRange(-10, 10)));
    }

    @Test
    void union_disjoint_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(new IntegerRange(1, 3), new IntegerRange(4, 5)), base.union(new IntegerRange(4, 5)));
        assertEquals(List.of(new IntegerRange(1, 3), new IntegerRange(-10, 0)), base.union(new IntegerRange(-10, 0)));
    }

    @Test
    void intersection_disjoint_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertTrue(base.intersection(new IntegerRange(3, 4)).isEmpty());
        assertTrue(base.intersection(new IntegerRange(0, 1)).isEmpty());
    }

    @Test
    void intersection_overlapping_works() {
        IntegerRange base = new IntegerRange(1, 4);
        assertEquals(Optional.of(new IntegerRange(2, 4)), base.intersection(new IntegerRange(2, 5)));
        assertEquals(Optional.of(new IntegerRange(1, 2)), base.intersection(new IntegerRange(0, 2)));
        assertEquals(Optional.of(new IntegerRange(2, 3)), base.intersection(new IntegerRange(2, 3)));
        assertEquals(Optional.of(new IntegerRange(2, 2)), base.intersection(new IntegerRange(2, 2)));
        assertEquals(Optional.of(new IntegerRange(1, 4)), base.intersection(new IntegerRange(1, 4)));
        assertEquals(Optional.of(new IntegerRange(1, 4)), base.intersection(new IntegerRange(0, 5)));
    }


    @Test
    void difference_disjoint_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(base), base.difference(new IntegerRange(3, 4)));
        assertEquals(List.of(base), base.difference(new IntegerRange(-1, 1)));
    }

    @Test
    void difference_subsetOrEqual_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertTrue(base.difference(new IntegerRange(0, 4)).isEmpty());
        assertTrue(base.difference(new IntegerRange(0, 3)).isEmpty());
        assertTrue(base.difference(new IntegerRange(1, 4)).isEmpty());
        assertTrue(base.difference(base).isEmpty());
    }

    @Test
    void difference_superset_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(new IntegerRange(1, 2), new IntegerRange(2, 3)), base.difference(new IntegerRange(2, 2)));
        IntegerRange largerRange = new IntegerRange(-10, 10);
        assertEquals(List.of(new IntegerRange(-10, -5), new IntegerRange(5, 10)), largerRange.difference(new IntegerRange(-5, 5)));
        assertEquals(List.of(new IntegerRange(-10, 0), new IntegerRange(9, 10)), largerRange.difference(new IntegerRange(0, 9)));
        assertEquals(List.of(new IntegerRange(-10, -9), new IntegerRange(0, 10)), largerRange.difference(new IntegerRange(-9, 0)));
    }

    @Test
    void difference_leftOverlap_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(new IntegerRange(2, 3)), base.difference(new IntegerRange(0, 2)));
    }

    @Test
    void difference_rightOverlap_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(new IntegerRange(1, 2)), base.difference(new IntegerRange(2, 4)));
    }
}