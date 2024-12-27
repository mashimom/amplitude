package org.shimomoto.amplitude.base;

import org.jetbrains.annotations.NotNull;
import org.shimomoto.amplitude.api.Range;

import java.util.*;

// it is broken because it needs a higher resolution inner type to represent the entire range,
// correct implementation would use BigInteger and recieve the Type as a parameter
public record IntegerRange(@NotNull Integer getMin, @NotNull Integer getMax) implements Range<Integer, IntegerRange> {
    public static Optional<IntegerRange> create(final Integer min, final Integer max) {
        if (min == null || max == null) {
            return Optional.empty();
        } else if (min > max) {
            return Optional.empty();
        } else if (min.equals(max)) {
            return Optional.of(new IntegerRange(min, max));
        }
        return Optional.of(new IntegerRange(min, max));
    }

    public static SortedSet<IntegerRange> create(SortedSet<@NotNull Integer> limits) {
        TreeSet<IntegerRange> ranges = new TreeSet<>();
        //noinspection ResultOfMethodCallIgnored
        limits.stream()
                .reduce((a, b) -> {
                    ranges.add(new IntegerRange(a, b));
                    return b;
                });
        return ranges;
    }

    @Override
    public boolean isEmpty() {
        return getMin == getMax;
    }

    @Override
    public boolean containsValue(Integer value) {
        return getMin <= value && value < getMax;
    }

    @Override
    public boolean isDisjoint(IntegerRange range) {
        return getMax <= range.getMin || range.getMax <= getMin;
    }

    @Override
    public boolean isTouching(IntegerRange range) {
        return getMax == range.getMin || range.getMax == getMin;
    }

    @Override
    public boolean isOverlapping(IntegerRange range) {
        return getMin < range.getMax && range.getMin < getMax;
    }

    @Override
    public boolean isSubsetOrEqualTo(IntegerRange range) {
        return range.getMin <= getMin && getMax <= range.getMax;
    }

    @Override
    public boolean isProperSubsetOf(IntegerRange range) {
        return range.getMin < getMin && getMax < range.getMax;
    }

    @Override
    public boolean isSuperSetOf(IntegerRange range) {
        return getMin <= range.getMin && getMax >= range.getMax;
    }

    @Override
    public List<IntegerRange> splitAt(Integer limit) {
        if (containsValue(limit)) {
            return List.of(new IntegerRange(this.getMin(), limit), new IntegerRange(limit, this.getMax()));
        }
        return List.of(this);
    }

    @Override
    public List<IntegerRange> union(IntegerRange other) {
        if(this.equals(other)) {
            return List.of(this);
        }
        if(this.isOverlapping(other) || this.isTouching(other)) {
            return List.of(new IntegerRange(Math.min(getMin, other.getMin()), Math.max(getMax, other.getMax())));
        }
        return List.of(this, other);
    }

    @Override
    public Optional<IntegerRange> intersection(IntegerRange other) {
        if(this.isOverlapping(other)){
            return Optional.of(new IntegerRange(Math.max(getMin, other.getMin()), Math.min(getMax, other.getMax())));
        }
        return Optional.empty();
    }

    @Override
    public List<IntegerRange> difference(@NotNull IntegerRange other) {
        if (this.isSubsetOrEqualTo(other)) {
            return List.of();
        }
        else if (this.isSuperSetOf(other)) {
            return List.of(new IntegerRange(getMin, other.getMin()), new IntegerRange(other.getMax(), getMax));
        }
        else if(this.containsValue(other.getMin)) {
            return List.of(new IntegerRange(getMin, other.getMin()));
        }
        else if(this.containsValue(other.getMax)) {
            return List.of(new IntegerRange(other.getMax(), getMax));
        }
        return List.of(this);
    }
}
