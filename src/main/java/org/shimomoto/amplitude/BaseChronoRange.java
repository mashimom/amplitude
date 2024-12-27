package org.shimomoto.amplitude;

import org.shimomoto.amplitude.api.ChronoRange;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BaseChronoRange<T extends Temporal & Comparable<T>> extends BaseRange<T> implements ChronoRange<T> {

    private final TemporalUnit unit;
    private final long step;

    public BaseChronoRange(T min, T max, TemporalUnit unit, long step) {
        super(min, max);
        this.unit = unit;
        this.step = step;
    }

    @Override
    public long getStep(T value, TemporalUnit unit) {
        return step;
    }

    @Override
    public Stream<T> stream() {
        return StreamSupport.stream(new ChronoRangeSpliterator<>(min, max, unit, step), false);
    }
}