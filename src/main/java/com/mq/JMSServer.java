package com.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class JMSServer {

	private String connectionFactoryName = "tcp://127.0.0.1:61616";

	@Test
	public void produce() throws Exception {
		ConnectionFactory connectionFactory;
		Connection connection = null;
		Session session = null;
		Destination destionation;
		MessageProducer producer = null;
		Message message;
		boolean useTransaction = false;

		try {
			/*Context ctx = new InitialContext();
			connectionFactory = (ConnectionFactory) ctx
					.lookup(connectionFactoryName);*/
			connectionFactory  = new ActiveMQConnectionFactory(connectionFactoryName);
			connection = connectionFactory.createConnection("admin","password");
			connection.start();

			session = connection.createSession(useTransaction,
					Session.AUTO_ACKNOWLEDGE);
			destionation = session.createQueue("test.queue");
			producer = session.createProducer(destionation);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			message = session.createTextMessage("this is just a demo");

			producer.send(message);

		} catch (Exception e) {
			System.out.print(e.getStackTrace());
		} finally {
			producer.close();
			session.close();
			connection.close();
		}
	}

}
