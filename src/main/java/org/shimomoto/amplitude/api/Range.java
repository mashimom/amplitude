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
public interface Range<T extends Comparable<? super T>> extends Comparable<Range<T>> {
    T min();

    T max();

    boolean isEmpty();
    boolean containsValue(@NotNull T value);
    boolean isDisjoint(@NotNull Range<T> range);
    boolean isTouching(@NotNull Range<T> range);
    boolean isOverlapping(@NotNull Range<T> range);
    boolean isSubsetOrEqualTo(@NotNull Range<T> range);
    boolean isProperSubsetOf(@NotNull Range<T> range);
    boolean isSuperSetOf(@NotNull Range<T> range);

    List<Range<T>> splitAt(@NotNull T limit);

    List<Range<T>> union(@NotNull Range<T> other);
    Optional<Range<T>> intersection(@NotNull Range<T> other);
    List<Range<T>> difference(@NotNull Range<T> other);
}
