package org.shimomoto.amplitude;


import org.shimomoto.amplitude.api.ChronoRange;
import org.shimomoto.amplitude.api.Range;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

public final class Ranges {
    private Ranges() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Comparable<? super T>> Range<T> of(T min, T max) {
        return new BaseRange<>(min, max);
    }
    public static <T extends Temporal & Comparable<T>> ChronoRange<T> chronoRange(T min, T max, TemporalUnit unit, long step) {
        return new BaseChronoRange<>(min, max, unit, step);
    }
}