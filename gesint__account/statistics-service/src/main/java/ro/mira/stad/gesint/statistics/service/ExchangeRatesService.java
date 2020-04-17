package ro.mira.stad.gesint.statistics.service;

import java.math.BigDecimal;
import java.util.Map;

import ro.mira.stad.gesint.statistics.domain.Currency;

/**
 * @author STAD
 */

public interface ExchangeRatesService {

	/**
	 * Converts given amount to specified currency
	 *
	 * @param from {@link Currency}
	 * @param to {@link Currency}
	 * @param amount to be converted
	 * @return converted amount
	 */
	BigDecimal convert(Currency from, Currency to, BigDecimal amount);

	/**
	 * Requests today's foreign exchange rates from a provider or reuses values from the last request (if they are still
	 * relevant)
	 *
	 * @return current date rates
	 */
	Map<Currency, BigDecimal> getCurrentRates();
}
