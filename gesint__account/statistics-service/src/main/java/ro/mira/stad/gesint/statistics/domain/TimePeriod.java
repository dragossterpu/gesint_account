package ro.mira.stad.gesint.statistics.domain;

import java.math.BigDecimal;

import lombok.Getter;

/**
 * @author STAD
 */
@Getter
public enum TimePeriod {

	YEAR(365.2425), QUARTER(91.3106), MONTH(30.4368), DAY(1), HOUR(0.0416);

	public static TimePeriod getBase() {
		return DAY;
	}

	private double baseRatio;

	TimePeriod(final double baseRatio) {
		this.baseRatio = baseRatio;
	}

	public BigDecimal getBaseRatio() {
		return new BigDecimal(baseRatio);
	}
}
