package ro.mira.stad.gesint.auth.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import ro.mira.stad.gesint.auth.domain.User;

/**
 * @author STAD
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository repository;

	@Test
	public void shouldSaveAndFindUserByName() {

		final User user = new User();
		user.setUsername("name");
		user.setPassword("password");
		repository.save(user);

		final Optional<User> found = repository.findById(user.getUsername());
		assertTrue(found.isPresent());
		assertEquals(user.getUsername(), found.get().getUsername());
		assertEquals(user.getPassword(), found.get().getPassword());
	}
}
