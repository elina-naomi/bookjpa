package telran.ashkelon2020.book.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class NotValidOperationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8021660502853434790L;

}
