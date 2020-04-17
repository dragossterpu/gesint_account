package ro.mira.stad.gesint.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ro.mira.stad.gesint.auth.domain.User;

/**
 * @author STAD
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
