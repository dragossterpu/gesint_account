package ro.mira.stad.gesint.statistics.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import ro.mira.stad.gesint.statistics.domain.Account;
import ro.mira.stad.gesint.statistics.domain.Currency;
import ro.mira.stad.gesint.statistics.domain.Item;
import ro.mira.stad.gesint.statistics.domain.Saving;
import ro.mira.stad.gesint.statistics.domain.TimePeriod;
import ro.mira.stad.gesint.statistics.domain.timeseries.DataPoint;
import ro.mira.stad.gesint.statistics.domain.timeseries.ItemMetric;
import ro.mira.stad.gesint.statistics.domain.timeseries.StatisticMetric;
import ro.mira.stad.gesint.statistics.repository.DataPointRepository;

/**
 * @author STAD
 */
public class StatisticsServiceImplTest {

	@InjectMocks
	private StatisticsServiceImpl statisticsService;

	@Mock
	private ExchangeRatesServiceImpl ratesService;

	@Mock
	private DataPointRepository repository;

	@Before
	public void setup() {
		initMocks(this);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailToFindDataPointWhenAccountNameIsEmpty() {
		statisticsService.findByAccountName("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailToFindDataPointWhenAccountNameIsNull() {
		statisticsService.findByAccountName(null);
	}

	@Test
	public void shouldFindDataPointListByAccountName() {
		final List<DataPoint> list = ImmutableList.of(new DataPoint());
		when(repository.findByIdAccount("test")).thenReturn(list);

		final List<DataPoint> result = statisticsService.findByAccountName("test");
		assertEquals(list, result);
	}

	@Test
	public void shouldSaveDataPoint() {

		/**
		 * Given
		 */

		final Item salary = new Item();
		salary.setTitle("Salary");
		salary.setAmount(new BigDecimal(9100));
		salary.setCurrency(Currency.USD);
		salary.setPeriod(TimePeriod.MONTH);

		final Item grocery = new Item();
		grocery.setTitle("Grocery");
		grocery.setAmount(new BigDecimal(500));
		grocery.setCurrency(Currency.RON);
		grocery.setPeriod(TimePeriod.DAY);

		final Item vacation = new Item();
		vacation.setTitle("Vacation");
		vacation.setAmount(new BigDecimal(3400));
		vacation.setCurrency(Currency.EUR);
		vacation.setPeriod(TimePeriod.YEAR);

		final Saving saving = new Saving();
		saving.setAmount(new BigDecimal(1000));
		saving.setCurrency(Currency.EUR);
		saving.setInterest(new BigDecimal(3.2));
		saving.setDeposit(true);
		saving.setCapitalization(false);

		final Account account = new Account();
		account.setIncomes(ImmutableList.of(salary));
		account.setExpenses(ImmutableList.of(grocery, vacation));
		account.setSaving(saving);

		final Map<Currency, BigDecimal> rates = ImmutableMap.of(Currency.EUR, new BigDecimal("0.8"), Currency.RON,
				new BigDecimal("80"), Currency.USD, BigDecimal.ONE);

		/**
		 * When
		 */

		when(ratesService.convert(any(Currency.class), any(Currency.class), any(BigDecimal.class))).then(
				i -> ((BigDecimal) i.getArgument(2)).divide(rates.get(i.getArgument(0)), 4, RoundingMode.HALF_UP));

		when(ratesService.getCurrentRates()).thenReturn(rates);

		when(repository.save(any(DataPoint.class))).then(returnsFirstArg());

		final DataPoint dataPoint = statisticsService.save("test", account);

		/**
		 * Then
		 */

		final BigDecimal expectedExpensesAmount = new BigDecimal("17.8861");
		final BigDecimal expectedIncomesAmount = new BigDecimal("298.9802");
		final BigDecimal expectedSavingAmount = new BigDecimal("1250");

		final BigDecimal expectedNormalizedSalaryAmount = new BigDecimal("298.9802");
		final BigDecimal expectedNormalizedVacationAmount = new BigDecimal("11.6361");
		final BigDecimal expectedNormalizedGroceryAmount = new BigDecimal("6.25");

		assertEquals(dataPoint.getId().getAccount(), "test");
		assertEquals(dataPoint.getId().getDate(),
				Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

		assertTrue(
				expectedExpensesAmount.compareTo(dataPoint.getStatistics().get(StatisticMetric.EXPENSES_AMOUNT)) == 0);
		assertTrue(expectedIncomesAmount.compareTo(dataPoint.getStatistics().get(StatisticMetric.INCOMES_AMOUNT)) == 0);
		assertTrue(expectedSavingAmount.compareTo(dataPoint.getStatistics().get(StatisticMetric.SAVING_AMOUNT)) == 0);

		final ItemMetric salaryItemMetric = dataPoint.getIncomes().stream()
				.filter(i -> i.getTitle().equals(salary.getTitle())).findFirst().get();

		final ItemMetric vacationItemMetric = dataPoint.getExpenses().stream()
				.filter(i -> i.getTitle().equals(vacation.getTitle())).findFirst().get();

		final ItemMetric groceryItemMetric = dataPoint.getExpenses().stream()
				.filter(i -> i.getTitle().equals(grocery.getTitle())).findFirst().get();

		assertTrue(expectedNormalizedSalaryAmount.compareTo(salaryItemMetric.getAmount()) == 0);
		assertTrue(expectedNormalizedVacationAmount.compareTo(vacationItemMetric.getAmount()) == 0);
		assertTrue(expectedNormalizedGroceryAmount.compareTo(groceryItemMetric.getAmount()) == 0);

		assertEquals(rates, dataPoint.getRates());

		verify(repository, times(1)).save(dataPoint);
	}
}