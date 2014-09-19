package raele.concurseiro.activity;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import raele.concurseiro.R;
import raele.concurseiro.StudyListAdapter;
import raele.concurseiro.entity.Study;
import raele.concurseiro.entity.Subject;
import raele.util.android.Ident;
import raele.util.android.baseactivity.ActionOnClick;
import raele.util.android.baseactivity.ActivityContentLayout;
import raele.util.android.baseactivity.BaseActivity;
import raele.util.android.baseactivity.FromScreenView;

@ActivityContentLayout(layout = R.layout.activity_record_study)
public class RecordStudyActivity extends BaseActivity {
	
	@FromScreenView(viewId = R.id.RecordStudy_StudyList)
	private ListView studyListView;
	private LinkedList<Study> studies;
	private List<Subject> subjects;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Ident.begin();
		super.onCreate(savedInstanceState);
		
		Ident.log("Including one empty initial Study to the list.");
		this.studies = new LinkedList<Study>();
		this.actionAddStudy(null);
		
		Ident.end();
	}
	
	@ActionOnClick(viewId = R.id.RecordStudy_AddStudy)
	public void actionAddStudy(View view) {
		Ident.begin();
		
		Study newStudy = new Study();
		newStudy.setTime(1);
		this.studies.add(newStudy);
		this.refreshAdapter();
		
		Ident.end();
	}

	private void refreshAdapter() {
		Ident.begin();
		
		Context context = this;
		LayoutInflater inflater = this.getLayoutInflater();
		int layout = R.layout.item_study;
		List<Subject> subjects = this.getSubjects();
		StudyListAdapter adapter = new StudyListAdapter(context, inflater, layout, this.studies, subjects);
		this.studyListView.setAdapter(adapter);
		
		Ident.end();
	}

	private synchronized List<Subject> getSubjects() {
		if (this.subjects == null)
		{
			this.subjects = this.loadSubjects();
		}
		return this.subjects;
	}

	private List<Subject> loadSubjects() {
		List<Subject> result = new LinkedList<Subject>();
		
		DH dh = new DH(this);
		SQLiteDatabase db = new DH(this).getReadableDatabase();
		Cursor c = db.query(DH.Subject.TABLE_NAME,
				new String[] {DH.Subject.COLUMN_NAME},
				null, null, null, null, null);
		
		while (c.moveToNext())
		{
			Subject subject = new Subject();
			
			subject.setName(c.getString(0));
			
			result.add(subject);
		}
		
		c.close();
		db.close();
		dh.close();
		
		return result;
	}
	
}
