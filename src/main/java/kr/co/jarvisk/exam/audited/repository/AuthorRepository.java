package kr.co.jarvisk.exam.audited.repository;

import kr.co.jarvisk.exam.audited.domain.Author;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "/authors", path = "/authors")
public interface AuthorRepository extends PagingAndSortingRepository<Author, Integer> {
    
}
