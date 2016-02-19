package com.soa.jaxws;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

public class SoapServer {

	public static void main(String[] args) {
		// Endpoint.publish("http://127.0.0.1:8080/helloService", new HelloServiceImpl());
		JaxWsServerFactoryBean soapFactoryBean = new JaxWsServerFactoryBean();
		soapFactoryBean.getInInterceptors().add(new LoggingInInterceptor());
		soapFactoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
		soapFactoryBean.setServiceClass(HelloServiceImpl.class);
		soapFactoryBean.setAddress("http://127.0.0.1:8080/helloService");
		soapFactoryBean.create();
	}
}
