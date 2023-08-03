package com.cms.studentportal.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cms.studentportal.exception.UserAlreadyEnrollIntoCourseException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cms.studentportal.utils.AuthenticateUtil;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	String error_redirect_url = "/error";

	@ExceptionHandler(MissingRequestCookieException.class)
	public RedirectView missingRequestCookie(MissingRequestCookieException exception, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) throws IOException {
		exception.printStackTrace();
		String redirectPath = "/";

		FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
		if (!AuthenticateUtil.isAuthenticate()) {
			outputFlashMap.put("error_msg", "Please authenticate before accessing this page.");
			redirectPath = error_redirect_url;

		}
		RedirectView rw = new RedirectView(redirectPath);
		return rw;
	}
	
	
	
	@ExceptionHandler(org.springframework.web.client.ResourceAccessException.class)
	public RedirectView missingRequestCookie(org.springframework.web.client.ResourceAccessException exception, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) throws IOException {
		exception.printStackTrace();
		RedirectView rw = new RedirectView(error_redirect_url);
		FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
		if (outputFlashMap != null){
			outputFlashMap.put("error_msg", "Internal Services are not connected yet. Please try again later!");
		}
		return rw;
	}

	@ExceptionHandler(Exception.class)
	public RedirectView throwable(Exception exception,HttpServletRequest request,  HttpServletResponse response, RedirectAttributes redirectAttributes)
			throws IOException {
		exception.printStackTrace();
		RedirectView rw = new RedirectView(error_redirect_url);
		FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
		if (outputFlashMap != null){
			outputFlashMap.put("error_msg", exception.getMessage());
		}
		return rw;
	}

	@ExceptionHandler(UserAlreadyEnrollIntoCourseException.class)
	public RedirectView  throwable(UserAlreadyEnrollIntoCourseException exception, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes)
			throws IOException {
		exception.printStackTrace();
		RedirectView rw = new RedirectView("/courses");
		FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
		if (outputFlashMap != null){
			outputFlashMap.put("error_msg", exception.getMessage());
		}
		return rw;
	}


}
