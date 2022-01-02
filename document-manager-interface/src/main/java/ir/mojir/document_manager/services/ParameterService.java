package ir.mojir.document_manager.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ParameterService {
	@Value("${workingDirectory:./files}")
	private String workingDirectory;

	public String getWorkingDirectory() {
		return workingDirectory;
	}
	
	
}
