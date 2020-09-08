package telran.ashkelon2020.book.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"publisherName"})
@Entity
public class Publisher implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -397026447117022878L;
	
	@Id
	String publisherName;

}
