package org.shimomoto.amplitude.api;

import java.util.List;
import java.util.Optional;

/**
 * Representation of a closed-open range.
 * All set operations apply.
 *
 * @param <T> A comparable type
 */
public interface Range<T extends Comparable<? super T>, R extends Range<T,? super R>> {
    T min();
    T max();
    boolean isEmpty();
    boolean containsValue(final T value);
    boolean isDisjoint(R range);
    boolean isTouching(R range);
    boolean isOverlapping(R range);
    boolean isSubsetOrEqualTo(R range);
    boolean isProperSubsetOf(R range);
    boolean isSuperSetOf(R range);
    @Deprecated
    boolean contains(final R range);
    @Deprecated
    default boolean intersects(final R other) {
        return isOverlapping(other);
    }
    List<R> splitAt(final T limit);
    Optional<R> intersection(final R other);
    List<R> union(final R other);
    List<R> difference(final R other);
}
