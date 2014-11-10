package raele.concurseiro.ui.activity;

import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import raele.concurseiro.R;
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

	private StudyListAdapter createStudyListAdapterFor(LinkedList<Study> studies) {
		Ident.begin();
		Ident.log("Creating new StudyListAdapter for " + studies);

		Context context = this;
		LayoutInflater inflater = this.getLayoutInflater();
		List<Subject> options = null;
		StudyListAdapter.Handler removeHandler = null;
		
		SubjectController subjectController = new SubjectController(context);
		options = subjectController.getSubjectOptions();
		
		removeHandler = new StudyListAdapter.Handler() {
			@Override
			public void onNewSubject(View view, Spinner spinner, Study study) {
				RecordStudyActivity.this.onNewSubject(study);
			}
			@Override
			public void onRemoveStudy(View view, Button button, Study study) {
				RecordStudyActivity.this.onRemoveStudy(study);
			}
		};
		
		StudyListAdapter result = new StudyListAdapter(
				context, inflater, studies, options, removeHandler);
		
		Ident.log("Created adapter: " + result);
		Ident.end();
		
		return result;
	}

	/*private*/ void onNewSubject(final Study study) {
		Ident.begin();
		
		final EditText editText = new EditText(this);
		DialogInterface.OnClickListener onPositiveChoice = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String newSubjectName = editText.getText().toString();
				RecordStudyActivity.this.onNewSubjectConfirm(newSubjectName, study);
			}};
		
		AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle(R.string.RecordStudy_NewStudyDialogTitle)
				.setMessage(R.string.RecordStudy_NewStudyDialogMessage)
				.setView(editText)
				.setPositiveButton(R.string.confirm, onPositiveChoice)
				.setNegativeButton(R.string.cancel, null)
				.create();
		dialog.show();
		
		Ident.end();
	}

	/*private*/ void onNewSubjectConfirm(String subjectName, Study study) {
		Ident.begin();
		
		Ident.log("Creating subject " + subjectName);
		Subject newSubject = new Subject();
		newSubject.setName(subjectName);
		
		Ident.log("Persisting this new subject.");
		SubjectController subjectController = new SubjectController(this);
		subjectController.put(newSubject);
		
		Ident.log("Setting study's subject to the new subject.");
		study.setSubject(newSubject);
		
		Ident.log("Adding the new subject to the spinners.");
		this.studyListAdapter.getSubjectSpinnerAdapter().getSubjects().add(newSubject);
		
		Ident.log("Notifying changes to the adapter.");
		this.studyListAdapter.notifyDataSetChanged();
		
		Ident.end();
	}

	/*private*/ void onRemoveStudy(Study study) {
		Ident.begin();
		
		Ident.log("Removing study " + study);
		this.studies.remove(study);
		
		Ident.log("Notifying changes to list adapter.");
		this.studyListAdapter.notifyDataSetChanged();
		
		Ident.end();
	}
	
	@ActionOnClick(viewId = R.id.RecordStudy_AddStudy)
	public void addStudy(View view) {
		Ident.begin();
		Ident.log("Not implemented yet.");
		Ident.end();
		// TODO Add a new Study
	}
	
	@ActionOnClick(viewId = R.id.RecordStudy_ConfirmButton)
	public void onConfirm(View view) {
		
		// TODO Persist info and navigate to next screen
	}
	
}
