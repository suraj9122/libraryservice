package com.cms.studentportal.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cms.studentportal.constant.Endpoint;
import com.cms.studentportal.domain.Account;
import com.cms.studentportal.exception.AuthenticationException;
import com.cms.studentportal.service.AccountServiceIfc;
import com.cms.studentportal.utils.AuthenticateUtil;;

@RestController
@RequestMapping(value = Endpoint.ROOT_API_V1)
public class AccountController {

	private AccountServiceIfc accountServiceIfc;

	AccountController(AccountServiceIfc accountServiceIfc) {
		this.accountServiceIfc = accountServiceIfc;
	}
	
	@PostMapping("/login")
	public @ResponseBody ResponseEntity<Account> loginApi(@RequestBody Account studentLogin) {

		if (!AuthenticateUtil.isAuthenticate()) {
			try {
				AuthenticateUtil.getHttpServletRequest().login(String.valueOf(studentLogin.getStudentId()), studentLogin.getPassword());
			} catch (ServletException e) {
				throw new AuthenticationException();
			}
		}

		Account student = new Account();
		student.setStudentId(AuthenticateUtil.getStudentId());

		student.add(linkTo(methodOn(AccountController.class).getStudent()).withRel("get_profile"));

		return ResponseEntity.status(HttpStatus.OK).body(student);
	}

	@GetMapping
	public @ResponseBody ResponseEntity<Account> getStudent() {
		Account student = accountServiceIfc.getAccountByStudentId(AuthenticateUtil.getStudentId());
		return ResponseEntity.status(HttpStatus.OK).body(student);
	}

	@PostMapping(Endpoint.ACCOUNT_CREATE_OR_GETS)
	public @ResponseBody ResponseEntity<Map> createFinanceAccount(@RequestBody Account account) {
		accountServiceIfc.createAccount(account);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/accounts/student/{student_id}")
	public @ResponseBody ResponseEntity<Map> isEligibleForGradution(@PathVariable("student_id") String studentId) {
		boolean hasOutstandingBalance =  false;//invoiceServiceIfc.hasOutstandingBalance(Long.valueOf(studentId));
		Map<String, Object> map= new HashMap();
		map.put("hasOutstandingBalance", hasOutstandingBalance);
		return ResponseEntity.status(HttpStatus.OK).body(map);
	}
}
