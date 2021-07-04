package ir.mojir.document_manager.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ir.mojir.document_manager.services.ParameterService;

@Component
public class InitializingBeanImpl implements InitializingBean{

	private static final Logger logger = LoggerFactory.getLogger(InitializingBeanImpl.class);
	
	@Autowired
	private ParameterService parameterService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Attempting to verify working directory...");
		
		String workingDir = parameterService.getWorkingDirectory();
		File workingDirFile = new File(workingDir);
		if(!workingDirFile.exists())
		{
			logger.info("Working directory ({}) does not exist. So creating it...", workingDir); 
			Files.createDirectories(Paths.get(workingDir));
		}
	}

}
