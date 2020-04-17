package ro.mira.stad.gesint.notification.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import ro.mira.stad.gesint.notification.domain.Frequency;
import ro.mira.stad.gesint.notification.domain.NotificationSettings;
import ro.mira.stad.gesint.notification.domain.NotificationType;
import ro.mira.stad.gesint.notification.domain.Recipient;
import ro.mira.stad.gesint.notification.repository.RecipientRepository;

/**
 * @author STAD
 */
public class RecipientServiceImplTest {

	@InjectMocks
	private RecipientServiceImpl recipientService;

	@Mock
	private RecipientRepository repository;

	@Before
	public void setup() {
		initMocks(this);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailToFindRecipientWhenAccountNameIsEmpty() {
		recipientService.findByAccountName("");
	}

	@Test
	public void shouldFindByAccountName() {
		final Recipient recipient = new Recipient();
		recipient.setAccountName("test");

		when(repository.findByAccountName(recipient.getAccountName())).thenReturn(recipient);
		final Recipient found = recipientService.findByAccountName(recipient.getAccountName());

		assertEquals(recipient, found);
	}

	@Test
	public void shouldFindReadyToNotifyWhenNotificationTypeIsBackup() {
		final List<Recipient> recipients = ImmutableList.of(new Recipient());
		when(repository.findReadyForBackup()).thenReturn(recipients);

		final List<Recipient> found = recipientService.findReadyToNotify(NotificationType.BACKUP);
		assertEquals(recipients, found);
	}

	@Test
	public void shouldFindReadyToNotifyWhenNotificationTypeIsRemind() {
		final List<Recipient> recipients = ImmutableList.of(new Recipient());
		when(repository.findReadyForRemind()).thenReturn(recipients);

		final List<Recipient> found = recipientService.findReadyToNotify(NotificationType.REMIND);
		assertEquals(recipients, found);
	}

	@Test
	public void shouldMarkAsNotified() {

		final NotificationSettings remind = new NotificationSettings();
		remind.setActive(true);
		remind.setFrequency(Frequency.WEEKLY);
		remind.setLastNotified(null);

		final Recipient recipient = new Recipient();
		recipient.setAccountName("test");
		recipient.setEmail("test@test.com");
		recipient.setScheduledNotifications(ImmutableMap.of(NotificationType.REMIND, remind));

		recipientService.markNotified(NotificationType.REMIND, recipient);
		assertNotNull(recipient.getScheduledNotifications().get(NotificationType.REMIND).getLastNotified());
		verify(repository).save(recipient);
	}

	@Test
	public void shouldSaveRecipient() {

		final NotificationSettings remind = new NotificationSettings();
		remind.setActive(true);
		remind.setFrequency(Frequency.WEEKLY);
		remind.setLastNotified(null);

		final NotificationSettings backup = new NotificationSettings();
		backup.setActive(false);
		backup.setFrequency(Frequency.MONTHLY);
		backup.setLastNotified(new Date());

		final Recipient recipient = new Recipient();
		recipient.setEmail("test@test.com");
		recipient.setScheduledNotifications(
				ImmutableMap.of(NotificationType.BACKUP, backup, NotificationType.REMIND, remind));

		final Recipient saved = recipientService.save("test", recipient);

		verify(repository).save(recipient);
		assertNotNull(saved.getScheduledNotifications().get(NotificationType.REMIND).getLastNotified());
		assertEquals("test", saved.getAccountName());
	}
}