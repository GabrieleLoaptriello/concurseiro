package raele.concurseiro.persistence;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "topic")
public class Topic extends Model {
	
	@Column(name = "name")		private String name;
	@Column(name = "subject")	private Subject subject;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	@Override
	public String toString() {
		return ""+this.name;
	}
	
}
