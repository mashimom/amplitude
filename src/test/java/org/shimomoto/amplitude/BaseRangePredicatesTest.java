package org.shimomoto.amplitude;

import org.junit.jupiter.api.Test;
import org.shimomoto.amplitude.api.ContinuousRange;

import static org.junit.jupiter.api.Assertions.*;

class BaseRangePredicatesTest {

    @Test
    void constructor_works() {
        ContinuousRange<Integer> range = Ranges.of(1, 2);
        assertEquals(1, range.getMin());
        assertEquals(2, range.getMax());

        try {
            Ranges.of(2, 1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Inverted range is not accepted: min: 2, max: 1", e.getMessage());
        }
    }

    @Test
    void testIsEmpty() {
        ContinuousRange<Integer> emptyRange = Ranges.of(5,5);
        assertTrue(emptyRange.isEmpty());

        ContinuousRange<Integer> nonEmptyRange = Ranges.of(5, 6);
        assertFalse(nonEmptyRange.isEmpty());
    }

    @Test
    void testContainsValue() {
        ContinuousRange<Integer> range = Ranges.of(1, 10);
        assertTrue(range.containsValue(1));
        assertTrue(range.containsValue(5));
        assertTrue(range.containsValue(9));

        assertFalse(range.containsValue(10));
        assertFalse(range.containsValue(0));
    }

    @Test
    void testIsDisjoint() {
        ContinuousRange<Integer> range1 = Ranges.of(1, 5);
        ContinuousRange<Integer> range2 = Ranges.of(5, 10);
        ContinuousRange<Integer> range3 = Ranges.of(0, 1);
        assertTrue(range1.isDisjoint(range2));
        assertTrue(range1.isDisjoint(range3));

        ContinuousRange<Integer> overlappingRange = Ranges.of(4, 6);
        assertFalse(range1.isDisjoint(overlappingRange));
    }

    @Test
    void testIsTouching() {
        ContinuousRange<Integer> range1 = Ranges.of(1, 5);
        ContinuousRange<Integer> range2 = Ranges.of(5, 10);
        ContinuousRange<Integer> range3 = Ranges.of(0, 1);

        ContinuousRange<Integer> overlapping = Ranges.of(3, 10);
        ContinuousRange<Integer> overlapping2 = Ranges.of(0, 3);

        assertTrue(range1.isTouching(range2));
        assertTrue(range1.isTouching(range3));

        assertFalse(range1.isTouching(overlapping));
        assertFalse(range1.isTouching(overlapping2));
    }

    @Test
    void testIsOverlapping() {
        ContinuousRange<Integer> range1 = Ranges.of(1, 5);
        ContinuousRange<Integer> range2 = Ranges.of(5, 10);
        ContinuousRange<Integer> range3 = Ranges.of(0, 1);

        ContinuousRange<Integer> overlapping = Ranges.of(3, 10);
        ContinuousRange<Integer> overlapping2 = Ranges.of(0, 3);

        assertFalse(range1.isOverlapping(range2));
        assertFalse(range1.isOverlapping(range3));

        assertTrue(range1.isOverlapping(overlapping));
        assertTrue(range1.isOverlapping(overlapping2));
    }

    @Test
    void testIsSubsetOrEqualTo() {
        ContinuousRange<Integer> range1 = Ranges.of(1, 5);
        ContinuousRange<Integer> range2 = Ranges.of(5, 10);

        ContinuousRange<Integer> subset1a = Ranges.of(2, 4);
        ContinuousRange<Integer> subset1b = Ranges.of(1, 4);
        ContinuousRange<Integer> subset2 = Ranges.of(6, 9);

        assertFalse(range1.isSubsetOrEqualTo(range2));
        assertFalse(range2.isSubsetOrEqualTo(range1));

        assertTrue(range1.isSubsetOrEqualTo(range1));
        assertTrue(range2.isSubsetOrEqualTo(range2));

        assertTrue(subset1a.isSubsetOrEqualTo(range1));
        assertTrue(subset1b.isSubsetOrEqualTo(range1));
        assertTrue(subset2.isSubsetOrEqualTo(range2));
    }

    @Test
    void testIsProperSubsetOf() {
        ContinuousRange<Integer> range1 = Ranges.of(1, 5);
        ContinuousRange<Integer> range2 = Ranges.of(5, 10);

        ContinuousRange<Integer> subset1a = Ranges.of(2, 4);
        ContinuousRange<Integer> subset1b = Ranges.of(1, 4);
        ContinuousRange<Integer> subset2a = Ranges.of(6, 9);
        ContinuousRange<Integer> subset2b = Ranges.of(6, 10);

        assertFalse(range1.isProperSubsetOf(range2));
        assertFalse(range2.isProperSubsetOf(range1));

        assertFalse(range1.isProperSubsetOf(range1));
        assertFalse(range2.isProperSubsetOf(range2));

        assertFalse(subset1b.isProperSubsetOf(range1));
        assertFalse(subset2b.isProperSubsetOf(range2));

        assertTrue(subset1a.isProperSubsetOf(range1));
        assertTrue(subset2a.isProperSubsetOf(range2));
    }

    @Test
    void testIsSuperSetOf() {
        ContinuousRange<Integer> range1 = Ranges.of(1, 5);
        ContinuousRange<Integer> range2 = Ranges.of(0, 10);

        ContinuousRange<Integer> same1 = Ranges.of(1, 5);
        ContinuousRange<Integer> notSuperSet1 = Ranges.of(2, 20);

        ContinuousRange<Integer> superset1 = Ranges.of(0, 6);
        ContinuousRange<Integer> superset2 = Ranges.of(-1, 11);

        assertTrue(range2.isSuperSetOf(range1));
        assertTrue(same1.isSuperSetOf(range1));
        assertTrue(range2.isSuperSetOf(range2));

        assertTrue(superset1.isSuperSetOf(range1));
        assertTrue(superset2.isSuperSetOf(range2));
        assertTrue(superset2.isSuperSetOf(range1));

        assertFalse(range1.isSuperSetOf(range2));

        assertFalse(superset1.isSuperSetOf(range2));
        assertFalse(range1.isSuperSetOf(range2));
        assertFalse(notSuperSet1.isSuperSetOf(range1));
        assertFalse(notSuperSet1.isSuperSetOf(range2));
    }
}