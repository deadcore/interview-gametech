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
package com.gametech.interview;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.gametech.interview.service.PriceHistorySource;
import com.gametech.interview.service.PriceHistorySourceFactory;
import com.gametech.interview.service.PriceHistorySourceFactory.PriceHistorySourceFactoryContext;

/**
 * Application entry
 *
 * @author Jack Liddiard
 */
public class Application {
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS dd/MM/yyyy");
	
	public static final void main(final String... args) {
		
		final PriceHistorySource source = PriceHistorySourceFactory.getInstance(new PriceHistorySourceFactoryContext() {
			@Override
			public int numberOfPriceGenerators() {
				return 5;
			}
		});
		
		while (true) {
			System.out.println("******** New Results **********");
			source.getPriceHistory(100).forEach(i -> {
				System.out.println("Time: " + formatter.format(Instant.ofEpochMilli(i.getTimestamp()).atZone(ZoneId.systemDefault())));
				System.out.println("Instrument Id " + i.getInstrument());
				System.out.println("Close " + i.getClose());
				System.out.println("Open " + i.getOpen());
				System.out.println("High " + i.getHigh());
				System.out.println("Low " + i.getLow());
				System.out.println("Open " + i.getOpen());
			});
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				throw new RuntimeException("Error in catch", e);
			}
		}
		
	}
	
}
