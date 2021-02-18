package de.timmi6790.basemod.utilities;

public enum MinecraftTimeUnit {
    /**
     * Time unit representing one thousandth of a microsecond
     */
    NANOSECONDS {
        @Override
        protected long getCurrentValue() {
            return NANO_SECOND;
        }

        @Override
        public long convert(final long d, final MinecraftTimeUnit unit) {
            return unit.toNanos(d);
        }

        @Override
        protected int excessNanos(final long d, final long m) {
            return (int) (d - (m * MILLI_SECOND));
        }
    },

    /**
     * Time unit representing one thousandth of a millisecond
     */
    MICROSECONDS {
        @Override
        protected long getCurrentValue() {
            return MICRO_SECOND;
        }

        @Override
        public long convert(final long d, final MinecraftTimeUnit u) {
            return u.toMicros(d);
        }

        @Override
        protected int excessNanos(final long d, final long m) {
            return (int) ((d * MICRO_SECOND) - (m * MILLI_SECOND));
        }
    },

    /**
     * Time unit representing one thousandth of a second
     */
    MILLISECONDS {
        @Override
        protected long getCurrentValue() {
            return MILLI_SECOND;
        }

        @Override
        public long convert(final long d, final MinecraftTimeUnit u) {
            return u.toMillis(d);
        }
    },

    /**
     * Time unit representing 20 of a second
     */
    TICKS {
        @Override
        protected long getCurrentValue() {
            return TICK;
        }

        @Override
        public long convert(final long d, final MinecraftTimeUnit u) {
            return u.toTicks(d);
        }
    },

    /**
     * Time unit representing one second
     */
    SECONDS {
        @Override
        protected long getCurrentValue() {
            return SECOND;
        }

        @Override
        public long convert(final long d, final MinecraftTimeUnit u) {
            return u.toSeconds(d);
        }
    },

    /**
     * Time unit representing sixty seconds
     */
    MINUTES {
        @Override
        protected long getCurrentValue() {
            return MINUTE;
        }

        @Override
        public long convert(final long d, final MinecraftTimeUnit u) {
            return u.toMinutes(d);
        }
    },

    /**
     * Time unit representing sixty minutes
     */
    HOURS {
        @Override
        protected long getCurrentValue() {
            return HOUR;
        }

        @Override
        public long convert(final long d, final MinecraftTimeUnit u) {
            return u.toHours(d);
        }
    },

    /**
     * Time unit representing twenty four hours
     */
    DAYS {
        @Override
        protected long getCurrentValue() {
            return DAY;
        }

        @Override
        public long convert(final long d, final MinecraftTimeUnit u) {
            return u.toDays(d);
        }
    };

    // Handy constants for conversion methods
    protected static final long NANO_SECOND = 1L;
    protected static final long MICRO_SECOND = NANO_SECOND * 1000L;
    protected static final long MILLI_SECOND = MICRO_SECOND * 1000L;
    protected static final long TICK = MILLI_SECOND * 50L;
    protected static final long SECOND = MILLI_SECOND * 1000L;
    protected static final long MINUTE = SECOND * 60L;
    protected static final long HOUR = MINUTE * 60L;
    protected static final long DAY = HOUR * 24L;

    protected static final long MAX = Long.MAX_VALUE;

    /**
     * Scale duration by m, checking for overflow. This has a short name to make above code more readable.
     */
    static long x(final long duration, final long m, final long over) {
        if (duration > over) {
            return Long.MAX_VALUE;
        }
        if (duration < -over) {
            return Long.MIN_VALUE;
        }
        return duration * m;
    }

    /**
     * Converts the duration down
     */
    private long convertDown(final long duration, final long to) {
        return x(duration, this.getCurrentValue() / to, MAX / (this.getCurrentValue() / to));
    }

    /**
     * Converts the duration up
     */
    private long convertUp(final long duration, final long to) {
        return duration / (to / this.getCurrentValue());
    }

    /**
     * Finds the correct conversion method for the given time unit.
     */
    private long convertValue(final long duration, final long to) {
        if (this.getCurrentValue() == to) {
            return duration;
        } else if (this.getCurrentValue() > to) {
            return this.convertDown(duration, to);
        } else {
            return this.convertUp(duration, to);
        }
    }

    /**
     * Gets current value in nano seconds.
     *
     * @return the current value in nano seconds
     */
    protected abstract long getCurrentValue();

    /**
     * Converts the given time duration in the given unit to this unit. Conversions from finer to coarser granularities
     * truncate, so lose precision. For example, converting {@code 999} milliseconds to seconds results in {@code 0}.
     * Conversions from coarser to finer granularities with arguments that would numerically overflow saturate to {@code
     * Long.MIN_VALUE} if negative or {@code Long.MAX_VALUE} if positive.
     *
     * <p>For example, to convert 10 minutes to milliseconds, use:
     * {@code TimeUnit.MILLISECONDS.convert(10L, TimeUnit.MINUTES)}
     *
     * @param sourceDuration the time duration in the given {@code sourceUnit}
     * @param sourceUnit     the unit of the {@code sourceDuration} argument
     * @return the converted duration in this unit, or {@code Long.MIN_VALUE} if conversion would negatively overflow,
     * or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    public abstract long convert(final long sourceDuration, final MinecraftTimeUnit sourceUnit);

    /**
     * Equivalent to {@link #convert(long, MinecraftTimeUnit) NANOSECONDS.convert(duration, this)}.
     *
     * @param duration the duration
     * @return the converted duration, or {@code Long.MIN_VALUE} if conversion would negatively overflow, or {@code
     * Long.MAX_VALUE} if it would positively overflow.
     */
    public long toNanos(final long duration) {
        return this.convertValue(duration, NANO_SECOND);
    }

    /**
     * Equivalent to {@link #convert(long, MinecraftTimeUnit) MICROSECONDS.convert(duration, this)}.
     *
     * @param duration the duration
     * @return the converted duration, or {@code Long.MIN_VALUE} if conversion would negatively overflow, or {@code
     * Long.MAX_VALUE} if it would positively overflow.
     */
    public long toMicros(final long duration) {
        return this.convertValue(duration, MICRO_SECOND);
    }

    /**
     * Equivalent to {@link #convert(long, MinecraftTimeUnit) MILLISECONDS.convert(duration, this)}.
     *
     * @param duration the duration
     * @return the converted duration, or {@code Long.MIN_VALUE} if conversion would negatively overflow, or {@code
     * Long.MAX_VALUE} if it would positively overflow.
     */
    public long toMillis(final long duration) {
        return this.convertValue(duration, MILLI_SECOND);
    }

    /**
     * Equivalent to {@link #convert(long, MinecraftTimeUnit) TICKS.convert(duration, this)}.
     *
     * @param duration the duration
     * @return the converted duration, or {@code Long.MIN_VALUE} if conversion would negatively overflow, or {@code
     * Long.MAX_VALUE} if it would positively overflow.
     */
    public long toTicks(final long duration) {
        return this.convertValue(duration, TICK);
    }

    /**
     * Equivalent to {@link #convert(long, MinecraftTimeUnit) SECONDS.convert(duration, this)}.
     *
     * @param duration the duration
     * @return the converted duration, or {@code Long.MIN_VALUE} if conversion would negatively overflow, or {@code
     * Long.MAX_VALUE} if it would positively overflow.
     */
    public long toSeconds(final long duration) {
        return this.convertValue(duration, SECOND);
    }

    /**
     * Equivalent to {@link #convert(long, MinecraftTimeUnit) MINUTES.convert(duration, this)}.
     *
     * @param duration the duration
     * @return the converted duration, or {@code Long.MIN_VALUE} if conversion would negatively overflow, or {@code
     * Long.MAX_VALUE} if it would positively overflow.
     * @since 1.6
     */
    public long toMinutes(final long duration) {
        return this.convertValue(duration, MINUTE);
    }

    /**
     * Equivalent to {@link #convert(long, MinecraftTimeUnit) HOURS.convert(duration, this)}.
     *
     * @param duration the duration
     * @return the converted duration, or {@code Long.MIN_VALUE} if conversion would negatively overflow, or {@code
     * Long.MAX_VALUE} if it would positively overflow.
     * @since 1.6
     */
    public long toHours(final long duration) {
        return this.convertValue(duration, HOUR);
    }

    /**
     * Equivalent to {@link #convert(long, MinecraftTimeUnit) DAYS.convert(duration, this)}.
     *
     * @param duration the duration
     * @return the converted duration
     * @since 1.6
     */
    public long toDays(final long duration) {
        return this.convertValue(duration, DAY);
    }

    /**
     * Utility to compute the excess-nanosecond argument to wait, sleep, join.
     *
     * @param d the duration
     * @param m the number of milliseconds
     * @return the number of nanoseconds
     */
    protected int excessNanos(final long d, final long m) {
        return 0;
    }

    /**
     * Performs a {@link Thread#sleep(long, int) Thread.sleep} using this time unit. This is a convenience method that
     * converts time arguments into the form required by the {@code Thread.sleep} method.
     *
     * @param timeout the minimum time to sleep. If less than or equal to zero, do not sleep at all.
     * @throws InterruptedException if interrupted while sleeping
     */
    public void sleep(final long timeout) throws InterruptedException {
        if (timeout > 0) {
            final long ms = this.toMillis(timeout);
            final int ns = this.excessNanos(timeout, ms);
            Thread.sleep(ms, ns);
        }
    }
}
