/*
 * Copyright (c) 2016 igu.io. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of igu.io.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license
 * agreement you entered into with igu.io.
 *
 * IGU.IO MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. NJW SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 */
package com.gametech.interview.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TransferQueue;

import org.apache.commons.lang3.Validate;

import com.gametech.interview.dao.DaoFactory;
import com.gametech.interview.dao.PriceDao;
import com.gametech.interview.provided.Price;
import com.gametech.interview.provided.PriceGenerator;
import com.gametech.interview.service.impl.DefaultPriceHistory;

/**
 * A factory for generating {@link PriceHistorySource}.
 * 
 * @author Jack Liddiard
 */
public class PriceHistorySourceFactory {
	
	private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private static final Collection<Poller> pollers = new ArrayList<>();
	private static final Collection<PriceGenerator> priceGenerators = new ArrayList<>();

	/**
	 * Defines the context of the factory.
	 *
	 * @author Jack Liddiard
	 */
	public interface PriceHistorySourceFactoryContext {

		/**
		 * Specifies the number of concurrent price generators to use.
		 * @return
		 */
		int numberOfPriceGenerators();
	}
	
	/**
	 * Creates an instance of {@link PriceHistorySource}.
	 * @param ctx the {@link PriceHistorySourceFactoryContext}. Must not be {@code null}.
	 * @return an instance of {@link PriceHistorySource}. Never {@code null}.
	 */
	public static final PriceHistorySource getInstance(final PriceHistorySourceFactoryContext ctx) {
		Validate.notNull(ctx, "ctx must not be null");
		
		final PriceDao priceDao = DaoFactory.getInstance(PriceDao.class);
		final LinkedTransferQueue<Price> backBuffer = new LinkedTransferQueue<>();
		final Poller poller = new Poller(backBuffer, priceDao);
		
		for (int i = 0; i < ctx.numberOfPriceGenerators(); i++) {
			priceGenerators.add(PriceGenerator.startInstance(backBuffer));
		}
		
		pollers.add(poller);
		executor.execute(poller);

		final PriceHistorySource source = new DefaultPriceHistory(() -> priceDao.findAll());
		
		return source;
	}
	
	/**
	 * Stops all instances registered through this factory.
	 */
	public static final void stop() {
		priceGenerators.forEach(PriceGenerator::stop);
		pollers.forEach(Poller::shutdown);
		executor.shutdownNow();
	}
	
	/**
	 * An internal classed used to poll the backBuffer for an values. If it detects a value the value is persited to the underlying dao.
	 *
	 * @author Jack Liddiard
	 */
	private static final class Poller implements Runnable {
		
		private final TransferQueue<Price> backBuffer;
		private final PriceDao priceDao;
		private volatile boolean shutdown = false;
		
		public Poller(final TransferQueue<Price> backBuffer, final PriceDao priceDao) {
			this.backBuffer = backBuffer;
			this.priceDao = priceDao;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			try {
				while (!shutdown) {
					priceDao.save(backBuffer.take());
				}
			} catch (final InterruptedException e) {
				shutdown();
				throw new RuntimeException("Error while taking element", e);
			}
		}
		
		public final void shutdown() {
			shutdown = true;
		}
	}
	
}
