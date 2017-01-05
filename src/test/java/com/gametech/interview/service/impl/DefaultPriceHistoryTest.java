package com.gametech.interview.service.impl;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.gametech.interview.provided.OHLC;
import com.gametech.interview.provided.Price;
import com.gametech.interview.provided.Price.Builder;

/**
 * A test suit surrounding {@link DefaultPriceHistory}.
 *
 *
 * @author Jack Liddiard
 */
public final class DefaultPriceHistoryTest {
	
	@Test
	public final void testGroupByIntervalGroupsByDesiredInterval() {
		
		final Collection<Price> buffer = new ArrayList<>();
		
		Builder builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(BigDecimal.ONE);
		
		buffer.add(builder.withUpdateTime(10).build());
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(BigDecimal.ONE);
		
		buffer.add(builder.withUpdateTime(11).build());
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(BigDecimal.ONE);
		
		buffer.add(builder.withUpdateTime(12).build());
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(BigDecimal.ONE);
		
		buffer.add(builder.withUpdateTime(20).build());
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(BigDecimal.ONE);
		
		buffer.add(builder.withUpdateTime(30).build());


		final DefaultPriceHistory hs = new DefaultPriceHistory(() -> buffer.stream());
		
		assertThat(hs.getPriceHistory(10), hasSize(3));
	}
	
	@Test
	public final void testHighAgregation() {
		
		final Collection<Price> buffer = new ArrayList<>();
		
		Builder builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(BigDecimal.ONE);
		
		buffer.add(builder.withUpdateTime(11).build());
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(BigDecimal.TEN);
		
		buffer.add(builder.withUpdateTime(12).build());
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(new BigDecimal(5));
		
		buffer.add(builder.withUpdateTime(12).build());
		

		final DefaultPriceHistory hs = new DefaultPriceHistory(() -> buffer.stream());
		
		final List<OHLC> result = hs.getPriceHistory(599);
		
		assertThat(result.get(0).getHigh(), comparesEqualTo(new BigDecimal(10)));
	}
	
	@Test
	public final void testLowAgregation() {
		
		final Collection<Price> buffer = new ArrayList<>();
		
		Builder builder = Price.builder();
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(new BigDecimal(5));
		
		buffer.add(builder.withUpdateTime(13).build());
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(new BigDecimal(7));
		
		buffer.add(builder.withUpdateTime(11).build());
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(BigDecimal.TEN);
		
		buffer.add(builder.withUpdateTime(12).build());
		
		final DefaultPriceHistory hs = new DefaultPriceHistory(() -> buffer.stream());
		
		final List<OHLC> result = hs.getPriceHistory(599);
		
		assertThat(result.get(0).getLow(), comparesEqualTo(new BigDecimal(5)));
	}
	
	@Test
	public final void testOpenIsStartOfRange() {
		
		final Collection<Price> buffer = new ArrayList<>();
		
		Builder builder = Price.builder();
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(new BigDecimal(5));
		
		buffer.add(builder.withUpdateTime(13).build());
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(new BigDecimal(7));
		
		buffer.add(builder.withUpdateTime(11).build());
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(BigDecimal.TEN);
		
		buffer.add(builder.withUpdateTime(12).build());
		
		final DefaultPriceHistory hs = new DefaultPriceHistory(() -> buffer.stream());
		
		final List<OHLC> result = hs.getPriceHistory(599);
		
		assertThat(result.get(0).getOpen(), comparesEqualTo(new BigDecimal(7)));
	}
	
	@Test
	public final void testCloseIsEmdOfRange() {
		
		final Collection<Price> buffer = new ArrayList<>();
		
		Builder builder = Price.builder();
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(new BigDecimal(5));
		
		buffer.add(builder.withUpdateTime(13).build());
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(new BigDecimal(7));
		
		buffer.add(builder.withUpdateTime(20).build());
		
		builder = Price.builder();
		builder.withAsk(BigDecimal.ONE);
		builder.withBid(BigDecimal.ONE);
		builder.withInstrumentId(1);
		builder.withMid(BigDecimal.TEN);
		
		buffer.add(builder.withUpdateTime(12).build());
		
		final DefaultPriceHistory hs = new DefaultPriceHistory(() -> buffer.stream());
		
		final List<OHLC> result = hs.getPriceHistory(599);
		
		assertThat(result.get(0).getClose(), comparesEqualTo(new BigDecimal(7)));
	}
	
}
