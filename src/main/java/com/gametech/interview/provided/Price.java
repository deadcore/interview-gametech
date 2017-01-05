package com.gametech.interview.provided;

import java.math.BigDecimal;

/**
 * This class represents a single Price for a given instrument.
 *
 * Copyright Gametech Limited, 2015.
 */
public class Price {
    private int instrumentId = -1;
    private long updateTime = -1;

    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal mid;


    private Price() { }

    public static Builder builder() {
        return new Builder();
    }

    public int getInstrumentId() {
        return instrumentId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public BigDecimal getMid() {
        return mid;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Price rawPrice = (Price) o;

        if (instrumentId != rawPrice.instrumentId) return false;
        if (updateTime != rawPrice.updateTime) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = instrumentId;
        result = 31 * result + (int) (updateTime ^ (updateTime >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "RawPrice{" +
                "instrumentId=" + instrumentId +
                ", updateTime=" + updateTime +
                ", bid=" + bid +
                ", ask=" + ask +
                ", mid=" + mid +
                '}';
    }

    public static class Builder {
        private final Price price = new Price();
        private boolean built = false;

        private Builder() {
        }

        public Price build() {
            if (built) {
                throw new IllegalArgumentException("Cannot rebuild a RawPrice");
            }

            if (price.updateTime == -1) {
                throw new IllegalArgumentException("updateTime must not be null");
            }
            if (price.instrumentId == -1) {
                throw new IllegalArgumentException("Market ID must be set");
            }
            built = true;
            return price;
        }

        public Builder withInstrumentId(final int instrumentId) {
            price.instrumentId = instrumentId;
            return this;
        }

        public Builder withUpdateTime(final long time) {
            price.updateTime = time;
            return this;
        }

        public Builder withAsk(final BigDecimal ask) {
            price.ask = ask;
            return this;
        }

        public Builder withBid(final BigDecimal bid) {
            price.bid = bid;
            return this;
        }

        public Builder withMid(final BigDecimal mid) {
            price.mid = mid;
            return this;
        }
    }
}
