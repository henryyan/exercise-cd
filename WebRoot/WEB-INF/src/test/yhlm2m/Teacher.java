package test.yhlm2m;

import java.util.HashSet;
import java.util.Set;

public class Teacher {
	private Integer tid;
	private String tname;
	private Set<StudentTeacherLink> studentTeacherLinks = new HashSet<StudentTeacherLink>(0);

	public Teacher() {
	}

	public Teacher(String tname) {
		this.tname = tname;
	}

	public Teacher(String tname, Set<StudentTeacherLink> studentTeacherLinks) {
		this.tname = tname;
		this.studentTeacherLinks = studentTeacherLinks;
	}

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public Set<StudentTeacherLink> getStudentTeacherLinks() {
		return studentTeacherLinks;
	}

	public void setStudentTeacherLinks(Set<StudentTeacherLink> studentTeacherLinks) {
		this.studentTeacherLinks = studentTeacherLinks;
	}

}