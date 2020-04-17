package ro.mira.stad.gesint.statistics.service;

import java.util.List;

import ro.mira.stad.gesint.statistics.domain.Account;
import ro.mira.stad.gesint.statistics.domain.timeseries.DataPoint;

/**
 * @author STAD
 */

public interface StatisticsService {

	/**
	 * Finds account by given name
	 *
	 * @param accountName
	 * @return found account
	 */
	List<DataPoint> findByAccountName(String accountName);

	/**
	 * Converts given {@link Account} object to {@link DataPoint} with a set of significant statistic metrics.
	 *
	 * Compound {@link DataPoint#id} forces to rewrite the object for each account within a day.
	 *
	 * @param accountName
	 * @param account
	 */
	DataPoint save(String accountName, Account account);

}