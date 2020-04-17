package ro.mira.stad.gesint.notification.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ro.mira.stad.gesint.notification.client.AccountServiceClient;
import ro.mira.stad.gesint.notification.domain.NotificationType;
import ro.mira.stad.gesint.notification.domain.Recipient;

/**
 * @author STAD
 */
@Service
public class NotificationServiceImpl implements NotificationService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private AccountServiceClient client;

	@Autowired
	private RecipientService recipientService;

	@Autowired
	private EmailService emailService;

	@Override
	@Scheduled(cron = "${backup.cron}")
	public void sendBackupNotifications() {

		final NotificationType type = NotificationType.BACKUP;

		final List<Recipient> recipients = recipientService.findReadyToNotify(type);
		log.info("found {} recipients for backup notification", recipients.size());

		recipients.forEach(recipient -> CompletableFuture.runAsync(() -> {
			try {
				final String attachment = client.getAccount(recipient.getAccountName());
				emailService.send(type, recipient, attachment);
				recipientService.markNotified(type, recipient);
			}
			catch (final Throwable t) {
				log.error("an error during backup notification for {}", recipient, t);
			}
		}));
	}

	@Override
	@Scheduled(cron = "${remind.cron}")
	public void sendRemindNotifications() {

		final NotificationType type = NotificationType.REMIND;

		final List<Recipient> recipients = recipientService.findReadyToNotify(type);
		log.info("found {} recipients for remind notification", recipients.size());

		recipients.forEach(recipient -> CompletableFuture.runAsync(() -> {
			try {
				emailService.send(type, recipient, null);
				recipientService.markNotified(type, recipient);
			}
			catch (final Throwable t) {
				log.error("an error during remind notification for {}", recipient, t);
			}
		}));
	}
}
