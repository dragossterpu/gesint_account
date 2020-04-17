package ro.mira.stad.gesint.notification.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * @author STAD
 */
@Getter
@Setter
public class NotificationSettings {

	@NotNull
	private Boolean active;

	@NotNull
	private Frequency frequency;

	private Date lastNotified;

}
