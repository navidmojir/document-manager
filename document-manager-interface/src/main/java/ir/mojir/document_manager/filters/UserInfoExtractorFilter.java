package ir.mojir.document_manager.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakSecurityContext;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import ir.mojir.spring_boot_commons.dtos.ThreadContextData;
import ir.mojir.spring_boot_commons.helpers.LocalThreadContext;

@Component
public class UserInfoExtractorFilter implements Filter{
	
//	private static final Logger logger = LoggerFactory.getLogger(UserInfoExtractorFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		MDC.clear();
		
		HttpServletRequest httpServletReq = (HttpServletRequest)request;
		
		KeycloakSecurityContext secutiryContext = (KeycloakSecurityContext)httpServletReq.getAttribute(
				KeycloakSecurityContext.class.getName());
		
		String username = secutiryContext.getToken().getPreferredUsername();
		ThreadContextData threadContextData = new ThreadContextData();
		threadContextData.setAccessToken(secutiryContext.getTokenString());
		threadContextData.setUsername(username);
        LocalThreadContext.setData(threadContextData);
        
//		logger.info("***********in my filter {}", LocalThreadContext.getData());
		
        MDC.put("username", username);
		chain.doFilter(request, response);
		
	}

}
