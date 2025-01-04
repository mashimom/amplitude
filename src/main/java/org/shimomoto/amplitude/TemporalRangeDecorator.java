package org.shimomoto.amplitude;

import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
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
        var sameSizeSplit = getUniformSplits(total);
        if (total % step == 0) {
            return sameSizeSplit;
        }

        return Stream.concat(sameSizeSplit, getRemainderSplit(total));
    }

    private @NotNull Stream<TemporalRange<T>> getRemainderSplit(long total) {
        //noinspection unchecked
        return Stream.of(Ranges.temporalRange(
                (T) max().minus(total % step, unit),
                max(),
                unit,
                step
        ));
    }

    private @NotNull Stream<TemporalRange<T>> getUniformSplits(long total) {
        //noinspection unchecked
        return LongStream.range(0, total / step)
                .mapToObj(l -> Ranges.temporalRange(
                        (T) min().plus(l * step, unit),
                        (T) min().plus((l + 1) * step, unit),
                        unit,
                        step));
    }
}