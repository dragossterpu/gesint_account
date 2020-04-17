package ro.mira.stad.gesint.statistics.client;

import java.util.Collections;

import org.springframework.stereotype.Component;

import ro.mira.stad.gesint.statistics.domain.Currency;
import ro.mira.stad.gesint.statistics.domain.ExchangeRatesContainer;

/**
 * @author STAD
 */
@Component
public class ExchangeRatesClientFallback implements ExchangeRatesClient {

	@Override
	public ExchangeRatesContainer getRates(final Currency base) {
		final ExchangeRatesContainer container = new ExchangeRatesContainer();
		container.setBase(Currency.getBase());
		container.setRates(Collections.emptyMap());
		return container;
	}
}
