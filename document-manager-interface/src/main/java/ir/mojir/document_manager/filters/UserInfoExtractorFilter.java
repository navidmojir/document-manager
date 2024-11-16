package ir.mojir.document_manager.filters;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import ir.mojir.spring_boot_commons.dtos.SimpleJwtToken;
import ir.mojir.spring_boot_commons.dtos.ThreadContextData;
import ir.mojir.spring_boot_commons.helpers.LocalThreadContext;
import ir.mojir.spring_boot_commons.helpers.SimpleJwtDecoder;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserInfoExtractorFilter implements Filter{
	
//	private static final Logger logger = LoggerFactory.getLogger(UserInfoExtractorFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		MDC.clear();
		
		HttpServletRequest httpServletReq = (HttpServletRequest)request;
		
//		KeycloakSecurityContext secutiryContext = (KeycloakSecurityContext)httpServletReq.getAttribute(
//				KeycloakSecurityContext.class.getName());
		
		SimpleJwtToken jwtToken = SimpleJwtDecoder.decode(httpServletReq.getHeader("Authorization"));
		
		String username = jwtToken.getPreferredUsername();
		ThreadContextData threadContextData = new ThreadContextData();
		threadContextData.setAccessToken(jwtToken.getTokenStr());
		threadContextData.setUsername(username);
        LocalThreadContext.setData(threadContextData);
        
//		logger.info("***********in my filter {}", LocalThreadContext.getData());
		
        MDC.put("username", username);
		chain.doFilter(request, response);
		
	}

}
