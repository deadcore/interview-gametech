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
package com.gametech.interview.service.impl;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.groupingBy;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.lang3.Validate;

import com.gametech.interview.provided.OHLC;
import com.gametech.interview.provided.Price;
import com.gametech.interview.service.PriceHistorySource;

/**
 * The default implamentation of {@link PriceHistorySource} which takes a supplier of
 * prices.
 *
 * @author Jack Liddiard
 */
public class DefaultPriceHistory implements PriceHistorySource {

	private final Supplier<Stream<Price>> priceDao;
	
	/**
	 * 
	 */
	public DefaultPriceHistory(final Supplier<Stream<Price>> priceDao) {
		this.priceDao = Validate.notNull(priceDao, "Price dao must not be null");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OHLC> getPriceHistory(final int intervalCount) {
		Validate.isTrue(intervalCount >= 0, "Interval count must be a positive integer greater than 0");
		Validate.isTrue(intervalCount < 600, "Interval count must not be greater than 600");

		final List<OHLC> returnBuffer = new LinkedList<>();
		
		priceDao.get().collect(groupingBy(i -> i.getUpdateTime() / intervalCount)).forEach((k, price) -> {
			price.sort(comparingLong(Price::getUpdateTime));
			
			final Iterator<Price> it = price.iterator();
			
			// Our collection is empty so skip
			if (it.hasNext()) {
				final Price firstElement = it.next();
				
				int instrument = firstElement.getInstrumentId();
				final BigDecimal open = firstElement.getMid();
				BigDecimal high = firstElement.getMid();
				BigDecimal low = firstElement.getMid();
				BigDecimal close = firstElement.getMid();
				long timestamp = firstElement.getUpdateTime();
				
				while (it.hasNext()) {
					final Price p = it.next();
					
					instrument = p.getInstrumentId();
					
					if (high.compareTo(p.getMid()) < 0) {
						high = p.getMid();
					}
					
					if (low.compareTo(p.getMid()) > 0) {
						low = p.getMid();
					}
					
					if (!it.hasNext()) {
						close = p.getMid();
					}
					
					timestamp += p.getUpdateTime();
				}
				
				returnBuffer.add(new OHLC(instrument, open, high, low, close, timestamp / price.size()));
			}
		});
		
		return returnBuffer;
	}
	
}
