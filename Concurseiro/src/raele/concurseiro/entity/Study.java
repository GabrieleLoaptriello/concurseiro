package raele.concurseiro.entity;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import raele.concurseiro.persistence.DH;

public class Study implements Entity {
	
	public static final String TABLE = "study";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SUBJECT = "subject";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_DATE = "date";
	
	public static final String TYPES =
			COLUMN_ID + DH.INTEGER + DH.PRIMARY_KEY + "," +
			COLUMN_SUBJECT + DH.INTEGER + "," +
			COLUMN_TIME + DH.INTEGER + "," +
			COLUMN_DATE + DH.INTEGER;
	
	@Override
	public void load(Cursor c) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ContentValues unload() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Long id;
	private Subject subject;
	private Date date;
	private Integer time;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		String name = "none";
		if (this.subject != null) {
			name = this.subject.getName();
		}
		return name + " (" + this.time + ")";
	}
	
}
