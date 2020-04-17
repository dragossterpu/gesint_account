package ro.mira.stad.gesint.notification.service;

import java.util.List;

import ro.mira.stad.gesint.notification.domain.NotificationType;
import ro.mira.stad.gesint.notification.domain.Recipient;

/**
 * @author STAD
 */
public interface RecipientService {

	/**
	 * Finds recipient by account name
	 *
	 * @param accountName
	 * @return recipient
	 */
	Recipient findByAccountName(String accountName);

	/**
	 * Finds recipients, which are ready to be notified at the moment
	 *
	 * @param type
	 * @return recipients to notify
	 */
	List<Recipient> findReadyToNotify(NotificationType type);

	/**
	 * Updates {@link NotificationType} {@code lastNotified} property with current date for given recipient.
	 *
	 * @param type
	 * @param recipient
	 */
	void markNotified(NotificationType type, Recipient recipient);

	/**
	 * Creates or updates recipient settings
	 *
	 * @param accountName
	 * @param recipient
	 * @return updated recipient
	 */
	Recipient save(String accountName, Recipient recipient);
}
