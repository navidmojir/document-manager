package ir.mojir.document_manager.rest_controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ir.mojir.document_manager.dtos.FileSearchFilters;
import ir.mojir.document_manager.entities.Document;
import ir.mojir.document_manager.services.DocumentService;
import ir.mojir.spring_boot_commons.dtos.SearchDto;

@RestController
@CrossOrigin
public class DocumentController {

	@Autowired
	private DocumentService documentService;
	
	@PostMapping(path = "/upload", consumes = "multipart/form-data")
	public Document upload(@RequestParam("file") MultipartFile file)
	{
		return documentService.upload(file);
	}
	
	@PostMapping(path = "/upload", consumes = "application/octet-stream")
	public Document uploadBinary(@RequestBody byte[] file)
	{
		return documentService.upload("untitled", file);
	}
	
	@GetMapping(path = "/download/{id}", produces = "application/octet-stream")
	public ResponseEntity<Resource> download(@PathVariable long id)
	{
		Document document = documentService.download(id);
		ByteArrayResource resource = new ByteArrayResource(document.getBytes());
		String fileName = URLEncoder.encode(document.getFileName(), StandardCharsets.UTF_8);
		return ResponseEntity.ok().header("Content-Disposition", "filename=" + fileName)
				.body(resource);
	}
	
	@PostMapping("/search")
	public ResponseEntity<List<Document>> search(@RequestBody SearchDto<FileSearchFilters> req)
	{
		Page<Document> result = documentService.search(req);
		return ResponseEntity.ok().header("X-TOTAL-COUNT", String.valueOf(result.getTotalElements()))
				.body(result.getContent());
	}
	
	@GetMapping("/documents/{id}")
	public Document getDocument(@PathVariable long id)
	{
		return documentService.findById(id);
	}
}
