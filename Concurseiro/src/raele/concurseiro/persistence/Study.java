package raele.concurseiro.persistence;

import java.util.Calendar;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "study")
public class Study extends Model {

	@Column(name = "topic")		private Topic topic;
	@Column(name = "calendar")	private Calendar calendar;
	@Column(name = "time")		private Integer time;
	
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public Calendar getCalendar() {
		return calendar;
	}
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "" + this.topic + " (" + this.time + "h)";
	}
	
}
