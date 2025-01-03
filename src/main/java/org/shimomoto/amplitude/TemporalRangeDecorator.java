package org.shimomoto.amplitude;

import lombok.experimental.Delegate;
import org.shimomoto.amplitude.api.Range;
import org.shimomoto.amplitude.api.TemporalRange;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

record TemporalRangeDecorator<T extends Temporal & Comparable<? super T>>(
        @Delegate Range<T> decorated,
        TemporalUnit unit,
        long step
) implements TemporalRange<T> {

    @Override
    public Stream<TemporalRange<T>> split() {
        long total = min().until(max(), unit);

        var sameSizeSplit = LongStream.iterate(0, l -> l + step)
                .limit(total / step)
                .mapToObj(l -> Ranges.chronoRange(
                        (T) min().plus(l, unit),
                        (T) min().plus(l + step, unit),
                        unit,
                        step));

        if (total % step != 0) {
            return Stream.concat(
                    sameSizeSplit,
                    Stream.of(Ranges.chronoRange(
                            (T) max().minus(total % step, unit),
                            max(),
                            unit,
                            step
                    )));
        }
        return sameSizeSplit;
    }
}