package ro.mira.stad.gesint.notification.domain;

import lombok.Getter;

/**
 * @author STAD
 */
@Getter

public enum NotificationType {

	BACKUP("backup.email.subject", "backup.email.text", "backup.email.attachment"), REMIND("remind.email.subject",
			"remind.email.text", null);

	private String subject;

	private String text;

	private String attachment;

	NotificationType(final String subject, final String text, final String attachment) {
		this.subject = subject;
		this.text = text;
		this.attachment = attachment;
	}

}
