package com.soa.jaxws;

import javax.jws.WebService;

@WebService
public class HelloServiceImpl implements HelloService {

	@Override
	public Customer getMaxAgeCustomer(Customer c1, Customer c2) {

		if (c1.getId() > c2.getId()) {
			return c1;
		} else {
			return c2;
		}
	}

}
