package ro.mira.stad.gesint.statistics.domain.timeseries;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author STAD
 */
@Getter
@Setter
public class ItemMetric {

	private String title;

	private BigDecimal amount;

	public ItemMetric(final String title, final BigDecimal amount) {
		this.title = title;
		this.amount = amount;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final ItemMetric that = (ItemMetric) o;

		return title.equalsIgnoreCase(that.title);

	}

	@Override
	public int hashCode() {
		return title.hashCode();
	}
}
