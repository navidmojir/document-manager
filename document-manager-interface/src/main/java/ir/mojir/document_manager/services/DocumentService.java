package ir.mojir.document_manager.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ir.mojir.document_manager.entities.Document;
import ir.mojir.document_manager.exceptions.AccessDeniedException;
import ir.mojir.document_manager.exceptions.UploadFailedException;
import ir.mojir.document_manager.repositories.DocumentRepo;
import ir.mojir.spring_boot_commons.exceptions.EntityNotFoundException;
import ir.mojir.spring_boot_commons.exceptions.InternalErrorException;
import ir.mojir.spring_boot_commons.helpers.LocalThreadContext;

@Service
public class DocumentService {
	
	private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);
	
	@Autowired
	private DocumentRepo documentRepo;
	
	@Autowired
	private ParameterService parameterService;
	
	public Document upload(MultipartFile file)
	{
		try 
		{
			return upload(file.getOriginalFilename(), file.getBytes());
		} catch (Exception e) {
			throw new UploadFailedException("Failed to upload. Details: " + e.getMessage(), e);
		}
	}
	
	public Document upload(String fileName, byte[] file)
	{
		try 
		{
			Document document = new Document();
			document.setFileName(fileName.toLowerCase());			
			document.setFileExtension(getFileExtenstion(fileName));
			document.setCreatorUser(LocalThreadContext.getData().getUsername());
			Document savedDocument = documentRepo.save(document);
		
			
			Files.write(Paths.get(getFilePath(savedDocument.getId())), file);
			
			logger.info("Document with filename {} was uploaded successfully", fileName);
			
			return savedDocument;
		} catch (Exception e) {
			throw new UploadFailedException("Failed to upload. Details: " + e.getMessage(), e);
		}
	}

	private String getFileExtenstion(String fileName) {
		String[] splitted = fileName.split("[.]");
		if(splitted.length > 1)
			return splitted[splitted.length - 1];
		return "";
	}

	public Document download(long id) {
		try {
			Document document = findById(id);
			document.setBytes(Files.readAllBytes(Paths.get(getFilePath(id))));
			logger.trace("Document with id {} was downloaded.", id);
			return document;
		} catch (IOException e) {
			throw new InternalErrorException("An IO error occured while downloading file. Details: " + 
					e.getMessage(), e);
		}
	}
	
	public Document findById(long id)
	{
		Optional<Document> optDoc = documentRepo.findById(id);
		if(optDoc.isEmpty())
			throw new EntityNotFoundException(id, null);
		checkAccess(optDoc.get());
		return optDoc.get();
	}
	
	private String getFilePath(long id)
	{
		return String.format("%s/%d", parameterService.getWorkingDirectory(), id);
	}

//	public Page<Document> search(SearchDto<FileSearchFilters> req) {
//		
//		if(req.getFilters() == null)
//			req.setFilters(new FileSearchFilters());
//		
//		return documentRepo.findAllByFileNameContaining(req.getFilters().getFileName(),
//				RepositoryHelper.generatePageRequestWithSort(req.getPaging(), req.getSorting()));
//	}

	public void delete(long id) {
		documentRepo.delete(findById(id));
		logger.info("Document with id {} was deleted", id);
	}
	
	private void checkAccess(Document document) {
		String currentUser = LocalThreadContext.getData().getUsername();
		if(document.getCreatorUser().equalsIgnoreCase(currentUser))
			return;
		
		if(document.getGrantedUsers() != null && document.getGrantedUsers().contains(currentUser))
			return;
		
		throw new AccessDeniedException(String.format("User %s has not access to document with id %d", 
				currentUser, document.getId()));
	}
	
	public void grantAccessToUser(long documentId, String username) {
		Document document = findById(documentId);
		document.addGrantedUser(username);
		documentRepo.save(document);
		logger.info("Granted access to document {} for user {} requested by {}", 
				documentId,
				username,
				LocalThreadContext.getData().getUsername());
	}
}
