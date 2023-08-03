package com.cms.studentportal.thirdPartyApi.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cms.studentportal.thirdPartyApi.constant.ThirdPartyEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.studentportal.thirdPartyApi.constant.PaymentType;
import com.cms.studentportal.thirdPartyApi.service.ThirdPartyAPIServiceIfc;
import com.cms.studentportal.thirdPartyApi.util.HttpUtil;

@Service
public class ThirdPartyAPIServiceImpl implements ThirdPartyAPIServiceIfc {

	@Autowired
	private HttpUtil httpUtil;

	@Override
	public boolean isEligibleGraduation(long studentId) {
		//
		Map<String, Object> responseMap = httpUtil.get(ThirdPartyEndpoint.FINANCE_GRADUATION_CHECK + "/" + studentId);

		boolean hasOutstandingBalance = (boolean) responseMap.get("hasOutstandingBalance");

		return !hasOutstandingBalance;
	}

	@Override
	public void createFinanceServiceInvoice(long studentId, BigDecimal amount, PaymentType paymentType) {
		Map<String, Object> createFinanceServiceMap = new HashMap<String, Object>();

		createFinanceServiceMap.put("amount", amount);
		
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1); 
		Date dueDate = cal.getTime();
		DateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");
		String dueDateString = newDate.format(dueDate);
		createFinanceServiceMap.put("due_date", dueDateString);
		createFinanceServiceMap.put("payment_type", paymentType);

		Map<String, Object> accountMap = new HashMap<String, Object>();
		accountMap.put("student_id", String.valueOf(studentId));

		createFinanceServiceMap.put("account", accountMap);
		httpUtil.post(ThirdPartyEndpoint.CREATE_FINANCE_INVOICE, createFinanceServiceMap);
	}

	@Override
	public void createFinanceAccount(long studentId) {
		Map<String, Object> requestFinanceAccountCreateMap = new HashMap<String, Object>();
		requestFinanceAccountCreateMap.put("student_id", String.valueOf(studentId));
		httpUtil.post(ThirdPartyEndpoint.CREATE_FINANCE_ACCOUNT, requestFinanceAccountCreateMap);
	}

	@Override
	public void createLibraryAccount(long studentId) {
		Map<String, Object> requestLibraryAccountCreateMap = new HashMap<String, Object>();
		requestLibraryAccountCreateMap.put("student_id", String.valueOf(studentId));
		httpUtil.post(ThirdPartyEndpoint.CREATE_LIBRARY_ACCOUNT, requestLibraryAccountCreateMap);
	}
}
