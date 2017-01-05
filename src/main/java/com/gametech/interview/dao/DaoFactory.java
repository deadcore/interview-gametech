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
package com.gametech.interview.dao;

import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.BinaryOperator;

import com.gametech.interview.dao.memory.InMemoryPriceDao;

/**
 * A redumentary dao factory used to retrieve singletons of the dao's.
 * @author Jack Liddiard
 */
public class DaoFactory {
	
	private static final Collection<Object> daos = new Vector<>();
	
	static {
		// Initialise some signltons
		daos.add(new InMemoryPriceDao(new LinkedTransferQueue<>()));
	}
	
	/**
	 * Gets an instance of the {@link Class} dao or throws an exception if one does not exist
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T getInstance(final Class<T> clazz) {
		return (T) daos.stream().filter(i -> clazz.isAssignableFrom(i.getClass())).reduce(DaoFactory::throwingMerger)
			.orElseThrow(() -> new IllegalArgumentException("Dao ["+clazz.getCanonicalName()+"] has not been initialised yet"));
	}
	
    private static <T> BinaryOperator<T> throwingMerger(final Object o1, final Object o2) {
        return (u,v) -> { throw new IllegalStateException(String.format("Duplicate dao found for class %s", u.getClass())); };
    }
}