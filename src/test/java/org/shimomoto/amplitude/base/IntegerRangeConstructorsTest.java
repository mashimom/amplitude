package org.shimomoto.amplitude.base;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class IntegerRangeConstructorsTest {
    @Test
    void create_fromValidValues_works() {
        //given
        Integer a = 1;
        Integer b = 2;
        IntegerRange range = new IntegerRange(a, b);
        Optional<IntegerRange> same = IntegerRange.create(a,b);

        assertEquals(a, range.getMin());
        assertEquals(b, range.getMax());
        assertTrue(same.isPresent());
        assertEquals(range, same.get());
    }

    @Disabled
    @Test
    void create_fromValidSet_works() {
        //given
        Integer a = 1;
        Integer b = 2;
        IntegerRange range = new IntegerRange(a, b);
        TreeSet<Integer> limits = Stream.of(a, b)
                .collect(Collectors.toCollection(TreeSet::new));

        SortedSet<IntegerRange> created = IntegerRange.create(limits);

        assertEquals(1, created.size());
        assertEquals(range, created.first());
    }

    @Test
    void create_fromEqualValues_works() {
        assertEquals(Optional.of(new IntegerRange(1, 1)), IntegerRange.create(1, 1));
    }

    @Test
    void create_fromNulls_isEmpty() {
        assertEquals(Optional.empty(), IntegerRange.create(null, null));
        assertEquals(Optional.empty(), IntegerRange.create(1, null));
        assertEquals(Optional.empty(), IntegerRange.create(null, 1));
    }

    @Test
    void create_fromGetMaxToGetMin_isEmpty() {
        assertEquals(Optional.empty(), IntegerRange.create(2, 1));
        assertEquals(Optional.empty(), IntegerRange.create(-1, -10));
    }
}