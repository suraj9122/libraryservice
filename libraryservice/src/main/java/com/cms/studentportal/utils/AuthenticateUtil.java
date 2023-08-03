package com.cms.studentportal.utils;

import javax.servlet.http.HttpServletRequest;

import com.cms.studentportal.domain.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <h1>Authentication user information</h1>
 */
public class AuthenticateUtil {

	public static boolean isAuthenticate() {
		return getPrincipal() != null;
	}

	public static long getStudentId() {
		return getPrincipal().getStudentId();
	}

	public static HttpServletRequest getHttpServletRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}

	private static Account getPrincipal() {
		Authentication authentication = getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal != null && principal instanceof Account) {
				return (Account) principal;
			}
		}
		return null;
	}

	private static Authentication getAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth;
	}

}
