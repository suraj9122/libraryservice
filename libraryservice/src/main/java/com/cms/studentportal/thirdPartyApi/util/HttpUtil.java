package com.cms.studentportal.thirdPartyApi.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This class will be used to call a api from this system
 */
@Component
public class HttpUtil {

	@Autowired
	@Qualifier("restTemplateAPICaller")
	private RestTemplate restTemplate;

	public void post(String endpoint, Object requestBody) {

		HttpEntity<Object> request = new HttpEntity<Object>(requestBody);
		Map<String, Object> response = restTemplate.postForObject(endpoint, request, Map.class);
	}

	public Map<String, Object> get(String endpoint) {

		ResponseEntity<Map> response = restTemplate.getForEntity(endpoint, Map.class);
		return response.getBody();
	}
}
