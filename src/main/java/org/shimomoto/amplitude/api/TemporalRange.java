package org.shimomoto.amplitude.api;

import java.time.temporal.Temporal;
import java.util.stream.Stream;

/**
 * Representation of a temporal range.
 * <p>
 * This interface extends {@link Range} to provide additional functionality specific to temporal types.
 * It includes methods for splitting the temporal range into smaller temporal ranges.
 * </p>
 * <p>
 * Example usage:
 * {@snippet :
 *     TemporalRange<LocalDate> dateRange = Ranges.temporalRange(LocalDate.now(), LocalDate.now().plusDays(10), ChronoUnit.DAYS, 1);
 *     Stream<TemporalRange<LocalDate>> splitRanges = dateRange.split(); //each range represents a day, from now to 10 days from now
 *
 *     TemporalRange<LocalDateTime> dateTimeRange = Ranges.temporalRange(LocalDateTime.now(), LocalDateTime.now().plusHours(10), ChronoUnit.HOURS, 1);
 *     TemporalRange<LocalTime> timeRange = Ranges.temporalRange(LocalTime.now(), LocalTime.now().plusMinutes(30), ChronoUnit.MINUTES, 5);
 *     TemporalRange<ZonedDateTime> zonedDateTimeRange = Ranges.temporalRange(ZonedDateTime.now(), ZonedDateTime.now().plusDays(5), ChronoUnit.DAYS, 1);
 *}
 * </p>
 *
 * @param <T> A temporal and comparable type
 * @see Range
 * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)">Range (mathematics) on Wikipedia</a>
 */
public interface TemporalRange<T extends Temporal & Comparable<? super T>> extends Range<T> {
    /**
     * Splits the temporal range into smaller ranges.
     * The resulting stream contains temporal ranges that are subdivisions of the original range.
     * The split is done in equal parts of the specified step size, except for the last range which may be smaller.
     * E.g.: [2025-01-01, 2025-01-10) split by days with a step of 3 will result in 4 ranges,
     * each representing 3 days plus a range for the remaining day.
     *
     * @return a stream of temporal ranges resulting from the split
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)#Splitting_ranges">Splitting ranges on Wikipedia</a>
     */
    Stream<TemporalRange<T>> split();
}
