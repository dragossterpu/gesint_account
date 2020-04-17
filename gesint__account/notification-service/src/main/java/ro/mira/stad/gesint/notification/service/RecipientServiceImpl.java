package ro.mira.stad.gesint.notification.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ro.mira.stad.gesint.notification.domain.NotificationType;
import ro.mira.stad.gesint.notification.domain.Recipient;
import ro.mira.stad.gesint.notification.repository.RecipientRepository;

/**
 * @author STAD
 */
@Service
public class RecipientServiceImpl implements RecipientService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RecipientRepository repository;

	@SuppressWarnings("deprecation")
	@Override
	public Recipient findByAccountName(final String accountName) {
		Assert.hasLength(accountName);
		return repository.findByAccountName(accountName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Recipient> findReadyToNotify(final NotificationType type) {
		switch (type) {
		case BACKUP:
			return repository.findReadyForBackup();
		case REMIND:
			return repository.findReadyForRemind();
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void markNotified(final NotificationType type, final Recipient recipient) {
		recipient.getScheduledNotifications().get(type).setLastNotified(new Date());
		repository.save(recipient);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Recipient save(final String accountName, final Recipient recipient) {

		recipient.setAccountName(accountName);
		recipient.getScheduledNotifications().values().forEach(settings -> {
			if (settings.getLastNotified() == null) {
				settings.setLastNotified(new Date());
			}
		});

		repository.save(recipient);

		log.info("recipient {} settings has been updated", recipient);

		return recipient;
	}
}
