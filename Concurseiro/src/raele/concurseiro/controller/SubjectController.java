package raele.concurseiro.controller;

import java.util.List;

import com.activeandroid.query.Select;

import android.content.Context;
import raele.concurseiro.persistence.Subject;
import raele.util.android.log.Ident;

public class SubjectController extends BaseController<Subject> {
	
	public SubjectController(Context context)
	{
		super(Subject.class, context);
	}
	
	public List<Subject> getAll()
	{
		Ident.begin();
		
		List<Subject> result = new Select()
				.from(Subject.class)
				.execute();
		
		Ident.end();
		
		return result;
	}

}
