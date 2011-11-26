package test.yhlm2m;

import java.util.HashSet;
import java.util.Set;

public class Student {
	private Integer sid;
	private String sname;
	private Set<StudentTeacherLink> studentTeacherLinks = new HashSet<StudentTeacherLink>(0);

	public Student() {
	}

	public Student(String sname) {
		this.sname = sname;
	}

	public Student(String sname, Set<StudentTeacherLink> studentTeacherLinks) {
		this.sname = sname;
		this.studentTeacherLinks = studentTeacherLinks;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public Set<StudentTeacherLink> getStudentTeacherLinks() {
		return studentTeacherLinks;
	}

	public void setStudentTeacherLinks(Set<StudentTeacherLink> studentTeacherLinks) {
		this.studentTeacherLinks = studentTeacherLinks;
	}
	
	
}