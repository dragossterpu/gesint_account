package ro.mira.stad.gesint.statistics.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableMap;

import ro.mira.stad.gesint.statistics.client.ExchangeRatesClient;
import ro.mira.stad.gesint.statistics.domain.Currency;
import ro.mira.stad.gesint.statistics.domain.ExchangeRatesContainer;

/**
 * @author STAD
 */

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {

	private static final Logger log = LoggerFactory.getLogger(ExchangeRatesServiceImpl.class);

	private ExchangeRatesContainer container;

	@Autowired
	private ExchangeRatesClient client;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public BigDecimal convert(final Currency from, final Currency to, final BigDecimal amount) {

		Assert.notNull(amount);

		final Map<Currency, BigDecimal> rates = getCurrentRates();
		final BigDecimal ratio = rates.get(to).divide(rates.get(from), 4, RoundingMode.HALF_UP);

		return amount.multiply(ratio);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Currency, BigDecimal> getCurrentRates() {

		if (container == null || !container.getDate().equals(LocalDate.now())) {
			container = client.getRates(Currency.getBase());
			log.info("exchange rates has been updated: {}", container);
		}

		return ImmutableMap.of(Currency.EUR, container.getRates().get(Currency.EUR.name()), Currency.RON,
				container.getRates().get(Currency.RON.name()), Currency.USD, BigDecimal.ONE);
	}
}
