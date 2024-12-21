package org.shimomoto.amplitude.base;

import org.jetbrains.annotations.NotNull;
import org.shimomoto.amplitude.api.Range;

import java.util.*;

// it is broken because it needs a higher resolution inner type to represent the entire range,
// correct implementation would use BigInteger and recieve the Type as a parameter
public record IntegerRange(@NotNull Integer min, @NotNull Integer max) implements Range<Integer, IntegerRange> {
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
        return min == max;
    }

    @Override
    public boolean containsValue(Integer value) {
        return min <= value && value < max;
    }

    @Override
    public boolean isDisjoint(IntegerRange range) {
        return max <= range.min || range.max <= min;
    }

    @Override
    public boolean isTouching(IntegerRange range) {
        return max == range.min || range.max == min;
    }

    @Override
    public boolean isOverlapping(IntegerRange range) {
        return min < range.max && range.min < max;
    }

    @Override
    public boolean isSubsetOrEqualTo(IntegerRange range) {
        return range.min <= min && max <= range.max;
    }

    @Override
    public boolean isProperSubsetOf(IntegerRange range) {
        return range.min < min && max < range.max;
    }

    @Override
    public boolean isSuperSetOf(IntegerRange range) {
        return min <= range.min && max >= range.max;
    }

    @Override
    public boolean contains(IntegerRange range) {
        return min <= range.min() && range.max() <= max;
    }

    @Override
    public List<IntegerRange> union(IntegerRange other) {
//        if (this.equals(other)) {
//            return List.of(this);
//        } else if (this.containsValue(other.min()) && !this.containsValue(other.max())) {
//            return List.of(new IntegerRange(this.min(), other.max()));
//        } else if (!this.containsValue(other.min()) && this.containsValue(other.max())) {
//            return List.of(new IntegerRange(other.min(), this.max()));
//        } else if (this.containsValue(other.min()) && this.containsValue(other.max())) {
//            return List.of(this);
//        } else if (other.containsValue(this.min()) && other.containsValue(this.max())) {
//            return List.of(other);
//        }
//        return List.of(this, other);
    }

    @Override
    public Optional<IntegerRange> intersection(IntegerRange other) {
        if (this.intersects(other)) {
            if (this.containsValue(other.min()) && !this.containsValue(other.max())) {
                return Optional.of(new IntegerRange(other.min(), max));
            } else if (!this.containsValue(other.min()) && this.containsValue(other.max())) {
                return Optional.of(new IntegerRange(min, other.max()));
            } else if (this.containsValue(other.min()) && this.containsValue(other.max())) {
                return Optional.of(other);
            } else if (other.containsValue(this.min()) && other.containsValue(this.max())) {
                return Optional.of(this);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<IntegerRange> difference(IntegerRange other) {
        if (this.equals(other)) {
            return List.of();
        }
        if (this.intersects(other)) {
            if (this.containsValue(other.min()) && !this.containsValue(other.max())) {
                return List.of(new IntegerRange(min, other.min()));
            } else if (!this.containsValue(other.min()) && this.containsValue(other.max())) {
                return List.of(new IntegerRange(other.max(), max));
            } else if (this.containsValue(other.min()) && this.containsValue(other.max())) {
                return List.of(new IntegerRange(min, other.min()), new IntegerRange(other.max(), max));
            } else if (other.containsValue(this.min()) && other.containsValue(this.max())) {
                return List.of();
            }
        }
        return List.of(this);
    }
    @Override
    public List<IntegerRange> splitAt(Integer limit) {
        if (containsValue(limit)) {
            return List.of(new IntegerRange(this.min(), limit), new IntegerRange(limit, this.max()));
        } else {
            return List.of(this);
        }
    }
//    @Override
//    public boolean isSingletonRange() {

//        return IntStream.range(min, max).count() == 1;

//    }



//    @Override
//    public boolean intersects(IntegerRange other) {
//        return min <= other.min() && other.min() < max ||
//                min < other.max() && other.max() < max ||
//                other.min() <= min && min < other.max() ||
//                other.min() < max && max < other.max();
//    }

//    @Override
//    public Stream<Integer> stream() {
//        return IntStream.range(min, max).boxed();
//    }
}
