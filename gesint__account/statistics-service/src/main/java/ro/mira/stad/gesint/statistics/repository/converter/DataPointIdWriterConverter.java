package ro.mira.stad.gesint.statistics.repository.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import ro.mira.stad.gesint.statistics.domain.timeseries.DataPointId;

/**
 * @author STAD
 */

@Component
public class DataPointIdWriterConverter implements Converter<DataPointId, DBObject> {

	private static final int FIELDS = 2;

	@Override
	public DBObject convert(final DataPointId id) {

		final DBObject object = new BasicDBObject(FIELDS);

		object.put("date", id.getDate());
		object.put("account", id.getAccount());

		return object;
	}
}
