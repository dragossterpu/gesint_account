package ro.mira.stad.gesint.statistics.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ro.mira.stad.gesint.statistics.domain.timeseries.DataPoint;
import ro.mira.stad.gesint.statistics.domain.timeseries.DataPointId;

/**
 * @author STAD
 */

@Repository
public interface DataPointRepository extends CrudRepository<DataPoint, DataPointId> {

	List<DataPoint> findByIdAccount(String account);

}
