package org.shimomoto.amplitude.api;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Representation of a closed-open range.
 * <p>
 * This interface defines a range with a closed start (inclusive) and an open end (exclusive).
 * It provides methods for various range operations such as checking containment, disjointness, touching, overlapping,
 * subset, superset, splitting, union, intersection, and difference.
 * </p>
 * <p>
 * Example usage:
 * {@snippet :
 *     Range<Integer> range = new BaseRange<>(1, 10);
 *     boolean contains = range.containsValue(5);
 *     boolean isEmpty = range.isEmpty();
 *}
 * </p>
 *
 * @author Marco Shimomoto
 * @param <T> A comparable type
 * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)">Range (mathematics) on Wikipedia</a>
 */
public interface Range<T extends Comparable<? super T>> extends Comparable<Range<T>> {
    /**
     * Retrieves the closed (thus included) start of the range.
     * It is the smallest value within the closed-open range.
     *
     * @return the minimum value of the range
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)#Minimum_and_maximum">Minimum and maximum on Wikipedia</a>
     */
    T min();

    /**
     * Retrieves the open (thus excluded) end of the range.
     * The actual maximum value within a closed-open range is anything less than the returned value.
     *
     * @return the maximum value of the range
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)#Minimum_and_maximum">Minimum and maximum on Wikipedia</a>
     */
    T max();

    /**
     * Checks if the range is empty.
     * A range is considered empty if the minimum value is equal to the maximum value.
     *
     * @return true if the range is empty, false otherwise
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)#Empty_range">Empty range on Wikipedia</a>
     */
    boolean isEmpty();

    /**
     * Checks if the specified value is within the range.
     * A value is considered within the range if it is greater than or equal to the minimum value
     * and less than the maximum value of the range.
     *
     * @param value the value to check
     * @return true if the value is within the range, false otherwise
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)">Range (mathematics) on Wikipedia</a>
     */
    boolean containsValue(@NotNull T value);

    /**
     * Checks if this range is disjoint with the specified range.
     * Two ranges are disjoint if they do not overlap, meaning they have no elements in common.
     *
     * @param range the range to check for disjointness
     * @return true if the ranges are disjoint, false otherwise
     * @see <a href="https://en.wikipedia.org/wiki/Interval_(mathematics)#Disjoint_intervals">Disjoint intervals on Wikipedia</a>
     */
    boolean isDisjoint(@NotNull Range<T> range);

    /**
     * Checks if this range is touching the specified range.
     * Two ranges are considered touching if they have exactly one point in common.
     * //TODO: definition conflict
     *
     * @param range the range to check for touching
     * @return true if the ranges are touching, false otherwise
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)#Touching_intervals">Touching intervals on Wikipedia</a>
     */
    boolean isTouching(@NotNull Range<T> range);

    /**
     * Checks if this range overlaps with the specified range.
     * Two ranges overlap if they have any elements in common.
     *
     * @param range the range to check for overlapping
     * @return true if the ranges overlap, false otherwise
     * @see <a href="https://en.wikipedia.org/wiki/Interval_(mathematics)#Overlapping_intervals">Overlapping intervals on Wikipedia</a>
     */
    boolean isOverlapping(@NotNull Range<T> range);

    /**
     * Checks if this range is a subset or equal to the specified range.
     * A range is considered a subset or equal if all elements of this range are contained within the specified range.
     *
     * @param range the range to check against
     * @return true if this range is a subset or equal to the specified range, false otherwise
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)#Subset">Subset on Wikipedia</a>
     */
    boolean isSubsetOrEqualTo(@NotNull Range<T> range);

    /**
     * Checks if this range is a proper subset of the specified range.
     * A range is considered a proper subset if all elements of this range are contained within the specified range,
     * and the specified range contains at least one element not in this range.
     *
     * @param range the range to check against
     * @return true if this range is a proper subset of the specified range, false otherwise
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)#Subset">Subset on Wikipedia</a>
     */
    boolean isProperSubsetOf(@NotNull Range<T> range);

    /**
     * Checks if this range is a superset of the specified range.
     * A range is considered a superset if it contains all elements of the specified range.
     *
     * @param range the range to check against
     * @return true if this range is a superset of the specified range, false otherwise
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)#Superset">Superset on Wikipedia</a>
     */
    boolean isSuperSetOf(@NotNull Range<T> range);

    /**
     * Splits the range at the specified limit.
     * The resulting list contains two ranges: one from the minimum value to the limit,
     * and another from the limit to the maximum value.
     *
     * @param limit the value at which to split the range
     * @return a list of two ranges resulting from the split
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)#Splitting_ranges">Splitting ranges on Wikipedia</a>
     */
    List<Range<T>> splitAt(@NotNull T limit);

    /**
     * Combines this range with the specified range.
     * The resulting list contains ranges that cover the union of both ranges.
     *
     * @param other the range to combine with
     * @return a list of ranges resulting from the union
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)#Union">Union on Wikipedia</a>
     */
    List<Range<T>> union(@NotNull Range<T> other);

    /**
     * Computes the intersection of this range with the specified range.
     * The intersection of two ranges is the set of elements that are common to both ranges.
     *
     * @param other the range to intersect with
     * @return an Optional containing the intersection range, or an empty Optional if the ranges do not intersect
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)#Intersection">Intersection on Wikipedia</a>
     */
    Optional<Range<T>> intersection(@NotNull Range<T> other);

    /**
     * Computes the difference between this range and the specified range.
     * The difference of two ranges is the set of elements that are in this range but not in the specified range.
     *
     * @param other the range to subtract from this range
     * @return a list of ranges resulting from the difference
     * @see <a href="https://en.wikipedia.org/wiki/Range_(mathematics)#Difference">Difference on Wikipedia</a>
     */
    List<Range<T>> difference(@NotNull Range<T> other);
}
