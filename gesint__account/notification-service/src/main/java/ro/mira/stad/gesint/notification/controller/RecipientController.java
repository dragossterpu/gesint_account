package ro.mira.stad.gesint.notification.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ro.mira.stad.gesint.notification.domain.Recipient;
import ro.mira.stad.gesint.notification.service.RecipientService;

/**
 * @author STAD
 */
@RestController
@RequestMapping("/recipients")
public class RecipientController {

	@Autowired
	private RecipientService recipientService;

	@RequestMapping(path = "/current", method = RequestMethod.GET)
	public Object getCurrentNotificationsSettings(final Principal principal) {
		return recipientService.findByAccountName(principal.getName());
	}

	@RequestMapping(path = "/current", method = RequestMethod.PUT)
	public Object saveCurrentNotificationsSettings(final Principal principal,
			@Valid @RequestBody final Recipient recipient) {
		return recipientService.save(principal.getName(), recipient);
	}
}
