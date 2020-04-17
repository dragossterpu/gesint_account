package ro.mira.stad.gesint.statistics.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

/**
 * @author STAD
 */
@Document(collection = "accounts")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter

public class Account {

	@Valid
	@NotNull
	private List<Item> incomes;

	@Valid
	@NotNull
	private List<Item> expenses;

	@Valid
	@NotNull
	private Saving saving;

}
