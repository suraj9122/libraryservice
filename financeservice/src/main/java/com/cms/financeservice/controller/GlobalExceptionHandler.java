package com.cms.financeservice.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandler {

	String error_redirect_url = "/error";


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



}
