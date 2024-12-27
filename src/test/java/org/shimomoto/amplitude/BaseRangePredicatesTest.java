package org.shimomoto.amplitude;

import org.junit.jupiter.api.Test;
import org.shimomoto.amplitude.api.Range;

import static org.junit.jupiter.api.Assertions.*;

class BaseRangePredicatesTest {

    @Test
    void constructor_works() {
        Range<Integer> range = Ranges.of(1, 2);
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
        Range<Integer> emptyRange = Ranges.of(5,5);
        assertTrue(emptyRange.isEmpty());

        Range<Integer> nonEmptyRange = Ranges.of(5, 6);
        assertFalse(nonEmptyRange.isEmpty());
    }

    @Test
    void testContainsValue() {
        Range<Integer> range = Ranges.of(1, 10);
        assertTrue(range.containsValue(1));
        assertTrue(range.containsValue(5));
        assertTrue(range.containsValue(9));

        assertFalse(range.containsValue(10));
        assertFalse(range.containsValue(0));
    }

    @Test
    void testIsDisjoint() {
        Range<Integer> range1 = Ranges.of(1, 5);
        Range<Integer> range2 = Ranges.of(5, 10);
        Range<Integer> range3 = Ranges.of(0, 1);
        assertTrue(range1.isDisjoint(range2));
        assertTrue(range1.isDisjoint(range3));

        Range<Integer> overlappingRange = Ranges.of(4, 6);
        assertFalse(range1.isDisjoint(overlappingRange));
    }

    @Test
    void testIsTouching() {
        Range<Integer> range1 = Ranges.of(1, 5);
        Range<Integer> range2 = Ranges.of(5, 10);
        Range<Integer> range3 = Ranges.of(0, 1);

        Range<Integer> overlapping = Ranges.of(3, 10);
        Range<Integer> overlapping2 = Ranges.of(0, 3);

        assertTrue(range1.isTouching(range2));
        assertTrue(range1.isTouching(range3));

        assertFalse(range1.isTouching(overlapping));
        assertFalse(range1.isTouching(overlapping2));
    }

    @Test
    void testIsOverlapping() {
        Range<Integer> range1 = Ranges.of(1, 5);
        Range<Integer> range2 = Ranges.of(5, 10);
        Range<Integer> range3 = Ranges.of(0, 1);

        Range<Integer> overlapping = Ranges.of(3, 10);
        Range<Integer> overlapping2 = Ranges.of(0, 3);

        assertFalse(range1.isOverlapping(range2));
        assertFalse(range1.isOverlapping(range3));

        assertTrue(range1.isOverlapping(overlapping));
        assertTrue(range1.isOverlapping(overlapping2));
    }

    @Test
    void testIsSubsetOrEqualTo() {
        Range<Integer> range1 = Ranges.of(1, 5);
        Range<Integer> range2 = Ranges.of(5, 10);

        Range<Integer> subset1a = Ranges.of(2, 4);
        Range<Integer> subset1b = Ranges.of(1, 4);
        Range<Integer> subset2 = Ranges.of(6, 9);

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
        Range<Integer> range1 = Ranges.of(1, 5);
        Range<Integer> range2 = Ranges.of(5, 10);

        Range<Integer> subset1a = Ranges.of(2, 4);
        Range<Integer> subset1b = Ranges.of(1, 4);
        Range<Integer> subset2a = Ranges.of(6, 9);
        Range<Integer> subset2b = Ranges.of(6, 10);

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
        Range<Integer> range1 = Ranges.of(1, 5);
        Range<Integer> range2 = Ranges.of(0, 10);

        Range<Integer> same1 = Ranges.of(1, 5);
        Range<Integer> notSuperSet1 = Ranges.of(2, 20);

        Range<Integer> superset1 = Ranges.of(0, 6);
        Range<Integer> superset2 = Ranges.of(-1, 11);

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