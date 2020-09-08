package telran.ashkelon2020.book.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.ashkelon2020.book.dao.AuthorRepository;
import telran.ashkelon2020.book.dao.BookRepository;
import telran.ashkelon2020.book.dao.PublisherRepository;
import telran.ashkelon2020.book.dto.AuthorDto;
import telran.ashkelon2020.book.dto.BookDto;
import telran.ashkelon2020.book.dto.exceptions.EntityNotFoundException;
import telran.ashkelon2020.book.model.Author;
import telran.ashkelon2020.book.model.Book;
import telran.ashkelon2020.book.model.Publisher;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	BookRepository bookRepository;
	@Autowired
	AuthorRepository authorRepository;
	@Autowired
	PublisherRepository publisherRepository; 
	@Autowired
	ModelMapper modelMapper;
	
	@Override
	@Transactional
	public boolean addBook(BookDto bookDto) {
		if(bookRepository.existsById(bookDto.getIsbn())) {
			return false;
		}
		//Publisher
		String publisherName = bookDto.getPublisher();
		Publisher publisher = publisherRepository.findById(publisherName)
				.orElse(publisherRepository.save(new Publisher(publisherName)));
		//Authors
		Set<Author> authors = bookDto.getAuthors().stream()
				.map(a -> authorRepository.findById(a.getName()).orElse(authorRepository.save(new Author(a.getName(), a.getBirthDate()))))
				.collect(Collectors.toSet());
		Book book = new Book(bookDto.getIsbn(), bookDto.getTitle(), authors, publisher);
		bookRepository.save(book);
		return true;
	}

	@Override
	public BookDto findBookByIsbn(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(()-> new EntityNotFoundException());
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto removeBook(String isbn) {
		Book book = bookRepository.findById(isbn).orElseThrow(()-> new EntityNotFoundException());
		bookRepository.delete(book);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional
	public BookDto updateBook(String isbn, String title) {
		Book book = bookRepository.findById(isbn).orElseThrow(()-> new EntityNotFoundException());
		book.setTitle(title);
		return modelMapper.map(book, BookDto.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<BookDto> findBooksByAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(()-> new EntityNotFoundException());
		return bookRepository.findByAuthorsContaining(author)
				.map(b -> modelMapper.map(b, BookDto.class)).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<BookDto> findBooksByPublisher(String publisherName) {
		Publisher publisher = publisherRepository.findById(publisherName).orElseThrow(()-> new EntityNotFoundException());
		return bookRepository.findByPublisher(publisher)
				.map(b -> modelMapper.map(b, BookDto.class)).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<AuthorDto> findBookAuthors(String isbn) {
		return bookRepository.findById(isbn).orElseThrow(()-> new EntityNotFoundException())
				.getAuthors().stream().map(a -> modelMapper.map(a, AuthorDto.class)).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<String> findPublishersByAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(()-> new EntityNotFoundException());
		return bookRepository.findByAuthorsContaining(author).map(b -> b.getPublisher().getPublisherName())
				.distinct().collect(Collectors.toList());
	}

	@Override
	@Transactional
	public AuthorDto removeAuthor(String authorName) {
		Author author = authorRepository.findById(authorName).orElseThrow(()-> new EntityNotFoundException());
		List<BookDto> booksByAuthor = (List<BookDto>) findBooksByAuthor(authorName);
		if(!booksByAuthor.isEmpty())
		{
			booksByAuthor.stream().map(b -> b.getIsbn()).forEach(isbn -> removeBook(isbn)); //OPTION A - delete both author and his books
//			throw new NotValidOperationException(); //OPTION B - forbidden if author has books
		}
		authorRepository.delete(author);
		return modelMapper.map(author, AuthorDto.class);
	}
}
