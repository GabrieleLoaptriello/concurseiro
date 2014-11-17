package raele.concurseiro.entity;

import raele.concurseiro.persistence.DH;
import android.content.ContentValues;
import android.database.Cursor;

public class Topic extends Entity {
	
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_SUBJECT_ID = "subject";
	public static final String COLUMN_ID = "id";
	
	public static final String TYPES =
			COLUMN_ID + DH.INTEGER + DH.PRIMARY_KEY + "," +
			COLUMN_NAME + DH.STRING + "," +
			COLUMN_SUBJECT_ID + DH.INTEGER;

	@Override
	public void load(Cursor c) {
		this.setId(c.getLong(c.getColumnIndex(COLUMN_ID)));
		this.name = c.getString(c.getColumnIndex(COLUMN_NAME));
		this.subjectId = c.getLong(c.getColumnIndex(COLUMN_SUBJECT_ID));
	}

	@Override
	public ContentValues unload(ContentValues values) {
		values.put(COLUMN_ID, this.getId());
		values.put(COLUMN_NAME, this.name);
		values.put(COLUMN_SUBJECT_ID, this.subjectId);
		return values;
	}
	
	private Long subjectId;
	private String name;

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
