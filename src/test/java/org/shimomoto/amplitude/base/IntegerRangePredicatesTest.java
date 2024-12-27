package org.shimomoto.amplitude.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegerRangePredicatesTest {
    @Test
    void isEmpty_accepts() {
        assertTrue(new IntegerRange(1, 1).isEmpty());
        assertTrue(new IntegerRange(-1, -1).isEmpty());
    }

    @Test
    void isEmpty_rejects() {
        assertFalse(new IntegerRange(1, 2).isEmpty());
        assertFalse(new IntegerRange(-1, 0).isEmpty());
    }

    @Test
    void containsValue_accepts() {
        IntegerRange range = new IntegerRange(1, 3);
        assertTrue(range.containsValue(1));
        assertTrue(range.containsValue(2));
    }

    @Test
    void containsValue_rejects() {
        IntegerRange range = new IntegerRange(1, 3);
        assertFalse(range.containsValue(0));
        assertFalse(range.containsValue(3));
    }

    @Test
    void isDisjoint_accepts() {
        IntegerRange range = new IntegerRange(1, 3);
        assertTrue(range.isDisjoint(new IntegerRange(4, 5)));
        assertTrue(range.isDisjoint(new IntegerRange(-10, 0)));
        assertTrue(range.isDisjoint(new IntegerRange(3, 5)));
        assertTrue(range.isDisjoint(new IntegerRange(-10, 1)));
    }

    @Test
    void isDisjoint_rejects() {
        IntegerRange range = new IntegerRange(1, 3);
        assertFalse(range.isDisjoint(new IntegerRange(2, 5)));
        assertFalse(range.isDisjoint(new IntegerRange(-10, 2)));
        assertFalse(range.isDisjoint(new IntegerRange(1, 2)));
        assertFalse(range.isDisjoint(new IntegerRange(-10, 3)));
    }

    @Test
    void isTouching_accepts() {
        IntegerRange range = new IntegerRange(1, 3);
        assertTrue(range.isTouching(new IntegerRange(3, 5)));
        assertTrue(range.isTouching(new IntegerRange(-10, 1)));
    }

    @Test
    void isTouching_rejects() {
        IntegerRange range = new IntegerRange(1, 3);
        assertFalse(range.isTouching(new IntegerRange(4, 5)));
        assertFalse(range.isTouching(new IntegerRange(-10, 0)));
    }

    @Test
    void isOverlapping_accepts() {
        IntegerRange range = new IntegerRange(1, 3);
        assertTrue(range.isOverlapping(new IntegerRange(1, 3)));
        assertTrue(range.isOverlapping(new IntegerRange(2, 5)));
        assertTrue(range.isOverlapping(new IntegerRange(-10, 2)));
        assertTrue(range.isOverlapping(new IntegerRange(1, 2)));
        assertTrue(range.isOverlapping(new IntegerRange(-10, 3)));
    }

    @Test
    void isOverlapping_rejects() {
        IntegerRange range = new IntegerRange(1, 3);
        assertFalse(range.isOverlapping(new IntegerRange(3, 5)));
        assertFalse(range.isOverlapping(new IntegerRange(-10, 0)));
    }

    @Test
    void isSubsetOrEqual_To_accepts() {
        IntegerRange range = new IntegerRange(1, 3);
        assertTrue(range.isSubsetOrEqualTo(new IntegerRange(1, 3)));
        assertTrue(range.isSubsetOrEqualTo(new IntegerRange(0, 3)));
        assertTrue(range.isSubsetOrEqualTo(new IntegerRange(1, 4)));
        assertTrue(range.isSubsetOrEqualTo(new IntegerRange(0, 4)));
    }

    @Test
    void isSubsetOrEqual_To_rejects() {
        IntegerRange range = new IntegerRange(1, 3);
        assertFalse(range.isSubsetOrEqualTo(new IntegerRange(2, 3)));
        assertFalse(range.isSubsetOrEqualTo(new IntegerRange(0, 2)));
        assertFalse(range.isSubsetOrEqualTo(new IntegerRange(-1, 1)));
        assertFalse(range.isSubsetOrEqualTo(new IntegerRange(3, 5)));
    }

    @Test
    void isProperSubsetOf_accepts() {
        assertTrue(new IntegerRange(0,0).isProperSubsetOf(new IntegerRange(-1,1)));

        IntegerRange range = new IntegerRange(1, 3);
        assertTrue(range.isProperSubsetOf(new IntegerRange(0, 4)));
        assertTrue(range.isProperSubsetOf(new IntegerRange(-10, 10)));

        IntegerRange largerRange = new IntegerRange(-2,2);
        assertTrue(new IntegerRange(0, 0).isProperSubsetOf(largerRange));
        assertTrue(new IntegerRange(-1, -1).isProperSubsetOf(largerRange));
        assertTrue(new IntegerRange(1, 1).isProperSubsetOf(largerRange));
        assertTrue(new IntegerRange(-1, 1).isProperSubsetOf(largerRange));
        assertTrue(new IntegerRange(-1, 0).isProperSubsetOf(largerRange));
        assertTrue(new IntegerRange(0, 1).isProperSubsetOf(largerRange));
    }

    @Test
    void isProperSubsetOf_rejects() {
        IntegerRange range = new IntegerRange(1, 3);
        assertFalse(range.isProperSubsetOf(range));
        assertFalse(range.isProperSubsetOf(new IntegerRange(2, 3)));
        assertFalse(range.isProperSubsetOf(new IntegerRange(1, 2)));
        assertFalse(range.isProperSubsetOf(new IntegerRange(0, 2)));
        assertFalse(range.isProperSubsetOf(new IntegerRange(1, 10)));

    }

    @Test
    void isSuperSetOf_accepts() {
        IntegerRange range = new IntegerRange(1, 3);
        assertTrue(range.isSuperSetOf(new IntegerRange(1, 3)));
        assertTrue(range.isSuperSetOf(new IntegerRange(1, 2)));
        assertTrue(range.isSuperSetOf(new IntegerRange(2, 2)));
        assertTrue(range.isSuperSetOf(new IntegerRange(2, 3)));
    }

    @Test
    void isSuperSetOf_rejects() {
        IntegerRange range = new IntegerRange(1, 3);
        assertFalse(range.isSuperSetOf(new IntegerRange(0, 3)));
        assertFalse(range.isSuperSetOf(new IntegerRange(1, 4)));
        assertFalse(range.isSuperSetOf(new IntegerRange(0, 4)));
        assertFalse(range.isSuperSetOf(new IntegerRange(-10, 10)));
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
}