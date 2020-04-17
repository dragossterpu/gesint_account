package ro.mira.stad.gesint.statistics.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author STAD
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true, value = { "date" })
public class ExchangeRatesContainer {

	private LocalDate date = LocalDate.now();

	private Currency base;

	private Map<String, BigDecimal> rates;

	@Override
	public String toString() {
		return "RateList{" + "date=" + date + ", base=" + base + ", rates=" + rates + '}';
	}
}
