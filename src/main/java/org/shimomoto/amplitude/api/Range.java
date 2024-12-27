package org.shimomoto.amplitude.api;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Representation of a closed-open range.
 * All set operations apply.
 *
 * @param <T> A comparable type
 */
public interface Range<T extends Comparable<? super T>, R extends Range<T, R>> {
    T getMin();
    T getMax();

    boolean isEmpty();
    boolean containsValue(@NotNull T value);
    boolean isDisjoint(@NotNull R range);
    boolean isTouching(@NotNull R range);
    boolean isOverlapping(@NotNull R range);
    boolean isSubsetOrEqualTo(@NotNull R range);
    boolean isProperSubsetOf(@NotNull R range);
    boolean isSuperSetOf(@NotNull R range);

    List<R> splitAt(@NotNull T limit);

    List<R> union(@NotNull R other);
    Optional<R> intersection(@NotNull R other);
    List<R> difference(@NotNull R other);
}
