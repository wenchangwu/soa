package com.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.ldap.InitialLdapContext;

import org.junit.Test;

public class JMSClient {

	private String connectionFactoryName = "";
	@Test
	public void consume() throws Exception{
		ConnectionFactory connectionFactory;
		Connection connection = null;
		Session session = null;
		Destination destination;
		MessageConsumer consumer = null;
		Message message;
		boolean useTransaction = false;
		
		try{
			Context ctx = new InitialLdapContext();
			connectionFactory =
			(ConnectionFactory) ctx.lookup(connectionFactoryName);
			connection = connectionFactory.createConnection();
			connection.start(); 
			session = connection.createSession(useTransaction,
			Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue("test.queue");
			 consumer = session.createConsumer(destination);
			 message = (TextMessage) consumer.receive(1000);
			 System.out.println("Received message: " + message);
		}catch(Exception e){
			
		}finally{
			consumer.close();
			session.close();
			connection.close();
		}
	}
}
