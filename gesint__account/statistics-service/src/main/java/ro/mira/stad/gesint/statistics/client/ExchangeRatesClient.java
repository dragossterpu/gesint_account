package ro.mira.stad.gesint.statistics.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ro.mira.stad.gesint.statistics.domain.Currency;
import ro.mira.stad.gesint.statistics.domain.ExchangeRatesContainer;

/**
 * @author STAD
 */
@FeignClient(url = "${rates.url}", name = "rates-client", fallback = ExchangeRatesClientFallback.class)
public interface ExchangeRatesClient {

	@RequestMapping(method = RequestMethod.GET, value = "/latest")
	ExchangeRatesContainer getRates(@RequestParam("base") Currency base);

}
