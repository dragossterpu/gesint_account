package ro.mira.stad.gesint.statistics.repository.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;

import ro.mira.stad.gesint.statistics.domain.timeseries.DataPointId;

/**
 * @author STAD
 */

@Component
public class DataPointIdReaderConverter implements Converter<DBObject, DataPointId> {

	@Override
	public DataPointId convert(final DBObject object) {

		final Date date = (Date) object.get("date");
		final String account = (String) object.get("account");

		return new DataPointId(account, date);
	}
}
