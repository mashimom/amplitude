package org.shimomoto.amplitude.api;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Representation of a right-open interval, that contains all values between the boundaries,
 * while start is included and end is excluded.
 * <p>
 * This interface defines a range with a closed start (inclusive) and an open (exclusive) end.
 * It provides methods for various operations such as checking containment, disjointness, touching, overlapping,
 * subset, superset, proper set, splitting, union, intersection, and difference.
 * </p>
 * <p>
 * As you can see the set operations apply here, they can be considered a set of all possible values
 * between the boundaries.
 * </p>
 * <p>
 * Example usage: <pre>{@code
 *     Range<Integer> range = new BaseRange<>(1, 10);
 *     boolean contains = range.containsValue(5);
 *     boolean isEmpty = range.isEmpty();
 *}</pre>
 * </p>
 *
 * @author Marco Shimomoto
 * @param <T> A comparable type
 * @see <a href="https://en.wikipedia.org/wiki/Interval_(mathematics)#Definitions_and_terminology">Interval (mathematics) - Definitions and terminoloagy on Wikipedia</a>
 */
public interface Range<T extends Comparable<? super T>> extends Comparable<Range<T>> {
    /**
     * Retrieves the closed (thus included) start of the range.
     * It is the smallest value within the closed-open range.
     *
     * @return the minimum value of the range
     */
    T min();

    /**
     * Retrieves the open (thus excluded) end of the range.
     * It is the first highest value outside the range.
     *
     * @return the maximum value of the range
     */
    T max();

    /**
     * Checks if the range is empty.
     * A range is considered empty if the minimum value is equal to the maximum value.
     * E.g: [1, 1) is an empty range.
     *
     * @return true if the range is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Checks if the specified value is within the range.
     * A value is considered within the range if it is greater than or equal to the minimum value
     * and less than the maximum value of the range.
     * E.g: [1, 3) contains 1, 2, but not 3.
     *
     * @param value the value to check
     * @return true if the value is within the range, false otherwise
     */
    boolean containsValue(@NotNull T value);

    /**
     * Checks if this range is disjoint with the specified range.
     * Two ranges are disjoint if they do not overlap, meaning they have no elements in common.
     * E.g: [1, 3) and [4, 5) are disjoint.
     *
     * @param range the range to check for disjointness
     * @return true if the ranges are disjoint, false otherwise
     */
    boolean isDisjoint(@NotNull Range<T> range);

    /**
     * Checks if this range is touching the specified range.
     * Two ranges are considered touching if there are no values found between them.
     * E.g: [1, 3) and [3, 5) are touching.
     *
     * @param range the range to check for touching
     * @return true if the ranges are touching, false otherwise
     */
    boolean isTouching(@NotNull Range<T> range);

    /**
     * Checks if this range overlaps with the specified range.
     * Two ranges overlap if they have any elements in common.
     * E.g: [1, 3) and [2, 5) overlap.
     *
     * @param range the range to check for overlapping
     * @return true if the ranges overlap, false otherwise
     */
    boolean isOverlapping(@NotNull Range<T> range);

    /**
     * Checks if this range is a subset or equal to the specified range.
     * A range is considered a subset or equal if all elements of this range are contained within the specified range.
     * E.g: [1, 3) is a subset of [0, 5).
     * E.g: [1, 3) is a subset of [1, 3).
     *
     * @param range the range to check against
     * @return true if this range is a subset or equal to the specified range, false otherwise
     */
    boolean isSubsetOrEqualTo(@NotNull Range<T> range);

    /**
     * Checks if this range is a proper subset of the specified range.
     * A range is considered a proper subset if all elements of this range are contained within the specified range,
     * and the specified range contains at least one element not in this range.
     * E.g: [1, 3) is a proper subset of [0, 3).
     * E.g: [1, 3) is a proper subset of [1, 4).
     *
     * @param range the range to check against
     * @return true if this range is a proper subset of the specified range, false otherwise
     */
    boolean isProperSubsetOf(@NotNull Range<T> range);

    /**
     * Checks if this range is a superset of the specified range.
     * A range is considered a superset if it contains all elements of the specified range.
     * E.g: [0, 5) is a superset of [1, 3).
     *
     * @param range the range to check against
     * @return true if this range is a superset of the specified range, false otherwise
     */
    boolean isSuperSetOf(@NotNull Range<T> range);

    /**
     * Splits the range at the specified limit.
     * The resulting list contains two ranges: one from the minimum value to the limit,
     * and another from the limit to the maximum value.
     * E.g: [1, 5) split at 3 results in [1, 3) and [3, 5).
     *
     * @param limit the value at which to split the range
     * @return a list of two ranges resulting from the split
     */
    List<Range<T>> splitAt(@NotNull T limit);

    /**
     * Combines this range with the specified range.
     * The resulting list contains ranges that cover the union of both ranges,
     * and combines them if they have elements in common.
     * E.g: [1, 3) union [1, 3) results in [1, 3).
     * E.g: [1, 3) union [2, 5) results in [1, 5).
     * E.g: [1, 3) union [4, 5) results in [1, 3) and [4, 5).
     *
     * @param other the range to combine with
     * @return a list of ranges resulting from the union
     */
    List<Range<T>> union(@NotNull Range<T> other);

    /**
     * Computes the intersection of this range with the specified range.
     * The intersection of two ranges is the set of elements that are common to both ranges.
     * E.g: [1, 3) intersect [2, 5) results in [2, 3).
     * E.g: [1, 3) intersect [4, 5) results in an empty range.
     *
     * @param other the range to intersect with
     * @return an Optional containing the intersection range, or an empty Optional if the ranges do not intersect
     */
    Optional<Range<T>> intersection(@NotNull Range<T> other);

    /**
     * Computes the difference between this range and the specified range.
     * The difference of two ranges is the set of elements that are in this range but not in the specified range.
     * E.g: [1, 5) difference [2, 3) results in [1, 2) and [3, 5).
     * E.g: [1, 5) difference [1, 5) results in an empty range.
     *
     * @param other the range to subtract from this range
     * @return a list of ranges resulting from the difference
     */
    List<Range<T>> difference(@NotNull Range<T> other);
}
