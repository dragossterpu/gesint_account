package ro.mira.stad.gesint.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import ro.mira.stad.gesint.auth.service.security.MongoUserDetailsService;

/**
 * @author STAD
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

	private final TokenStore tokenStore = new InMemoryTokenStore();

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Autowired
	private MongoUserDetailsService userDetailsService;

	@Autowired
	private Environment env;

	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager)
				.userDetailsService(userDetailsService);
	}

	@Override
	public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
				.passwordEncoder(NoOpPasswordEncoder.getInstance());
	}

	@Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {

		// TODO persist clients details

		// @formatter:off
		clients.inMemory().withClient("browser").authorizedGrantTypes("refresh_token", "password").scopes("ui").and()
				.withClient("account-service").secret(env.getProperty("ACCOUNT_SERVICE_PASSWORD"))
				.authorizedGrantTypes("client_credentials", "refresh_token").scopes("server").and()
				.withClient("statistics-service").secret(env.getProperty("STATISTICS_SERVICE_PASSWORD"))
				.authorizedGrantTypes("client_credentials", "refresh_token").scopes("server").and()
				.withClient("notification-service").secret(env.getProperty("NOTIFICATION_SERVICE_PASSWORD"))
				.authorizedGrantTypes("client_credentials", "refresh_token").scopes("server");
		// @formatter:on
	}

}
