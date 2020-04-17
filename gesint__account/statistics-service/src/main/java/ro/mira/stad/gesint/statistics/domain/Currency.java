package ro.mira.stad.gesint.statistics.domain;

/**
 * @author STAD
 */
public enum Currency {

	USD, EUR, RON;

	public static Currency getBase() {
		return USD;
	}
}
