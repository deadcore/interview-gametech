Your task is to create an implementation of PriceHistorySource. You should use Java 8 and any of the shiny new features
that it provides. Feel free to use anything from Guava (already added to the pom), but we're not interested in seeing
a Spring application.

Your implementation will need to provide price data for an OHLC (open-high-low-close) chart
(https://en.wikipedia.org/wiki/Open-high-low-close_chart). OHLC charts split price data into intervals - all prices
within an interval are merged into a single data point consisting of the price at the beginning of the interval (the
open price) and the end of the interval (the close price), as well as the highest and lowest price within the interval.
The Price class that you're working with provides mid, ask and bid prices. You're only expected to handle the mid price.

Let's say that we receive the following sequence of prices in a one second interval:
    * 100.098
    * 100.096
    * 100.101
    * 100.099
    * 100.098
    * 100.096
The open for this interval would then be 100.098, the high is 100.101, the low is 100.096 and the close is 100.096.

You'll need to provide a main method somewhere so that your solution can be run stand-alone, as well as unit tests that
you feel are appropriate. You'll also need to modify PriceGenerator so that the prices get into your implementation
somehow.

The price source generates the prices, and your implementation is expected to process <i>every</i> price as quickly
as possible. You're only expected to handle price history for a single instrument, but in reality this code would
need to handle thousands of instruments. Some instruments will be generating thousands of prices every second, so
performance is paramount. For simplicity, this test will run a price generator in the same process as the price
history provider. In reality this will absolutely not be the case: prices will come from multiple external sources
(probably over some kind of message queue). Your solution should take this into account. Don't let the message queue
back up!

Your implementation will be judged on performance, maintainability, documentation, testability and design, as well
as your use of algorithms, data structures and concurrency. Although implementations are provided for many of the
classes, you may change anything you want. This test is not an abstract exercise; it's an example of the kind of
thing we deal with every day.

Too easy? Handle multiple instruments and multiple time intervals. What about more than 10 minutes worth of history?
What happens if we don't receive any prices within an interval?
