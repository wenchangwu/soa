package com.soa.jaxrs;

public class StudentServiceImpl implements StudentService {

	@Override
	public Student getStudent(long id, String name) {
		Student s1=new Student();
		s1.setId(1L);
		s1.setName("���Ĳ�");
		return s1;
	}

	@Override
	public Student getStudent(String name) {
		Student s1=new Student();
		s1.setId(2L);
		s1.setName("����ϼ");
		return s1;
	}

}
