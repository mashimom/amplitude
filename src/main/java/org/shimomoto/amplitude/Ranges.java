package org.shimomoto.amplitude;

import org.shimomoto.amplitude.api.Range;
import org.shimomoto.amplitude.api.TemporalRange;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

/**
 * Utility class for creating range instances.
 * <p>
 * This class provides static methods to create instances of {@link Range} and {@link TemporalRange}.
 * It includes methods to create ranges for comparable types and temporal types(with specific units and step size).
 * The class is designed to be non-instantiable and throws {@link UnsupportedOperationException} if an attempt is made to instantiate it.
 * </p>
 * <p>
 * Example usage:
 * {@snippet :
 *     Range<Integer> intRange = Ranges.of(1, 10);
 *     TemporalRange<LocalDate> dateRange = Ranges.temporalRange(LocalDate.now(), LocalDate.now().plusDays(10), ChronoUnit.DAYS, 1);
 *}
 * </p>
 *
 * @author Marco Shimomoto
 * @see Range
 * @see TemporalRange
 */
public final class Ranges {
    /*
     * Private constructor to prevent instantiation.
     * Throws UnsupportedOperationException if called.
     */
    private Ranges() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a range with the specified minimum and maximum values.
     *
     * @param min the minimum value of the range
     * @param max the maximum value of the range
     * @param <T> the type of the range elements, which must be comparable
     * @return a new Range instance with the specified minimum and maximum values
     * @throws IllegalArgumentException if min is greater than max
     * @see Range
     */
    public static <T extends Comparable<? super T>> Range<T> of(T min, T max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException(String.format("Inverted range is not accepted: min: %s, max: %s", min, max));
        }
        return new BaseRange<>(min, max);
    }

    /**
     * Creates a temporal range with the specified minimum and maximum values, temporal unit, and step.
     * Throws an exception if the unit is inadequate for the Temporal type.
     *
     * @param min  the minimum value of the temporal range
     * @param max  the maximum value of the temporal range
     * @param unit the temporal unit for the range
     * @param step the step size for the range
     * @param <T>  the type of the temporal range elements, which must be temporal and comparable
     * @return a new TemporalRange instance with the specified parameters
     * @throws UnsupportedTemporalTypeException if the unit is inadequate for the Temporal type
     * @see TemporalRange
     */
    public static <T extends Temporal & Comparable<? super T>> TemporalRange<T> temporalRange(T min, T max, TemporalUnit unit, long step) {
        // Throws exception if the unit is inadequate for the Temporal type, so it does not build invalid range
        min.plus(1, unit);
        return new TemporalRangeDecorator<>(of(min, max), unit, step);
    }
}