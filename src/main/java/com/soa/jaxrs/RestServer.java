package com.soa.jaxrs;

import org.apache.cxf.jaxrs.JAXRSBindingFactory;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;

public class RestServer {

	public static void main(String[] args){
		 JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		 sf.setResourceClasses(StudentServiceImpl.class);
		 sf.setBindingId(JAXRSBindingFactory.JAXRS_BINDING_ID);
		 sf.setAddress("http://localhost:9080/");
		 sf.create();
	}
}
