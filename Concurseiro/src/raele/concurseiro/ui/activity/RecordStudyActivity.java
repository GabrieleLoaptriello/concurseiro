package raele.concurseiro.ui.activity;

import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import raele.concurseiro.R;
import raele.concurseiro.controller.StudyController;
import raele.concurseiro.controller.SubjectController;
import raele.concurseiro.entity.Study;
import raele.concurseiro.entity.Subject;
import raele.concurseiro.ui.adapter.StudyListAdapter;
import raele.util.android.baseactivity.ActionOnClick;
import raele.util.android.baseactivity.ActivityContentLayout;
import raele.util.android.baseactivity.BaseActivity;
import raele.util.android.baseactivity.FromScreenView;
import raele.util.android.log.Ident;

@ActivityContentLayout(layout = R.layout.activity_record_study)
public class RecordStudyActivity extends BaseActivity {
	
	@FromScreenView(viewId = R.id.RecordStudy_StudyList)
	private ListView studyListView;
	private LinkedList<Study> studies;
	private StudyListAdapter studyListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Ident.begin();
		super.onCreate(savedInstanceState);
		
		Ident.log("Initializing variables...");
		this.studies = new LinkedList<Study>();
		this.studyListAdapter = this.createStudyListAdapterFor(this.studies);
		this.studyListView.setAdapter(this.studyListAdapter);
		
		Ident.log("Including one empty initial Study to the list.");
		this.actionAddStudy(null);
		
		Ident.end();
	}
	
	@ActionOnClick(viewId = R.id.RecordStudy_AddStudy)
	public void actionAddStudy(View view) {
		Ident.begin();
		
		Ident.log("Creating study...");
		Study newStudy = new Study();
		newStudy.setTime(1);
		
		Ident.log("New study created: " + newStudy);
		this.studies.add(newStudy);
		
		Ident.log("Notifying the adapter.");
		this.studyListAdapter.notifyDataSetChanged();
		
		Ident.end();
	}
	
	@ActionOnClick(viewId = R.id.RecordStudy_ConfirmButton)
	public void confirmAndContinue(View view) {
		Ident.begin();
		
		if (this.studies.isEmpty())
		{
			Ident.log("No studies added. Showing dialog...");
			
			new AlertDialog.Builder(this)
					.setTitle(R.string.warning)
					.setMessage(R.string.RecordStudy_NoStudies)
					.setPositiveButton(R.string.confirm, null)
					.create()
					.show();
		}
		else
		{
			Ident.log("Persisting new studies.");
			StudyController controller = new StudyController(this);
			controller.put(this.studies);
			
			Ident.log("Redirecting to summary screen.");
			this.startActivity(SummaryActivity.class);
		}
		
		Ident.end();
	}

	private StudyListAdapter createStudyListAdapterFor(LinkedList<Study> studies) {
		Ident.begin();
		Ident.log("Creating new StudyListAdapter for " + studies);

		Context context = this;
		LayoutInflater inflater = this.getLayoutInflater();
		List<Subject> subjects = null;
		StudyListAdapter.Handler studyHandler = null;
		
		SubjectController subjectController = new SubjectController(context);
		subjects = subjectController.getAll();
		
		studyHandler = new StudyListAdapter.Handler() {
			@Override
			public void onRemoveStudy(View view, Button button, Study study) {
				RecordStudyActivity.this.onRemoveStudy(study);
			}
		};
		
		StudyListAdapter result = new StudyListAdapter(
				context, inflater, studies, subjects, studyHandler);
		
		Ident.log("Created adapter: " + result);
		Ident.end();
		
		return result;
	}

	/*private*/ void onRemoveStudy(Study study) {
		Ident.begin();
		
		Ident.log("Removing study " + study);
		this.studies.remove(study);
		
		Ident.log("Notifying changes to list adapter.");
		this.studyListAdapter.notifyDataSetChanged();
		
		Ident.end();
	}
	
}
