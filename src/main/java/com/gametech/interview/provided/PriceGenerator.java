package com.gametech.interview.provided;

import static java.lang.Math.sqrt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PriceGenerator {
	private static final double DT = 0.0000000317098;
	private static final double U_TIMES_DT = 0.1 * DT;

	private static final double SQRT_DT = sqrt(DT);

	private static final int VOLATILITY = 20;
	private static final BigDecimal START_PRICE = BigDecimal.valueOf(100);
	private static final BigDecimal PIP = BigDecimal.ONE.movePointLeft(3);

	private double lastValue;
	private final MersenneTwister twister  = new MersenneTwister();

	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	private final Queue<Price> buffer;
	
	private final int instrumentId;
	
	private static final AtomicInteger counter = new AtomicInteger(1);
	
	private PriceGenerator(final Queue<Price> queue) {
		lastValue = START_PRICE.doubleValue();
		this.buffer = queue;
		this.instrumentId = counter.getAndIncrement();
	}
	
	public static PriceGenerator startInstance(final Queue<Price> queue) {
		final PriceGenerator gen = new PriceGenerator(queue);
		gen.start();
		return gen;
	}
	
	public void start() {
		executor.scheduleAtFixedRate(this::sendNextPrice, 10, 10, TimeUnit.MILLISECONDS);
	}

	public void stop() {
		executor.shutdownNow();
	}

	double getNextValue() {
		final double epsilon = twister.nextGaussian();

		final double dW = SQRT_DT * epsilon;
		final double multiplier = (1 + (U_TIMES_DT) + (VOLATILITY * dW));
		return lastValue * multiplier;
	}

	void sendNextPrice() {
		final double nextValue = getNextValue();
		lastValue = nextValue;

		final BigDecimal val = new BigDecimal(nextValue).setScale(3, RoundingMode.HALF_UP);

		final Price price = Price.builder().withInstrumentId(instrumentId).withMid(val).withBid(val.subtract(PIP)).withAsk(val.add(PIP)).withUpdateTime(System.currentTimeMillis()).build();
		
		// May throw exception if buffer is full
		buffer.add(price);
	
	}
}
