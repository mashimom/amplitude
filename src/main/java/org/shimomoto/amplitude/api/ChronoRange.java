package org.shimomoto.amplitude.api;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.stream.Stream;

public interface ChronoRange<T extends Temporal & Comparable<T>> extends Range<T> {
    long getStep(T amount, TemporalUnit unit);
    Stream<T> stream();
    List<Range<T>> splitEvery(TemporalUnit unit, long step);
}