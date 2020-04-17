package ro.mira.stad.gesint.statistics.repository;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import ro.mira.stad.gesint.statistics.domain.timeseries.DataPoint;
import ro.mira.stad.gesint.statistics.domain.timeseries.DataPointId;
import ro.mira.stad.gesint.statistics.domain.timeseries.ItemMetric;
import ro.mira.stad.gesint.statistics.domain.timeseries.StatisticMetric;

/**
 * @author STAD
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class DataPointRepositoryTest {

	@Autowired
	private DataPointRepository repository;

	@Test
	public void shouldRewriteDataPointWithinADay() {

		final BigDecimal earlyAmount = new BigDecimal(100);
		final BigDecimal lateAmount = new BigDecimal(200);

		final DataPointId pointId = new DataPointId("test-account", new Date(0));

		final DataPoint earlier = new DataPoint();
		earlier.setId(pointId);
		earlier.setStatistics(ImmutableMap.of(StatisticMetric.SAVING_AMOUNT, earlyAmount));

		repository.save(earlier);

		final DataPoint later = new DataPoint();
		later.setId(pointId);
		later.setStatistics(ImmutableMap.of(StatisticMetric.SAVING_AMOUNT, lateAmount));

		repository.save(later);

		final List<DataPoint> points = repository.findByIdAccount(pointId.getAccount());

		assertEquals(1, points.size());
		assertEquals(lateAmount, points.get(0).getStatistics().get(StatisticMetric.SAVING_AMOUNT));
	}

	@Test
	public void shouldSaveDataPoint() {

		final ItemMetric salary = new ItemMetric("salary", new BigDecimal(20_000));

		final ItemMetric grocery = new ItemMetric("grocery", new BigDecimal(1_000));
		final ItemMetric vacation = new ItemMetric("vacation", new BigDecimal(2_000));

		final DataPointId pointId = new DataPointId("test-account", new Date(0));

		final DataPoint point = new DataPoint();
		point.setId(pointId);
		point.setIncomes(Sets.newHashSet(salary));
		point.setExpenses(Sets.newHashSet(grocery, vacation));
		point.setStatistics(
				ImmutableMap.of(StatisticMetric.SAVING_AMOUNT, new BigDecimal(400_000), StatisticMetric.INCOMES_AMOUNT,
						new BigDecimal(20_000), StatisticMetric.EXPENSES_AMOUNT, new BigDecimal(3_000)));

		repository.save(point);

		final List<DataPoint> points = repository.findByIdAccount(pointId.getAccount());
		assertEquals(1, points.size());
		assertEquals(pointId.getDate(), points.get(0).getId().getDate());
		assertEquals(point.getStatistics().size(), points.get(0).getStatistics().size());
		assertEquals(point.getIncomes().size(), points.get(0).getIncomes().size());
		assertEquals(point.getExpenses().size(), points.get(0).getExpenses().size());
	}
}
