package ro.mira.stad.gesint.statistics.domain.timeseries;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @author STAD
 */
@Getter
@Setter
public class DataPointId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String account;

	private Date date;

	public DataPointId(final String account, final Date date) {
		this.account = account;
		this.date = date;
	}

	@Override
	public String toString() {
		return "DataPointId{" + "account='" + account + '\'' + ", date=" + date + '}';
	}
}
