package ir.mojir.document_manager.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import ir.mojir.document_manager.entities.Document;

public interface DocumentRepo extends CrudRepository<Document, Long> {
	Page<Document> findAllByFileNameContaining(String fileName, Pageable pageable);
}
