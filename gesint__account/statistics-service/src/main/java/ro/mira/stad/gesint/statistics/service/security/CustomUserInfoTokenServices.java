package ro.mira.stad.gesint.statistics.service.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * @author STAD
 */

public class CustomUserInfoTokenServices implements ResourceServerTokenServices {

	private static final String[] PRINCIPAL_KEYS = new String[] { "user", "username", "userid", "user_id", "login",
			"id", "name" };

	protected final Log logger = LogFactory.getLog(getClass());

	private final String userInfoEndpointUrl;

	private final String clientId;

	private OAuth2RestOperations restTemplate;

	private String tokenType = DefaultOAuth2AccessToken.BEARER_TYPE;

	private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();

	public CustomUserInfoTokenServices(final String userInfoEndpointUrl, final String clientId) {
		this.userInfoEndpointUrl = userInfoEndpointUrl;
		this.clientId = clientId;
	}

	private OAuth2Authentication extractAuthentication(final Map<String, Object> map) {
		final Object principal = getPrincipal(map);
		final OAuth2Request request = getRequest(map);
		final List<GrantedAuthority> authorities = this.authoritiesExtractor.extractAuthorities(map);
		final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, "N/A",
				authorities);
		token.setDetails(map);
		return new OAuth2Authentication(request, token);
	}

	@SuppressWarnings({ "unchecked" })
	private Map<String, Object> getMap(final String path, final String accessToken) {
		this.logger.debug("Getting user info from: " + path);
		try {
			OAuth2RestOperations restTemplate = this.restTemplate;
			if (restTemplate == null) {
				final BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
				resource.setClientId(this.clientId);
				restTemplate = new OAuth2RestTemplate(resource);
			}
			final OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext().getAccessToken();
			if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
				final DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
				token.setTokenType(this.tokenType);
				restTemplate.getOAuth2ClientContext().setAccessToken(token);
			}
			return restTemplate.getForEntity(path, Map.class).getBody();
		}
		catch (final Exception ex) {
			this.logger.info("Could not fetch user details: " + ex.getClass() + ", " + ex.getMessage());
			return Collections.<String, Object> singletonMap("error", "Could not fetch user details");
		}
	}

	private Object getPrincipal(final Map<String, Object> map) {
		for (final String key : PRINCIPAL_KEYS) {
			if (map.containsKey(key)) {
				return map.get(key);
			}
		}
		return "unknown";
	}

	@SuppressWarnings({ "unchecked" })
	private OAuth2Request getRequest(final Map<String, Object> map) {
		final Map<String, Object> request = (Map<String, Object>) map.get("oauth2Request");

		final String clientId = (String) request.get("clientId");
		final Set<String> scope = new LinkedHashSet<>(
				request.containsKey("scope") ? (Collection<String>) request.get("scope")
						: Collections.<String> emptySet());

		return new OAuth2Request(null, clientId, null, true, new HashSet<>(scope), null, null, null, null);
	}

	@Override
	public OAuth2Authentication loadAuthentication(final String accessToken)
			throws AuthenticationException, InvalidTokenException {
		final Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);
		if (map.containsKey("error")) {
			this.logger.debug("userinfo returned error: " + map.get("error"));
			throw new InvalidTokenException(accessToken);
		}
		return extractAuthentication(map);
	}

	@Override
	public OAuth2AccessToken readAccessToken(final String accessToken) {
		throw new UnsupportedOperationException("Not supported: read access token");
	}

	public void setAuthoritiesExtractor(final AuthoritiesExtractor authoritiesExtractor) {
		this.authoritiesExtractor = authoritiesExtractor;
	}

	public void setRestTemplate(final OAuth2RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setTokenType(final String tokenType) {
		this.tokenType = tokenType;
	}
}
