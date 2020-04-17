package ro.mira.stad.gesint.notification.repository.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ro.mira.stad.gesint.notification.domain.Frequency;

/**
 * @author STAD
 */
@Component
public class FrequencyWriterConverter implements Converter<Frequency, Integer> {

	@Override
	public Integer convert(final Frequency frequency) {
		return frequency.getDays();
	}
}
