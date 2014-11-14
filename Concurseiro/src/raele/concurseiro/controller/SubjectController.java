package raele.concurseiro.controller;

import java.util.List;

import android.content.Context;
import raele.concurseiro.entity.Subject;
import raele.concurseiro.persistence.DH;
import raele.util.android.log.Ident;

public class SubjectController extends BaseController<Subject> {
	
	public SubjectController(Context context)
	{
		super(context);
	}
	
	public List<Subject> getAll()
	{
		Ident.begin();
		Ident.log("Querying all subjects...");
		
		List<Subject> result = new DH(this.getContext()).queryBuilder()
				.column(Subject.COLUMN_ID)
				.column(Subject.COLUMN_NAME)
				.table(Subject.class)
				.queryMultiple(Subject.class);
		
		Ident.log("Returning " + result.size() + " subjects.");
		Ident.end();
		
		return result;
	}

}
