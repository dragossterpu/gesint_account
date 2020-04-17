package ro.mira.stad.gesint.notification.service;

import java.io.IOException;

import javax.mail.MessagingException;

import ro.mira.stad.gesint.notification.domain.NotificationType;
import ro.mira.stad.gesint.notification.domain.Recipient;

/**
 * @author STAD
 */
public interface EmailService {

	void send(NotificationType type, Recipient recipient, String attachment) throws MessagingException, IOException;

}
