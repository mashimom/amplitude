package org.shimomoto.amplitude;

import org.junit.jupiter.api.Test;
import org.shimomoto.amplitude.api.Range;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.shimomoto.amplitude.Ranges.of;

class BaseRangeTest {

    @Test
    void testEquals() {
        Range<Integer> range1 = of(1, 3);
        Range<Integer> range2 = of(1, 3);
        Range<Integer> range3 = of(2, 4);

        assertEquals(range1, range2);
        assertNotEquals(range1, range3);

        Range<Character> charRange1 = of('a', 'c');
        Range<Character> charRange2 = of('a', 'c');
        Range<Character> charRange3 = of('b', 'd');

        assertEquals(charRange1, charRange2);
        assertNotEquals(charRange1, charRange3);

        Range<LocalDateTime> dateTimeRange1 = of(
                LocalDateTime.of(2023, 1, 1, 0, 0),
                LocalDateTime.of(2023, 1, 2, 0, 0));

        Range<LocalDateTime> dateTimeRange2 = of(
                LocalDateTime.of(2023, 1, 1, 0, 0),
                LocalDateTime.of(2023, 1, 2, 0, 0));

        Range<LocalDateTime> dateTimeRange3 = of(
                LocalDateTime.of(2023, 1, 2, 0, 0),
                LocalDateTime.of(2023, 1, 3, 0, 0));

        assertEquals(dateTimeRange1, dateTimeRange2);
        assertNotEquals(dateTimeRange1, dateTimeRange3);

        Range<BigDecimal> bigDecimalRange1 = of(new BigDecimal("1.0"), new BigDecimal("3.0"));
        Range<BigDecimal> bigDecimalRange2 = of(new BigDecimal("1.0"), new BigDecimal("3.0"));
        Range<BigDecimal> bigDecimalRange3 = of(new BigDecimal("2.0"), new BigDecimal("4.0"));

        assertEquals(bigDecimalRange1, bigDecimalRange2);
        assertNotEquals(bigDecimalRange1, bigDecimalRange3);
    }

    @Test
    void testHashCode() {
        Range<Integer> range1 = of(1, 3);
        Range<Integer> range2 = of(1, 3);
        Range<Integer> range3 = of(2, 4);

        assertEquals(range1.hashCode(), range2.hashCode());
        assertNotEquals(range1.hashCode(), range3.hashCode());
    }

    @Test
    void testToString() {
        Range<Integer> intRange = of(1, 3);
        assertEquals("[1, 3)", intRange.toString());

        Range<Double> doubleRange = of(1.0, 3.0);
        assertEquals("[1.0, 3.0)", doubleRange.toString());

        Range<Character> charRange = of('a', 'c');
        assertEquals("[a, c)", charRange.toString());

    Range<LocalDateTime> dateTimeRange = of(
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 1, 2, 0, 0));
        assertEquals("[2023-01-01T00:00, 2023-01-02T00:00)", dateTimeRange.toString());
    }

    //---------- Operations ----------
    @Test
    void split_At_withContainedLimit_works() {
        Range<Integer> base = of(1, 3);
        assertEquals(List.of(of(1, 2), of(2, 3)), base.splitAt(2));
    }

    @Test
    void split_At_withContainedLimit_String_works() {
        Range<String> base = of("a", "c");
        assertEquals(List.of(of("a", "b"), of("b", "c")), base.splitAt("b"));
    }

    @Test
    void split_At_withoutContainedLimit_Double_works() {
        Range<Double> base = of(1.0, 3.0);
        assertEquals(List.of(of(1.0, 3.0)), base.splitAt(0.0));
        assertEquals(List.of(of(1.0, 3.0)), base.splitAt(3.0));
    }

    @Test
    void union_equal_works() {
        Range<Integer> base = of(1, 3);
        assertEquals(List.of(base), base.union(base));
    }

    @Test
    void union_touching_String_works() {
        Range<String> base = of("f", "m");
        assertEquals(List.of(of("f", "q")), base.union(of("m", "q")));
        assertEquals(List.of(of("a", "m")), base.union(of("a", "f")));
    }

    @Test
    void union_overlapping_Double_works() {
        Range<Double> base = of(1.0, 3.0);
        assertEquals(List.of(of(1.0, 10.0)), base.union(of(2.0, 10.0)));
        assertEquals(List.of(of(1.0, 3.0)), base.union(of(2.0, 2.0)));
    }

    @Test
    void union_disjoint_String_works() {
        Range<String> base = of("a", "c");
        assertEquals(List.of(of("a", "c"), of("d", "e")), base.union(of("d", "e")));
        assertEquals(List.of(of("a", "c"), of("x", "y")), base.union(of("x", "y")));
    }

    @Test
    void intersection_disjoint_works() {
        Range<Integer> base = of(1, 3);
        assertTrue(base.intersection(of(3, 4)).isEmpty());
        assertTrue(base.intersection(of(0, 1)).isEmpty());
    }

    @Test
    void intersection_overlapping_String_works() {
        Range<String> base = of("d", "f");
        assertEquals(Optional.of(of("d", "e")), base.intersection(of("a", "e")));
        assertEquals(Optional.of(of("e", "f")), base.intersection(of("e", "h")));
    }

    @Test
    void difference_disjoint_works() {
        Range<Integer> base = of(1, 3);
        assertEquals(List.of(base), base.difference(of(3, 4)));
        assertEquals(List.of(base), base.difference(of(-1, 1)));
    }

    @Test
    void difference_subsetOrEqual_works() {
        Range<Integer> base = of(1, 3);
        assertTrue(base.difference(of(0, 4)).isEmpty());
        assertTrue(base.difference(of(0, 3)).isEmpty());
        assertTrue(base.difference(of(1, 4)).isEmpty());
        assertTrue(base.difference(base).isEmpty());
    }

    @Test
    void difference_superset_Double_works() {
        Range<Double> base = of(1.0, 3.0);
        assertEquals(List.of(of(1.0, 2.0), of(2.0, 3.0)), base.difference(of(2.0, 2.0)));
        Range<Double> largerRange = of(-10.0, 10.0);
        assertEquals(List.of(of(-10.0, -5.0), of(5.0, 10.0)), largerRange.difference(of(-5.0, 5.0)));
    }

    @Test
    void difference_leftOverlap_String_works() {
        Range<String> base = of("a", "c");
        assertEquals(List.of(of("a", "b")), base.difference(of("b", "e")));
    }

    @Test
    void difference_rightOverlap_String_works() {
        Range<String> base = of("a", "c");
        assertEquals(List.of(of("a", "b")), base.difference(of("b", "d")));
    }
}