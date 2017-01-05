package com.gametech.interview.provided;

import java.math.BigDecimal;

/**
 * An open-high-low-close data point.
 *
 * Copyright Gametech Limited, 2015
 */
public class OHLC {
    private final int instrument;
    private final BigDecimal open;
    private final BigDecimal high;
    private final BigDecimal low;
    private final BigDecimal close;
    private final long timestamp;

    public OHLC(final int instrument, final BigDecimal open, final BigDecimal high, final BigDecimal low, final BigDecimal close, final long timestamp) {
        this.instrument = instrument;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.timestamp = timestamp;
    }

	public int getInstrument() {
        return instrument;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final OHLC ohlc = (OHLC) o;

        if (instrument != ohlc.instrument) return false;
        return timestamp == ohlc.timestamp;

    }

    @Override
    public int hashCode() {
        int result = instrument;
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "OHLC{" +
                "instrument=" + instrument +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", timestamp=" + timestamp +
                '}';
    }
}
