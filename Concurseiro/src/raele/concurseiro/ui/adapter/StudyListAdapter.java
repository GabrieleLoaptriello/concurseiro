package raele.concurseiro.ui.adapter;

import java.util.List;

import raele.concurseiro.R;
import raele.concurseiro.entity.Study;
import raele.concurseiro.entity.Subject;
import raele.concurseiro.entity.Topic;
import raele.concurseiro.persistence.DH;
import raele.util.android.log.Ident;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class StudyListAdapter extends BaseAdapter {
	
	public interface Handler {
		public void onRemoveStudy(View view, Button button, Study study);
	}
	
	private static final int ITEM_LAYOUT = R.layout.item_study;
	
	private Context context;
	private LayoutInflater inflater;
	private List<Study> studies;
	private SubjectSpinnerAdapter subjectSpinnerAdapter;
	private Handler handler;

	public StudyListAdapter(Context context, LayoutInflater inflater,
			List<Study> studies, List<Subject> subjects, Handler handler)
	{
		super();
		this.context = context;
		this.inflater = inflater;
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
		Study study = this.getItem(position);
		View result = this.createViewFor(study);
		
		return result;
	}

	private View createViewFor(final Study study) {
		final View result = this.inflater.inflate(ITEM_LAYOUT, null);
		
		try {
			final Spinner subjectSpinner = (Spinner) result.findViewById(R.id.Study_SubjectSpinner);
			subjectSpinner.setAdapter(this.subjectSpinnerAdapter);
			int index = subjectSpinner.getSelectedItemPosition();
			Subject selectedSubject = this.subjectSpinnerAdapter.getItem(index);
			Topic topic = new DH(this.context).queryBuilder() // FIXME Gambiarra! :D
					.table(Topic.class)
					.where(Topic.COLUMN_SUBJECT_ID, DH.EQUALS, selectedSubject.getId())
					.querySingle(Topic.class);
			study.setTopicId(topic.getId());
		} catch (NullPointerException e) {
			Ident.printStackTrace(e);
		} catch (ClassCastException e) {
			Ident.printStackTrace(e);
		}

		try {
			EditText editText = (EditText) result.findViewById(R.id.Study_Time);
			String text = editText.getText().toString();
			
			// FIXME This strategy is not working.
			if (!"".equals(text)) {
				try {
					Integer time = Integer.parseInt(text);
					study.setTime(time);
				} catch (NumberFormatException e) {
					Ident.error("Input from user \"" + text + "\" is not an integer.");
				}
			}
			
			editText.setText("" + study.getTime());
		} catch (NullPointerException e) {
			Ident.printStackTrace(e);
		} catch (ClassCastException e) {
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
		} catch (NullPointerException e) {
			Ident.printStackTrace(e);
		} catch (ClassCastException e) {
			Ident.printStackTrace(e);
		}
		
		return result;
	}

}
