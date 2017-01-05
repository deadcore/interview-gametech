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

import java.util.Collection;
import java.util.stream.Stream;

import com.gametech.interview.dao.PriceDao;
import com.gametech.interview.provided.Price;

/**
 * @author Jack Liddiard
 */
public class InMemoryPriceDao implements PriceDao {
	
	private final Collection<Price> priceBuffer;

	public InMemoryPriceDao(final Collection<Price> priceBuffer) {
		this.priceBuffer = priceBuffer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<Price> findAll() {
		return priceBuffer.stream();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(final Price entity) {
		priceBuffer.add(entity);
	}
}
