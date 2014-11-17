package raele.concurseiro.ui.adapter;

import java.util.List;

import raele.concurseiro.R;
import raele.concurseiro.persistence.Subject;
import raele.concurseiro.ui.activity.TopicSelectionActivity.PremadeStudy;
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
		public void onRemoveStudy(View view, Button button, PremadeStudy study);
	}
	
	private static final int ITEM_LAYOUT = R.layout.layout_item_study;
	
	private Context context;
	private LayoutInflater inflater;
	private List<PremadeStudy> studies;
	private SubjectSpinnerAdapter subjectSpinnerAdapter;
	private Handler handler;

	public StudyListAdapter(Context context, LayoutInflater inflater,
			List<PremadeStudy> studies, List<Subject> subjects, Handler handler)
	{
		super();
		this.context = context;
		this.inflater = inflater;
		this.studies = studies;
		this.handler = handler;
		this.subjectSpinnerAdapter =
				new SubjectSpinnerAdapter(this.context, this.inflater, subjects);
	}

	@Override
	public int getCount() {
		return this.studies.size();
	}

	@Override
	public PremadeStudy getItem(int index) {
		PremadeStudy result;
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
		PremadeStudy study = this.getItem(position);
		View result = this.createViewFor(study);
		return result;
	}

	private View createViewFor(final PremadeStudy study) {
		final View result = this.inflater.inflate(ITEM_LAYOUT, null);
		
		try {
			final Spinner subjectSpinner = (Spinner) result.findViewById(R.id.Study_SubjectSpinner);
			subjectSpinner.setAdapter(this.subjectSpinnerAdapter);
			int index = subjectSpinner.getSelectedItemPosition();
			Subject selectedSubject = this.subjectSpinnerAdapter.getItem(index);
			study.setSubject(selectedSubject);
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
					Ident.error("Input \"" + text + "\" from user is not an integer.");
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
