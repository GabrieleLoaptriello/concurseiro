package raele.concurseiro.ui.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import raele.concurseiro.R;
import raele.concurseiro.controller.SubjectController;
import raele.concurseiro.persistence.Subject;
import raele.concurseiro.ui.activity.TopicSelectionActivity.PremadeStudy;
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
	private LinkedList<PremadeStudy> studies;
	private StudyListAdapter studyListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Ident.begin();
		super.onCreate(savedInstanceState);
		
		Ident.log("Initializing variables...");
		this.studies = new LinkedList<PremadeStudy>();
		this.studyListAdapter = this.createStudyListAdapterFor(this.studies);
		this.studyListView.setAdapter(this.studyListAdapter);
		
		Ident.log("Including one empty initial Study to the list.");
		this.actionAddStudy(null);
		
		Ident.end();
	}
	
	@ActionOnClick(viewId = R.id.RecordStudy_AddStudy)
	private void actionAddStudy(View view) {
		Ident.begin();
		
		Ident.log("Creating study...");
		PremadeStudy newStudy = new PremadeStudy();
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
			Ident.log("Building studies bundle to send to TopicSelectionActivity activity.");
			ArrayList<PremadeStudy> sendingStudies = new ArrayList<PremadeStudy>(this.studies.size());
			sendingStudies.addAll(this.studies);
			
			Bundle extras = new Bundle();
			extras.putParcelableArrayList(TopicSelectionActivity.BUNDLE_PREMADE_STUDIES_KEY, sendingStudies);
			
			Ident.log("Starting new activity.");
			Intent intent = new Intent(this, TopicSelectionActivity.class);
			intent.putExtras(extras);
			this.startActivity(intent);
			this.finish();
		}
		
		Ident.end();
	}

	private StudyListAdapter createStudyListAdapterFor(LinkedList<PremadeStudy> studies) {
		Ident.begin();
		Ident.log("Creating new StudyListAdapter for " + studies);

		Context context = this;
		LayoutInflater inflater = this.getLayoutInflater();
		List<Subject> subjects;
		StudyListAdapter.Handler studyHandler;
		
		SubjectController subjectController = new SubjectController(context);
		subjects = subjectController.getAll();
		
		studyHandler = new StudyListAdapter.Handler() {
			@Override
			public void onRemoveStudy(View view, Button button, PremadeStudy study) {
				RecordStudyActivity.this.onRemoveStudy(study);
			}
		};
		
		StudyListAdapter result = new StudyListAdapter(
				context, inflater, studies, subjects, studyHandler);
		
		Ident.log("Created adapter: " + result);
		Ident.end();
		
		return result;
	}

	/*private*/ void onRemoveStudy(PremadeStudy study) {
		Ident.begin();
		
		Ident.log("Removing study " + study);
		this.studies.remove(study);
		
		Ident.log("Notifying changes to list adapter.");
		this.studyListAdapter.notifyDataSetChanged();
		
		Ident.end();
	}
	
}
