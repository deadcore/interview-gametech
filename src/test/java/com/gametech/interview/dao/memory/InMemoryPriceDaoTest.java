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
package com.gametech.interview.dao.memory;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import com.gametech.interview.dao.PriceDao;
import com.gametech.interview.provided.Price;

/**
 * A test suite surrounding {@link PriceDao}.
 * @author Jack Liddiard
 */
public class InMemoryPriceDaoTest {
	
	@Test
	public final void testSave() {
		final Collection<Price> myUnderlyingCollection = new ArrayList<>();
		
		final InMemoryPriceDao dao = new InMemoryPriceDao(myUnderlyingCollection);
		
		final Price entity = Price.builder()
		.withAsk(BigDecimal.ONE)
		.withBid(BigDecimal.ONE)
		.withInstrumentId(1)
		.withMid(BigDecimal.ONE)
		.withUpdateTime(20)
		.build();
		
		dao.save(entity);
		
		assertThat(myUnderlyingCollection, contains(entity));
	}
	
	@Test
	public final void testFind() {
		
		final Price entity = Price.builder()
		.withAsk(BigDecimal.ONE)
		.withBid(BigDecimal.ONE)
		.withInstrumentId(1)
		.withMid(BigDecimal.ONE)
		.withUpdateTime(20)
		.build();
		
		final Collection<Price> myUnderlyingCollection = Arrays.asList(entity);
		
		assertThat(new InMemoryPriceDao(myUnderlyingCollection).findAll().collect(toList()), contains(entity));
	}
	
}
