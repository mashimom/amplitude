package org.shimomoto.amplitude;

import org.jetbrains.annotations.NotNull;
import org.shimomoto.amplitude.api.Range;

import java.util.List;
import java.util.Optional;

record BaseRange<T extends Comparable<? super T>>(T min, T max) implements Range<T> {
    BaseRange {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException(String.format("Inverted range is not accepted: min: %s, max: %s", min, max));
        }
    }

    /**
     * @param a first value
     * @param b second value
     * @return the maximum value between a and b
     */
    private T getMaxBetween(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    /**
     * @param a first value
     * @param b second value
     * @return the minimum value between a and b
     */
    private T getMinBetween(T a, T b) {
        return a.compareTo(b) < 0 ? a : b;
    }

    @Override
    public boolean isEmpty() {
        return min.equals(max);
    }

    @Override
    public boolean containsValue(@NotNull T value) {
        return min.compareTo(value) <= 0 && value.compareTo(max) < 0;
    }

    @Override
    public boolean isDisjoint(@NotNull Range<T> range) {
        return max.compareTo(range.min()) <= 0 || range.max().compareTo(min) <= 0;
    }

    @Override
    public boolean isTouching(@NotNull Range<T> range) {
        return max.equals(range.min()) || range.max().equals(min);
    }

    @Override
    public boolean isOverlapping(@NotNull Range<T> range) {
        return min.compareTo(range.max()) < 0 && range.min().compareTo(max) < 0;
    }

    @Override
    public boolean isSubsetOrEqualTo(@NotNull Range<T> range) {
        return range.min().compareTo(min) <= 0 && max.compareTo(range.max()) <= 0;
    }

    @Override
    public boolean isProperSubsetOf(@NotNull Range<T> range) {
        return range.min().compareTo(min) < 0 && max.compareTo(range.max()) <= 0
                || range.min().compareTo(min) <= 0 && max.compareTo(range.max()) < 0;
    }

    @Override
    public boolean isSuperSetOf(@NotNull Range<T> range) {
        return min.compareTo(range.min()) <= 0 && max.compareTo(range.max()) >= 0;
    }

    @Override
    public List<Range<T>> splitAt(@NotNull T limit) {
        if (containsValue(limit)) {
            return List.of(new BaseRange<>(min, limit), new BaseRange<>(limit, max));
        }
        return List.of(this);
    }

    @Override
    public List<Range<T>> union(@NotNull Range<T> other) {
        if (this.equals(other)) {
            return List.of(this);
        }
        if (this.isOverlapping(other) || this.isTouching(other)) {
            return List.of(new BaseRange<>(getMinBetween(min, other.min()), getMaxBetween(max, other.max())));
        }
        return List.of(this, other);
    }

    @Override
    public Optional<Range<T>> intersection(@NotNull Range<T> other) {
        if (this.isOverlapping(other)) {
            return Optional.of(new BaseRange<>(getMaxBetween(min, other.min()), getMinBetween(max, other.max())));
        }
        return Optional.empty();
    }

    @Override
    public List<Range<T>> difference(@NotNull Range<T> other) {
        if (this.equals(other) || other.isSuperSetOf(this)) {
            return List.of();
        } else if (this.isSuperSetOf(other)) {
            return List.of(new BaseRange<>(min, other.min()), new BaseRange<>(other.max(), max));
        } else if (this.containsValue(other.min())) {
            return List.of(new BaseRange<>(min, other.min()));
        } else if (this.containsValue(other.max())) {
            return List.of(new BaseRange<>(other.max(), max));
        }
        return List.of(this);
    }

    @Override
    public String toString() {
        return "[" + min.toString() + ", " + max.toString() + ")";
    }

    @Override
    public int compareTo(Range<T> other) {
        int minComparison = this.min.compareTo(other.min());
        if (minComparison != 0) {
            return minComparison;
        }
        return this.max.compareTo(other.max());
    }
}
