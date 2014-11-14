package raele.concurseiro.entity;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import raele.concurseiro.persistence.DH;

public class Study extends Entity {
	
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_TOPIC_ID = "subject";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_DATE = "date";
	
	public static final String TYPES =
			COLUMN_ID + DH.INTEGER + DH.PRIMARY_KEY + "," +
			COLUMN_TOPIC_ID + DH.INTEGER + "," +
			COLUMN_TIME + DH.INTEGER + "," +
			COLUMN_DATE + DH.INTEGER;

	@Override
	public void load(Cursor c) {
		this.setId(c.getLong(c.getColumnIndex(COLUMN_ID)));
		this.topicId = c.getLong(c.getColumnIndex(COLUMN_TOPIC_ID));
		this.time = c.getInt(c.getColumnIndex(COLUMN_TIME));;
		this.date = new Date(c.getLong(c.getColumnIndex(COLUMN_DATE)));
	}

	@Override
	public ContentValues unload(ContentValues values) {
		values.put(COLUMN_ID, this.getId());
		values.put(COLUMN_TIME, this.time);
		values.put(COLUMN_TOPIC_ID, this.topicId);
		if (this.date != null) {
			values.put(COLUMN_DATE, this.date.getTime());
		}
		return values;
	}
	
	private Long topicId;
	private Date date;
	private Integer time;
	
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
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
		return "{" + this.time + " hours of topic #" + this.topicId + "}";
	}
	
}
