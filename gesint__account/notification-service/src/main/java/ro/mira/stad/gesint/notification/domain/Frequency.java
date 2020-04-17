package ro.mira.stad.gesint.notification.domain;

import java.util.stream.Stream;

/**
 * @author STAD
 */
public enum Frequency {

	WEEKLY(7), MONTHLY(30), QUARTERLY(90);

	public static Frequency withDays(final int days) {
		return Stream.of(Frequency.values()).filter(f -> f.getDays() == days).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	private int days;

	Frequency(final int days) {
		this.days = days;
	}

	public int getDays() {
		return days;
	}
}
