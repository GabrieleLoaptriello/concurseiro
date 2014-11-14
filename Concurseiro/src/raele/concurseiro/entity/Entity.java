package raele.concurseiro.entity;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class Entity {
	
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public abstract void load(Cursor c);
	
	public abstract ContentValues unload(ContentValues values);
	
}
