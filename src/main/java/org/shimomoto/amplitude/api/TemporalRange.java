package org.shimomoto.amplitude.api;

import java.time.temporal.Temporal;
import java.util.stream.Stream;

public interface TemporalRange<T extends Temporal & Comparable<? super T>> extends Range<T> {
    Stream<TemporalRange<T>> split();
}
