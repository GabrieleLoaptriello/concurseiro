package raele.concurseiro.entity;

import android.content.ContentValues;
import android.database.Cursor;

public interface Entity {
	
	public void load(Cursor c);
	public ContentValues unload();
	
}
