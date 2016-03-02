package com.soa.jaxrs;

public class StudentServiceImpl implements StudentService {

	@Override
	public Student getStudent(long id, String name) {
		Student s1=new Student();
		s1.setId(1L);
		s1.setName("ÎâÎÄ²ı");
		return s1;
	}

	@Override
	public Student getStudent(String name) {
		Student s1=new Student();
		s1.setId(2L);
		s1.setName("ÎâÎÄÏ¼");
		return s1;
	}

}
