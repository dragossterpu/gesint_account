package ro.mira.stad.gesint.notification.repository.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ro.mira.stad.gesint.notification.domain.Frequency;

/**
 * @author STAD
 */
@Component
public class FrequencyReaderConverter implements Converter<Integer, Frequency> {

	@Override
	public Frequency convert(final Integer days) {
		return Frequency.withDays(days);
	}
}
