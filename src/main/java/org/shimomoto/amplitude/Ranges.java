package org.shimomoto.amplitude;


import org.shimomoto.amplitude.api.Range;
import org.shimomoto.amplitude.api.TemporalRange;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

public final class Ranges {
    private Ranges() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Comparable<? super T>> Range<T> of(T min, T max) {
        return new BaseRange<>(min, max);
    }

    public static <T extends Temporal & Comparable<? super T>> TemporalRange<T> chronoRange(T min, T max, TemporalUnit unit, long step) {
        //throws exception if the unit is inadequate for the Temporal type, so it does not build invalid range
        min.plus(1, unit);
        return new TemporalRangeDecorator<>(of(min, max), unit, step);
    }
}