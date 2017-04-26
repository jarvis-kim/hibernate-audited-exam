package kr.co.jarvisk;

import kr.co.jarvisk.exam.audited.domain.Book;
import kr.co.jarvisk.exam.audited.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import sun.tools.jar.CommandLine;

@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication
public class HibernateAuditedExamApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HibernateAuditedExamApplication.class, args);
	}

	@Autowired
	private BookRepository bookRepository;

	@Override
	public void run(String... strings) throws Exception {
		Book book1 = Book.builder()
				.title("book-1")
				.content("book1-content")
				.build();

		Book book2 = Book.builder()
				.title("book-2")
				.content("book2-content")
				.build();

		bookRepository.save(book1);
		bookRepository.save(book2);

		book1.setContent(book1.getContent() + "(content-updated)");
		bookRepository.save(book1);
	}
}
