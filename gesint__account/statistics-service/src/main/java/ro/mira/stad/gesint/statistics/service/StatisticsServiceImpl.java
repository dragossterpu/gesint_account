package ro.mira.stad.gesint.statistics.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableMap;

import ro.mira.stad.gesint.statistics.domain.Account;
import ro.mira.stad.gesint.statistics.domain.Currency;
import ro.mira.stad.gesint.statistics.domain.Item;
import ro.mira.stad.gesint.statistics.domain.Saving;
import ro.mira.stad.gesint.statistics.domain.TimePeriod;
import ro.mira.stad.gesint.statistics.domain.timeseries.DataPoint;
import ro.mira.stad.gesint.statistics.domain.timeseries.DataPointId;
import ro.mira.stad.gesint.statistics.domain.timeseries.ItemMetric;
import ro.mira.stad.gesint.statistics.domain.timeseries.StatisticMetric;
import ro.mira.stad.gesint.statistics.repository.DataPointRepository;

/**
 * @author STAD
 */

@Service
public class StatisticsServiceImpl implements StatisticsService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DataPointRepository repository;

	@Autowired
	private ExchangeRatesService ratesService;

	/**
	 * Normalizes given item amount to {@link Currency#getBase()} currency with {@link TimePeriod#getBase()} time period
	 */
	private ItemMetric createItemMetric(final Item item) {

		final BigDecimal amount = ratesService.convert(item.getCurrency(), Currency.getBase(), item.getAmount())
				.divide(item.getPeriod().getBaseRatio(), 4, RoundingMode.HALF_UP);

		return new ItemMetric(item.getTitle(), amount);
	}

	private Map<StatisticMetric, BigDecimal> createStatisticMetrics(final Set<ItemMetric> incomes,
			final Set<ItemMetric> expenses, final Saving saving) {

		final BigDecimal savingAmount = ratesService.convert(saving.getCurrency(), Currency.getBase(),
				saving.getAmount());

		final BigDecimal expensesAmount = expenses.stream().map(ItemMetric::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		final BigDecimal incomesAmount = incomes.stream().map(ItemMetric::getAmount).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return ImmutableMap.of(StatisticMetric.EXPENSES_AMOUNT, expensesAmount, StatisticMetric.INCOMES_AMOUNT,
				incomesAmount, StatisticMetric.SAVING_AMOUNT, savingAmount);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<DataPoint> findByAccountName(final String accountName) {
		Assert.hasLength(accountName);
		return repository.findByIdAccount(accountName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataPoint save(final String accountName, final Account account) {

		final Instant instant = LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

		final DataPointId pointId = new DataPointId(accountName, Date.from(instant));

		final Set<ItemMetric> incomes = account.getIncomes().stream().map(this::createItemMetric)
				.collect(Collectors.toSet());

		final Set<ItemMetric> expenses = account.getExpenses().stream().map(this::createItemMetric)
				.collect(Collectors.toSet());

		final Map<StatisticMetric, BigDecimal> statistics = createStatisticMetrics(incomes, expenses,
				account.getSaving());

		final DataPoint dataPoint = new DataPoint();
		dataPoint.setId(pointId);
		dataPoint.setIncomes(incomes);
		dataPoint.setExpenses(expenses);
		dataPoint.setStatistics(statistics);
		dataPoint.setRates(ratesService.getCurrentRates());

		log.debug("new datapoint has been created: {}", pointId);

		return repository.save(dataPoint);
	}
}
