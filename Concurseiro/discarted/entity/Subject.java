package raele.concurseiro.entity;

import android.content.ContentValues;
import android.database.Cursor;
import raele.concurseiro.persistence.DH;

public class Subject extends Entity {
    
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	
	public static final String TYPES =
			COLUMN_ID + DH.INTEGER + DH.PRIMARY_KEY + "," +
			COLUMN_NAME + DH.STRING;

	@Override
	public void load(Cursor c) {
		this.setId( c.getLong(c.getColumnIndex(COLUMN_ID)) );
		this.name = c.getString(c.getColumnIndex(COLUMN_NAME));
	}

	@Override
	public ContentValues unload(ContentValues values) {
		values.put(COLUMN_ID, this.getId());
		values.put(COLUMN_NAME, this.getName());
		return values;
	}
	
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return ""+this.getName();
	}

}
