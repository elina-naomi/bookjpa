package telran.ashkelon2020.book.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"name"})
@Entity
public class Author implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1712895226657668235L;
	
	@Id
	String name;
	LocalDate birthDate;

}
