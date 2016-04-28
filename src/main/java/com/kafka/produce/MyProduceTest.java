package com.kafka.produce;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import kafka.producer.KeyedMessage;

public class MyProduceTest {

	public static void main(String[] args){
		 Properties props = new Properties();
		 props.put("bootstrap.servers", "192.168.201.19:9092,192.168.201.19:9093");
		 props.put("acks", "all");
		 props.put("retries", 0);
		 props.put("batch.size", 16384);
		 props.put("linger.ms", 1);
		 props.put("buffer.memory", 33554432);
		 props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		 props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		 Producer<String, String> producer = new KafkaProducer<>(props);
		 for(int i = 0; i < 10; i++){
			 producer.send(new ProducerRecord<String, String>("jiketest", Integer.toString(i), "this is just a demo by wuwenchang"));
		 }
		 
		/* for(int i = 1; i <= 5; i++){
		List<kafka.javaapi.producer.ProducerData> messageList = new ArrayList<ProducerData<String, String>>();
	     for(int j = 0; j < 4; j++){
	    	 messageList.add(new KeyedMessage<String, String>("topic2", j+"", "The " + i + " message for key " + j));
	     }
	     producer.send(messageList);
		    }*/
		 producer.close();
	}

}
