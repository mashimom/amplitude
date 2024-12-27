package org.shimomoto.amplitude;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.Spliterator;
import java.util.function.Consumer;

class ChronoRangeSpliterator<T extends Temporal> implements Spliterator<T> {

    private T current;
    private final T max;
    private final TemporalUnit unit;
    private final long step;

    ChronoRangeSpliterator(T start, T end, TemporalUnit unit, long step) {
        this.current = start;
        this.max = end;
        this.unit = unit;
        this.step = step;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (current.until(max, unit) < 0) {
            action.accept(current);
            current = (T) current.plus(step, unit);
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
        return null; // No splitting for simplicity
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE; // Unknown size
    }

    @Override
    public int characteristics() {
        return ORDERED | DISTINCT | SORTED | NONNULL | IMMUTABLE;
    }
}