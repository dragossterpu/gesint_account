package ro.mira.stad.gesint.statistics.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.google.common.collect.ImmutableMap;

import ro.mira.stad.gesint.statistics.client.ExchangeRatesClient;
import ro.mira.stad.gesint.statistics.domain.Currency;
import ro.mira.stad.gesint.statistics.domain.ExchangeRatesContainer;

/**
 * @author STAD
 */
public class ExchangeRatesServiceImplTest {

	@InjectMocks
	private ExchangeRatesServiceImpl ratesService;

	@Mock
	private ExchangeRatesClient client;

	@Before
	public void setup() {
		initMocks(this);
	}

	@Test
	public void shouldConvertCurrency() {

		final ExchangeRatesContainer container = new ExchangeRatesContainer();
		container.setRates(
				ImmutableMap.of(Currency.EUR.name(), new BigDecimal("0.8"), Currency.RON.name(), new BigDecimal("80")));

		when(client.getRates(Currency.getBase())).thenReturn(container);

		final BigDecimal amount = new BigDecimal(100);
		final BigDecimal expectedConvertionResult = new BigDecimal("1.25");

		final BigDecimal result = ratesService.convert(Currency.RON, Currency.USD, amount);

		assertTrue(expectedConvertionResult.compareTo(result) == 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailToConvertWhenAmountIsNull() {
		ratesService.convert(Currency.EUR, Currency.RON, null);
	}

	@Test
	public void shouldNotRequestRatesWhenTodaysContainerAlreadyExists() {

		final ExchangeRatesContainer container = new ExchangeRatesContainer();
		container.setRates(
				ImmutableMap.of(Currency.EUR.name(), new BigDecimal("0.8"), Currency.RON.name(), new BigDecimal("80")));

		when(client.getRates(Currency.getBase())).thenReturn(container);

		// initialize container
		ratesService.getCurrentRates();

		// use existing container
		ratesService.getCurrentRates();

		verify(client, times(1)).getRates(Currency.getBase());
	}

	@Test
	public void shouldReturnCurrentRatesWhenContainerIsEmptySoFar() {

		final ExchangeRatesContainer container = new ExchangeRatesContainer();
		container.setRates(
				ImmutableMap.of(Currency.EUR.name(), new BigDecimal("0.8"), Currency.RON.name(), new BigDecimal("80")));

		when(client.getRates(Currency.getBase())).thenReturn(container);

		final Map<Currency, BigDecimal> result = ratesService.getCurrentRates();
		verify(client, times(1)).getRates(Currency.getBase());

		assertEquals(container.getRates().get(Currency.EUR.name()), result.get(Currency.EUR));
		assertEquals(container.getRates().get(Currency.RON.name()), result.get(Currency.RON));
		assertEquals(BigDecimal.ONE, result.get(Currency.USD));
	}
}