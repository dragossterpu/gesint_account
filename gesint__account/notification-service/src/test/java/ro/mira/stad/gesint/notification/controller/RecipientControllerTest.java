package ro.mira.stad.gesint.notification.controller;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.sun.security.auth.UserPrincipal;

import ro.mira.stad.gesint.notification.domain.Frequency;
import ro.mira.stad.gesint.notification.domain.NotificationSettings;
import ro.mira.stad.gesint.notification.domain.NotificationType;
import ro.mira.stad.gesint.notification.domain.Recipient;
import ro.mira.stad.gesint.notification.service.RecipientService;

/**
 * @author STAD
 */
@SuppressWarnings("restriction")
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipientControllerTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	@InjectMocks
	private RecipientController recipientController;

	@Mock
	private RecipientService recipientService;

	private MockMvc mockMvc;

	private Recipient getStubRecipient() {

		final NotificationSettings remind = new NotificationSettings();
		remind.setActive(true);
		remind.setFrequency(Frequency.WEEKLY);
		remind.setLastNotified(null);

		final NotificationSettings backup = new NotificationSettings();
		backup.setActive(false);
		backup.setFrequency(Frequency.MONTHLY);
		backup.setLastNotified(null);

		final Recipient recipient = new Recipient();
		recipient.setAccountName("test");
		recipient.setEmail("test@test.com");
		recipient.setScheduledNotifications(
				ImmutableMap.of(NotificationType.BACKUP, backup, NotificationType.REMIND, remind));

		return recipient;
	}

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(recipientController).build();
	}

	@Test
	public void shouldGetCurrentRecipientSettings() throws Exception {

		final Recipient recipient = getStubRecipient();
		when(recipientService.findByAccountName(recipient.getAccountName())).thenReturn(recipient);

		mockMvc.perform(get("/recipients/current").principal(new UserPrincipal(recipient.getAccountName())))
				.andExpect(jsonPath("$.accountName").value(recipient.getAccountName())).andExpect(status().isOk());
	}

	@Test
	public void shouldSaveCurrentRecipientSettings() throws Exception {

		final Recipient recipient = getStubRecipient();
		final String json = mapper.writeValueAsString(recipient);

		mockMvc.perform(put("/recipients/current").principal(new UserPrincipal(recipient.getAccountName()))
				.contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
	}
}