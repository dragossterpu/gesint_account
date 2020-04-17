package ro.mira.stad.gesint.auth.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ro.mira.stad.gesint.auth.domain.User;
import ro.mira.stad.gesint.auth.service.UserService;

/**
 * @author STAD
 */
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PreAuthorize("#oauth2.hasScope('server')")
	@RequestMapping(method = RequestMethod.POST)
	public void createUser(@Valid @RequestBody final User user) {
		userService.create(user);
	}

	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public Principal getUser(final Principal principal) {
		return principal;
	}
}
