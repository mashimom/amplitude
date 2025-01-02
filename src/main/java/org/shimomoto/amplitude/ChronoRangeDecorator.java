package org.shimomoto.amplitude;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import org.shimomoto.amplitude.api.ChronoRange;
import org.shimomoto.amplitude.api.Range;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@AllArgsConstructor
class ChronoRangeDecorator<T extends Temporal & Comparable<T>> implements ChronoRange<T> {

    @Delegate
    private final Range<T> decorated;
    private final TemporalUnit unit;
    private final long step;

    @Override
    public long getStep(TemporalUnit unit) {
        return step;
    }

    @Override
    public Stream<T> stream() {
        return StreamSupport.stream(new ChronoRangeSpliterator<>(decorated.getMin(), decorated.getMax(), unit, step), false);
    }
}