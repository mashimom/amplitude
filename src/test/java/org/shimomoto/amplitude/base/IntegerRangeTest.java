package org.shimomoto.amplitude.base;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class IntegerRangeTest {



    //---------- Predicates ----------
    @Test
    void isEmpty_withEmptyRangeValues_accepts() {
        assertTrue(new IntegerRange(1, 1).isEmpty());
        assertTrue(new IntegerRange(-1, -1).isEmpty());
        assertTrue(new IntegerRange(0, 0).isEmpty());
        assertTrue(new IntegerRange(Integer.MIN_VALUE, Integer.MIN_VALUE).isEmpty());
        assertTrue(new IntegerRange(Integer.MAX_VALUE, Integer.MAX_VALUE).isEmpty());
    }

    @Test
    void isEmpty_withoutEmptyRangeValues_refuses() {
        assertFalse(new IntegerRange(1, 2).isEmpty());
        assertFalse(new IntegerRange(-1, 3).isEmpty());
        assertFalse(new IntegerRange(Integer.MIN_VALUE, Integer.MAX_VALUE).isEmpty());
        assertFalse(new IntegerRange(0, Integer.MAX_VALUE).isEmpty());
    }

//    @Test
//    void isSingletonRange_withSingleValue_accepts() {
//        assertTrue(new IntegerRange(1, 2).isSingletonRange());
//        assertEquals(1, IntStream.range(1, 2).count());
//        assertTrue(new IntegerRange(-1, 0).isSingletonRange());
//        assertEquals(1, IntStream.range(-1, 0).count());
//        assertTrue(new IntegerRange(Integer.MIN_VALUE, Integer.MIN_VALUE+1).isSingletonRange());
//        assertEquals(1, IntStream.range(Integer.MIN_VALUE, Integer.MIN_VALUE+1).count());
//        assertTrue(new IntegerRange(Integer.MAX_VALUE-1, Integer.MAX_VALUE).isSingletonRange());
//        assertEquals(1, IntStream.range(Integer.MAX_VALUE-1, Integer.MAX_VALUE).count());
//    }
//
//    @Test
//    void isSingletonRange_withoutSingleValue_refuses() {
//        assertFalse(new IntegerRange(1, 3).isSingletonRange());
//        assertFalse(new IntegerRange(-1, 1).isSingletonRange());
//        assertFalse(new IntegerRange(Integer.MIN_VALUE, Integer.MIN_VALUE+2).isSingletonRange());
//        assertFalse(new IntegerRange(Integer.MAX_VALUE-2, Integer.MAX_VALUE).isSingletonRange());
//    }

    @Test
    void containsValue_withContainedValue_accepts() {
        assertTrue(new IntegerRange(1, 3).containsValue(2));
        assertTrue(new IntegerRange(-1, 1).containsValue(0));
    }

    @Test
    void containsValue_withoutContainedValue_refuses() {
        assertFalse(new IntegerRange(1, 3).containsValue(0));
        assertFalse(new IntegerRange(-1, 1).containsValue(1));
    }

    @Test
    void contains_withContainedRange_accepts() {
        assertTrue(new IntegerRange(1, 3).contains(new IntegerRange(1, 3)));
        assertTrue(new IntegerRange(1, 3).contains(new IntegerRange(2, 3)));
        assertTrue(new IntegerRange(1, 3).contains(new IntegerRange(1, 2)));
        assertTrue(new IntegerRange(1, 3).contains(new IntegerRange(1,1)));
        assertTrue(new IntegerRange(-1, 1).contains(new IntegerRange(-1, 1)));
        assertTrue(new IntegerRange(-1, 1).contains(new IntegerRange(-1, 0)));
        assertTrue(new IntegerRange(-1, 1).contains(new IntegerRange(0, 1)));
        assertTrue(new IntegerRange(-1, 1).contains(new IntegerRange(1, 1)));


    }

    @Test
    void contains_withoutContainedRange_refuses() {
        assertFalse(new IntegerRange(1, 3).contains(new IntegerRange(-1, 3)));
        assertFalse(new IntegerRange(1, 3).contains(new IntegerRange(3, 4)));
        assertFalse(new IntegerRange(1, 3).contains(new IntegerRange(-1, 1)));
        assertFalse(new IntegerRange(1, 3).contains(new IntegerRange(3, 4)));
        assertFalse(new IntegerRange(-1, 1).contains(new IntegerRange(-1, 2)));
        assertFalse(new IntegerRange(-1, 1).contains(new IntegerRange(1, 2)));
    }

    @Test
    void intersects_withIntersectingValues_accepts() {
        IntegerRange base = new IntegerRange(1, 3);
        assertTrue(base.intersects(new IntegerRange(0, 4)));
        assertTrue(base.intersects(new IntegerRange(1, 3)));
        assertTrue(base.intersects(new IntegerRange(2, 2)));
        assertTrue(base.intersects(new IntegerRange(2, 3)));
        assertTrue(base.intersects(new IntegerRange(0, 2)));
        assertTrue(base.intersects(new IntegerRange(1, 2)));
        assertTrue(base.intersects(new IntegerRange(2, 3)));
        assertTrue(base.intersects(new IntegerRange(2, 4)));
    }

    @Test
    void intersects_withoutIntersectingValues_rejects() {
        IntegerRange base = new IntegerRange(1, 3);
        assertFalse(base.intersects(new IntegerRange(3, 3)));
        assertFalse(base.intersects(new IntegerRange(4, 10)));
        assertFalse(base.intersects(new IntegerRange(0, 1)));
        assertFalse(base.intersects(new IntegerRange(0, 0)));
    }

//    @Test
//    void stream_withValues_works() {
//        IntegerRange base = new IntegerRange(1, 3);
//        assertEquals(base.stream().toList(), IntStream.range(1, 3).boxed().toList());
//        IntegerRange aroundZero = new IntegerRange(-1, 1);
//        assertEquals(aroundZero.stream().toList(), IntStream.range(-1, 1).boxed().toList());
//    }

    //---------- Operations ----------
    @Test
    void split_At_withContainedLimit_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(new IntegerRange(1,2),new IntegerRange(2,3)), base.splitAt(2));
    }

    @Test
    void split_At_withoutContainedLimit_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(new IntegerRange(1,3)), base.splitAt(0));
        assertEquals(List.of(new IntegerRange(1,3)), base.splitAt(3));
    }

    @Test
    void difference_withoutIntersection_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(base), base.difference(new IntegerRange(3, 4)));
        assertEquals(List.of(base), base.difference(new IntegerRange(-1, 1)));
    }

    @Test
    void difference_withIntersection_works() {
        IntegerRange base = new IntegerRange(1, 4);
        assertEquals(List.of(new IntegerRange(1,2)), base.difference(new IntegerRange(2, 5)));
        assertEquals(List.of(new IntegerRange(2,4)), base.difference(new IntegerRange(0, 2)));
        assertEquals(List.of(new IntegerRange(1,2), new IntegerRange(2,4)), base.difference(new IntegerRange(2, 2)));
        assertEquals(List.of(new IntegerRange(1,2), new IntegerRange(3,4)), base.difference(new IntegerRange(2, 3)));
        assertTrue(base.difference(new IntegerRange(1, 4)).isEmpty());
        assertEquals(List.of(new IntegerRange(2,4)), base.difference(new IntegerRange(0, 2)));
        assertEquals(List.of(new IntegerRange(1,2)), base.difference(new IntegerRange(2, 5)));
        assertTrue(base.difference(new IntegerRange(0, 5)).isEmpty());
    }

    @Test
    void intersection_withoutIntersection_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertTrue(base.intersection(new IntegerRange(3, 4)).isEmpty());
        assertTrue(base.intersection(new IntegerRange(0, 1)).isEmpty());
    }

    @Test
    void intersection_withIntersection_works() {
        IntegerRange base = new IntegerRange(1, 4);
        assertEquals(Optional.of(new IntegerRange(2, 4)), base.intersection(new IntegerRange(2, 5)));
        assertEquals(Optional.of(new IntegerRange(1, 2)), base.intersection(new IntegerRange(0, 2)));
        assertEquals(Optional.of(new IntegerRange(2, 3)), base.intersection(new IntegerRange(2, 3)));
        assertEquals(Optional.of(new IntegerRange(2, 2)), base.intersection(new IntegerRange(2, 2)));
        assertEquals(Optional.of(new IntegerRange(1, 4)), base.intersection(new IntegerRange(1, 4)));
        assertEquals(Optional.of(new IntegerRange(1, 4)), base.intersection(new IntegerRange(0, 5)));
    }

    @Test
    void union_withIntersection_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(base), base.union(new IntegerRange(1, 3)));
        assertEquals(List.of(new IntegerRange(1, 4)), base.union(new IntegerRange(2, 4)));
        assertEquals(List.of(new IntegerRange(0, 3)), base.union(new IntegerRange(0, 1)));
        assertEquals(List.of(new IntegerRange(1, 3)), base.union(new IntegerRange(2, 2)));
        assertEquals(List.of(new IntegerRange(0, 4)), base.union(new IntegerRange(0, 4)));
        assertEquals(List.of(new IntegerRange(1, 3)), base.union(new IntegerRange(1, 2)));
        assertEquals(List.of(new IntegerRange(1, 3)), base.union(new IntegerRange(2, 3)));
    }

    @Test
    void union_withoutIntersection_works() {
        IntegerRange base = new IntegerRange(1, 3);
        assertEquals(List.of(new IntegerRange(1, 3), new IntegerRange(3, 4)), base.union(new IntegerRange(3, 4)));
        assertEquals(List.of(new IntegerRange(1, 3), new IntegerRange(-10, 0)), base.union(new IntegerRange(-10, 0)));
        assertEquals(List.of(new IntegerRange(-1, 3)), base.union(new IntegerRange(-1, 1)));
    }
}