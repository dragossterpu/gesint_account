package ro.mira.stad.gesint.notification.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.ImmutableMap;

import ro.mira.stad.gesint.notification.domain.Frequency;
import ro.mira.stad.gesint.notification.domain.NotificationSettings;
import ro.mira.stad.gesint.notification.domain.NotificationType;
import ro.mira.stad.gesint.notification.domain.Recipient;

/**
 * @author STAD
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class RecipientRepositoryTest {

	@Autowired
	private RecipientRepository repository;

	@Test
	public void shouldFindByAccountName() {

		final NotificationSettings remind = new NotificationSettings();
		remind.setActive(true);
		remind.setFrequency(Frequency.WEEKLY);
		remind.setLastNotified(new Date(0));

		final NotificationSettings backup = new NotificationSettings();
		backup.setActive(false);
		backup.setFrequency(Frequency.MONTHLY);
		backup.setLastNotified(new Date());

		final Recipient recipient = new Recipient();
		recipient.setAccountName("test");
		recipient.setEmail("test@test.com");
		recipient.setScheduledNotifications(
				ImmutableMap.of(NotificationType.BACKUP, backup, NotificationType.REMIND, remind));

		repository.save(recipient);

		final Recipient found = repository.findByAccountName(recipient.getAccountName());
		assertEquals(recipient.getAccountName(), found.getAccountName());
		assertEquals(recipient.getEmail(), found.getEmail());

		assertEquals(recipient.getScheduledNotifications().get(NotificationType.BACKUP).getActive(),
				found.getScheduledNotifications().get(NotificationType.BACKUP).getActive());
		assertEquals(recipient.getScheduledNotifications().get(NotificationType.BACKUP).getFrequency(),
				found.getScheduledNotifications().get(NotificationType.BACKUP).getFrequency());
		assertEquals(recipient.getScheduledNotifications().get(NotificationType.BACKUP).getLastNotified(),
				found.getScheduledNotifications().get(NotificationType.BACKUP).getLastNotified());

		assertEquals(recipient.getScheduledNotifications().get(NotificationType.REMIND).getActive(),
				found.getScheduledNotifications().get(NotificationType.REMIND).getActive());
		assertEquals(recipient.getScheduledNotifications().get(NotificationType.REMIND).getFrequency(),
				found.getScheduledNotifications().get(NotificationType.REMIND).getFrequency());
		assertEquals(recipient.getScheduledNotifications().get(NotificationType.REMIND).getLastNotified(),
				found.getScheduledNotifications().get(NotificationType.REMIND).getLastNotified());
	}

	@Test
	public void shouldFindReadyForRemindWhenFrequencyIsWeeklyAndLastNotifiedWas8DaysAgo() {

		final NotificationSettings remind = new NotificationSettings();
		remind.setActive(true);
		remind.setFrequency(Frequency.WEEKLY);
		remind.setLastNotified(DateUtils.addDays(new Date(), -8));

		final Recipient recipient = new Recipient();
		recipient.setAccountName("test");
		recipient.setEmail("test@test.com");
		recipient.setScheduledNotifications(ImmutableMap.of(NotificationType.REMIND, remind));

		repository.save(recipient);

		final List<Recipient> found = repository.findReadyForRemind();
		assertFalse(found.isEmpty());
	}

	@Test
	public void shouldNotFindReadyForBackupWhenFrequencyIsQuaterly() {

		final NotificationSettings remind = new NotificationSettings();
		remind.setActive(true);
		remind.setFrequency(Frequency.QUARTERLY);
		remind.setLastNotified(DateUtils.addDays(new Date(), -91));

		final Recipient recipient = new Recipient();
		recipient.setAccountName("test");
		recipient.setEmail("test@test.com");
		recipient.setScheduledNotifications(ImmutableMap.of(NotificationType.BACKUP, remind));

		repository.save(recipient);

		final List<Recipient> found = repository.findReadyForBackup();
		assertFalse(found.isEmpty());
	}

	@Test
	public void shouldNotFindReadyForRemindWhenFrequencyIsWeeklyAndLastNotifiedWasYesterday() {

		final NotificationSettings remind = new NotificationSettings();
		remind.setActive(true);
		remind.setFrequency(Frequency.WEEKLY);
		remind.setLastNotified(DateUtils.addDays(new Date(), -1));

		final Recipient recipient = new Recipient();
		recipient.setAccountName("test");
		recipient.setEmail("test@test.com");
		recipient.setScheduledNotifications(ImmutableMap.of(NotificationType.REMIND, remind));

		repository.save(recipient);

		final List<Recipient> found = repository.findReadyForRemind();
		assertTrue(found.isEmpty());
	}

	@Test
	public void shouldNotFindReadyForRemindWhenNotificationIsNotActive() {

		final NotificationSettings remind = new NotificationSettings();
		remind.setActive(false);
		remind.setFrequency(Frequency.WEEKLY);
		remind.setLastNotified(DateUtils.addDays(new Date(), -30));

		final Recipient recipient = new Recipient();
		recipient.setAccountName("test");
		recipient.setEmail("test@test.com");
		recipient.setScheduledNotifications(ImmutableMap.of(NotificationType.REMIND, remind));

		repository.save(recipient);

		final List<Recipient> found = repository.findReadyForRemind();
		assertTrue(found.isEmpty());
	}
}