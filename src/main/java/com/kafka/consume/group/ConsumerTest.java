package com.kafka.consume.group;

import java.io.UnsupportedEncodingException;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
 
public class ConsumerTest implements Runnable {
    private KafkaStream m_stream;
    private int m_threadNumber;
 
    public ConsumerTest(KafkaStream a_stream, int a_threadNumber) {
        m_threadNumber = a_threadNumber;
        m_stream = a_stream;
    }
 
    public void run() {
    	
        ConsumerIterator<byte[], byte[]> iterator = m_stream.iterator();
        while (iterator.hasNext()) {  
  	        MessageAndMetadata<byte[], byte[]> next = iterator.next();  
  	        System.out.println("partiton:" + next.partition()+"分区上的偏移量为："+next.offset()+"所属线程号："+m_threadNumber);  
  	        try {
					System.out.println("消费为"+"message:" + new String(next.message(), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
  	      }  
      
        System.out.println("Shutting down Thread: " + m_threadNumber);
    }
}