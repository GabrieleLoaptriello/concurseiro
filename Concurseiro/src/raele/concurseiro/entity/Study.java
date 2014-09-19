package raele.concurseiro.entity;

import java.util.Calendar;

public class Study {
	
	private Subject subject;
	private Calendar calendar;
	private Integer time;
	
	public Study() {
		super();
	}
	public Study(Subject subject, Calendar date, Integer time) {
		super();
		this.subject = subject;
		this.calendar = date;
		this.time = time;
	}
	
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public Calendar getDate() {
		return calendar;
	}
	public void setDate(Calendar date) {
		this.calendar = date;
	}
	
}
