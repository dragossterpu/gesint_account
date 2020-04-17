package ro.mira.stad.gesint.auth.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ro.mira.stad.gesint.auth.repository.UserRepository;

/**
 * @author STAD
 */
@Service
public class MongoUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		return repository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
	}
}
