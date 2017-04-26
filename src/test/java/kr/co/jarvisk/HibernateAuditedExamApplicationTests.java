package kr.co.jarvisk;

import kr.co.jarvisk.exam.audited.domain.Author;
import kr.co.jarvisk.exam.audited.domain.Book;
import kr.co.jarvisk.exam.audited.repository.BookRepository;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HibernateAuditedExamApplicationTests {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private BookRepository bookRepository;

	private Integer bookId;

	@Test
	public void contextLoads() {

	}

	private static final String BEFORE_CONTENT = "book1-content";

	private static final String AFTER_CONTENT = "book1-modified-content";

	@Before
	public void bookInsertAndModify() {
		Author author1 = Author.builder()
				.name("jarvis-kim")
				.build();

		Book book1 = Book.builder()
				.title("book1-title")
				.content(BEFORE_CONTENT)
				.authors(Arrays.asList(author1))
				.build();

		bookRepository.save(book1);

		this.bookId = book1.getId();

		book1.setContent(AFTER_CONTENT);

		bookRepository.save(book1);
	}

	@Test
	public void testAudited() {
		Iterable<Book> all = bookRepository.findAll();
		int lastBookId = StreamSupport.stream(all.spliterator(), false).map(Book::getId)
				.sorted(Integer::compareUnsigned).findFirst().get();

		AuditReader auditReader = AuditReaderFactory.get(entityManagerFactory.createEntityManager());
		List<Number> revisions = auditReader.getRevisions(Book.class, lastBookId);
		int lastReversionId = revisions.stream().findFirst().get().intValue();
		Book book = auditReader.find(Book.class, lastBookId, lastReversionId);

		assertEquals(BEFORE_CONTENT, book.getContent());
		
	}
}


