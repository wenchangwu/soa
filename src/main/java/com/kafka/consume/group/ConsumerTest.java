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
  	        System.out.println("partiton:" + next.partition()+"�����ϵ�ƫ����Ϊ��"+next.offset()+"�����̺߳ţ�"+m_threadNumber);  
  	        try {
					System.out.println("����Ϊ"+"message:" + new String(next.message(), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
  	      }  
      
        System.out.println("Shutting down Thread: " + m_threadNumber);
    }
}