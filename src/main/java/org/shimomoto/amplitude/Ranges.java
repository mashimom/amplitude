package org.shimomoto.amplitude;

import org.shimomoto.amplitude.api.ContinuousRange;

public final class Ranges {
    private Ranges() {
        throw new UnsupportedOperationException();
    }

    public static <T extends Comparable<T>> ContinuousRange<T> of(T min, T max) {
        return new BaseRange<>(min, max);
    }
}