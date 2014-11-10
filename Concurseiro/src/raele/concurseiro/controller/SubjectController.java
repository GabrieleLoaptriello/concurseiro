package raele.concurseiro.controller;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import raele.concurseiro.entity.Subject;
import raele.concurseiro.persistence.DH;
import raele.util.android.log.Ident;

public class SubjectController {
	
	private Context context;

	public SubjectController(Context context)
	{
		this.context = context;
	}
	
	public List<Subject> getSubjectOptions()
	{
		Ident.begin();
		Ident.log("Querying all subjects...");
		
		Cursor c = new DH(this.context).queryBuilder()
				.table(Subject.TABLE)
				.column(Subject.COLUMN_ID)
				.column(Subject.COLUMN_NAME)
				.where(Subject.COLUMN_NAME + DH.EQUALS + "leonardo")
				.query();

		List<Subject> result = new LinkedList<Subject>();
		while (c.moveToNext())
		{
			Subject subject = new Subject();
			subject.load(c);
			result.add(subject);
		}
		
		// FIXME Quais são necessários fechar?
		c.close();
//		db.close();
//		dh.close();
		
		Ident.log("Returning " + result.size() + " subjects.");
		Ident.end();
		
		return result;
	}

	public void put(Subject subject) {
		Ident.begin();
		Ident.log("Persisting " + subject);
		
		DH dh = new DH(this.context);
		SQLiteDatabase db = dh.getWritableDatabase();
		
		db.beginTransaction();
		if (subject.getId() == null) {
			Ident.log("Inserting subject " + subject);
			db.insert(Subject.TABLE, null, subject.unload());
		} else {
			Ident.log("Updating subject " + subject);
			db.update(Subject.TABLE, subject.unload(), "id = ?", new String[] {""+subject.getId()});
		}
		db.endTransaction();
		
		Ident.end();
	}

}
