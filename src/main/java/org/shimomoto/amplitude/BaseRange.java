package org.shimomoto.amplitude;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.shimomoto.amplitude.api.Range;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@EqualsAndHashCode
@Getter
class BaseRange<T extends Comparable<? super T>> implements Range<T> {
    protected final T min;
    protected final T max;

    BaseRange(T min, T max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Inverted range is not accepted: min: " + min + ", max: " + max);
        }
        this.min = min;
        this.max = max;
    }

    @Override
    public T getMin() {
        return min;
    }

    @Override
    public T getMax() {
        return max;
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
        return max.compareTo(range.getMin()) <= 0 || range.getMax().compareTo(min) <= 0;
    }

    @Override
    public boolean isTouching(@NotNull Range<T> range) {
        return max.equals(range.getMin()) || range.getMax().equals(min);
    }

    @Override
    public boolean isOverlapping(@NotNull Range<T> range) {
        return min.compareTo(range.getMax()) < 0 && range.getMin().compareTo(max) < 0;
    }

    @Override
    public boolean isSubsetOrEqualTo(@NotNull Range<T> range) {
        return range.getMin().compareTo(min) <= 0 && max.compareTo(range.getMax()) <= 0;
    }

    @Override
    public boolean isProperSubsetOf(@NotNull Range<T> range) {
        return range.getMin().compareTo(min) < 0 && max.compareTo(range.getMax()) < 0;
    }

    @Override
    public boolean isSuperSetOf(@NotNull Range<T> range) {
        return min.compareTo(range.getMin()) <= 0 && max.compareTo(range.getMax()) >= 0;
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
            return List.of(new BaseRange<>(getMinBetween(min, other.getMin()), getMaxBetween(max, other.getMax())));
        }
        return List.of(this, other);
    }

    @Override
    public Optional<Range<T>> intersection(@NotNull Range<T> other) {
        if (this.isOverlapping(other)) {
            return Optional.of(new BaseRange<>(getMaxBetween(min, other.getMin()), getMinBetween(max, other.getMax())));
        }
        return Optional.empty();
    }

    @Override
    public List<Range<T>> difference(@NotNull Range<T> other) {
        if (this.isSubsetOrEqualTo(other)) {
            return List.of();
        } else if (this.isSuperSetOf(other)) {
            return List.of(new BaseRange<>(min, other.getMin()), new BaseRange<>(other.getMax(), max));
        } else if (this.containsValue(other.getMin())) {
            return List.of(new BaseRange<>(min, other.getMin()));
        } else if (this.containsValue(other.getMax())) {
            return List.of(new BaseRange<>(other.getMax(), max));
        }
        return List.of(this);
    }

    @Override
    public String toString() {
        return "[" + min.toString() + ", " + max.toString() + ")";
    }
}
