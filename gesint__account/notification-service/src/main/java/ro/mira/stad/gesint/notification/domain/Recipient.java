package ro.mira.stad.gesint.notification.domain;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

/**
 * @author STAD
 */
@SuppressWarnings("deprecation")
@Getter
@Setter
@Document(collection = "recipients")
public class Recipient {

	@Id
	private String accountName;

	@NotNull
	@Email
	private String email;

	@Valid
	private Map<NotificationType, NotificationSettings> scheduledNotifications;

	@Override
	public String toString() {
		return "Recipient{" + "accountName='" + accountName + '\'' + ", email='" + email + '\'' + '}';
	}
}
