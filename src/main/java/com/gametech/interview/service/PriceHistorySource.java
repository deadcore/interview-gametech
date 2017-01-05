package com.gametech.interview.service;

import java.util.List;

import com.gametech.interview.provided.OHLC;
import com.gametech.interview.provided.Price;
import com.gametech.interview.provided.PriceGenerator;

/**
 * This is where the work is. You need to implement this interface. The class needs to provide price data for an OHLC
 * (open-high-low-close) chart (https://en.wikipedia.org/wiki/Open-high-low-close_chart). OHLC charts split price
 * data into intervals - all prices within that interval are merged into a single data point consisting of the
 * price at the beginning of the interval (the open price) and the end of the interval (the close price), as well
 * as the highest and lowest price within the interval. The {@link Price} class that you're working with provides
 * mid, ask and bid prices. You're only expected to handle the mid price.
 * <p/>
 * Let's say that we receive the following sequence of prices in a one second interval:
 * <ul>
 *     <li>100.098</li>
 *     <li>100.096</li>
 *     <li>100.101</li>
 *     <li>100.099</li>
 *     <li>100.098</li>
 *     <li>100.096</li>
 * </ul>
 * The open for this interval would then be 100.098, the high is 100.101, the low is 100.096 and the close is 100.096.
 * <p/>
 * You'll need to provide a {@code main} method somewhere so that your solution can be run stand-alone, as well
 * as unit tests that you feel are appropriate. You'll also need to modify {@link PriceGenerator} so that the
 * prices get here somehow.
 * <p/>
 * The price source generates the prices, and your implementation is expected to process <i>every</i> price as quickly
 * as possible. You're only expected to handle price history for a single instrument, but in reality this code would
 * need to handle thousands of instruments. Some instruments will be generating thousands of prices every second, so
 * performance is paramount. For simplicity, this test will run a price generator in the same process as the price
 * history provider. In reality this will absolutely not be the case: prices will come from multiple external sources
 * (probably over some kind of message queue). Your solution should take this into account. Don't let the message queue
 * back up!
 * <p/>
 * Your implementation will be judged on performance, maintainability, documentation, testability and design, as well
 * as your use of algorithms, data structures and concurrency. Although implementations are provided for many of the
 * classes, you may change anything you want. This test is not an abstract exercise; it's an example of the kind of
 * thing we deal with every day.
 * <p/>
 * Too easy? Handle multiple instruments and multiple time intervals. What about more than 10 minutes worth of history?
 * What happens if we don't receive any prices within an interval?
 *
 * Copyright Gametech Limited, 2015
 */
public interface PriceHistorySource {
    /**
     * Retrieve the most recent price history for the instrument.
     * @throws IllegalArgumentException if intervalCount is <=0 or >600
     * @param intervalCount the number of intervals to retrieve
     * @param i instrument id.
     * @return the list of data. Never null, may be empty.
     */
    List<OHLC> getPriceHistory(int intervalCount);

}
