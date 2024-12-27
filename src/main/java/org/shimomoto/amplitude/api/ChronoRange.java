package org.shimomoto.amplitude.api;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.stream.Stream;

public interface ChronoRange<T extends Temporal & Comparable<T>> extends Range<T> {
    long getStep(T value, TemporalUnit unit);
    Stream<T> stream();
}