package com.soa.jaxws;

import javax.jws.WebService;

@WebService
public interface HelloService {

	public Customer getMaxAgeCustomer(Customer c1,Customer c2);
}
