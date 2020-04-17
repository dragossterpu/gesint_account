package ro.mira.stad.gesint.auth.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ro.mira.stad.gesint.auth.domain.User;
import ro.mira.stad.gesint.auth.repository.UserRepository;

/**
 * @author STAD
 */
@Service
public class UserServiceImpl implements UserService {

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository repository;

	@Override
	public void create(final User user) {

		final Optional<User> existing = repository.findById(user.getUsername());
		existing.ifPresent(it -> {
			throw new IllegalArgumentException("user already exists: " + it.getUsername());
		});

		final String hash = encoder.encode(user.getPassword());
		user.setPassword(hash);

		repository.save(user);

		log.info("new user has been created: {}", user.getUsername());
	}
}
