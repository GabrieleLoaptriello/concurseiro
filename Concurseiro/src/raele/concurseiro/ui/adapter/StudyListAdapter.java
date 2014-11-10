package raele.concurseiro.ui.adapter;

import java.util.List;

import raele.concurseiro.R;
import raele.concurseiro.entity.Study;
import raele.concurseiro.entity.Subject;
import raele.util.android.log.Ident;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

public class StudyListAdapter extends BaseAdapter {
	
	public interface Handler {
		public void onNewSubject(View view, Spinner spinner, Study study);
		public void onRemoveStudy(View view, Button button, Study study);
	}
	
	private static final int LAYOUT = R.layout.item_study;
	
	private Context context;
	private LayoutInflater inflater;
	private int layout;
	private List<Study> studies;
	private SubjectSpinnerAdapter subjectSpinnerAdapter;
	private Handler handler;

	public StudyListAdapter(Context context, LayoutInflater inflater,
			List<Study> studies, List<Subject> subjects, Handler handler)
	{
		super();
		this.context = context;
		this.inflater = inflater;
		this.layout = LAYOUT;
		this.studies = studies;
		this.handler = handler;
		this.subjectSpinnerAdapter =
				new SubjectSpinnerAdapter(this.context, subjects);
	}

	public SubjectSpinnerAdapter getSubjectSpinnerAdapter() {
		return subjectSpinnerAdapter;
	}

	@Override
	public int getCount() {
		return this.studies.size();
	}

	@Override
	public Study getItem(int index) {
		Study result;
		if (index < this.studies.size()) {
			result = this.studies.get(index);
		} else {
			result = null;
		}
		return result;
	}

	@Override
	public long getItemId(int index) {
		return -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewGroup target;
		
		if (convertView instanceof ViewGroup)
		{
			target = (ViewGroup) convertView;
		}
		else
		{
			target = new FrameLayout(this.context);
		}
		
		Study study = this.getItem(position);
		View result = createItemView(study, target);
		
		return result;
	}

	private View createItemView(final Study study, ViewGroup convertView) {
		final View result = this.inflater.inflate(this.layout, convertView);
		
		try {
			final Spinner subjectSpinner = (Spinner) result.findViewById(R.id.Study_SubjectSpinner);
			subjectSpinner.setAdapter(this.subjectSpinnerAdapter);
			subjectSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					if (StudyListAdapter.this.subjectSpinnerAdapter.getItemId(position) ==
							SubjectSpinnerAdapter.NEW_SUBJECT_PLACEHOLDER_ID)
					{
						StudyListAdapter.this.handler.onNewSubject(result, subjectSpinner, study);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// Do nothing
				}
			});
		} catch (NullPointerException | ClassCastException e) {
			Ident.printStackTrace(e);
		}

		try {
			EditText editText = (EditText) result.findViewById(R.id.Study_Time);
			editText.setText("" + study.getTime());
		} catch (NullPointerException | ClassCastException e) {
			Ident.printStackTrace(e);
		}

		try {
			final Button removeButton = (Button) result.findViewById(R.id.Study_ButtonRemove);
			removeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					StudyListAdapter.this.handler.onRemoveStudy(result, removeButton, study);
				}
			});
		} catch (NullPointerException | ClassCastException e) {
			Ident.printStackTrace(e);
		}
		
		return result;
	}

}
