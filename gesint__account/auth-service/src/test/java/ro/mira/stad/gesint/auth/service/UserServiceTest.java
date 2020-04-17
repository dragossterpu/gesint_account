package ro.mira.stad.gesint.auth.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import ro.mira.stad.gesint.auth.domain.User;
import ro.mira.stad.gesint.auth.repository.UserRepository;

/**
 * @author STAD
 */
public class UserServiceTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserRepository repository;

	@Before
	public void setup() {
		initMocks(this);
	}

	@Test
	public void shouldCreateUser() {

		final User user = new User();
		user.setUsername("name");
		user.setPassword("password");

		userService.create(user);
		verify(repository, times(1)).save(user);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenUserAlreadyExists() {

		final User user = new User();
		user.setUsername("name");
		user.setPassword("password");

		when(repository.findById(user.getUsername())).thenReturn(Optional.of(new User()));
		userService.create(user);
	}
}
