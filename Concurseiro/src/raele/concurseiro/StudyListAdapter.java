package raele.concurseiro;

import java.util.List;

import raele.concurseiro.entity.Study;
import raele.concurseiro.entity.Subject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class StudyListAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater inflater;
	private int layout;
	private List<Study> studies;
	private List<Subject> subjects;

	public StudyListAdapter(Context context, LayoutInflater inflater, int layoutResource, List<Study> studies, List<Subject> subjects) {
		this.context = context;
		this.inflater = inflater;
		this.layout = layoutResource;
		this.studies = studies;
		this.subjects = subjects;
	}

	@Override
	public int getCount() {
		return this.studies.size();
	}

	@Override
	public Study getItem(int index) {
		Study result;
		try {
			result = this.studies.get(index);
		} catch (IndexOutOfBoundsException e) {
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
			target = new RelativeLayout(this.context);
		}
		
		Study study = this.studies.get(position);
		View result = createItemView(study, target);
		
		return result;
	}

	private View createItemView(Study study, ViewGroup convertView) {
		View result = this.inflater.inflate(this.layout, convertView);
		
		try {
			Spinner subjectSpinner = (Spinner) result.findViewById(R.id.Study_SubjectSpinner);
			SubjectSpinnerAdapter adapter = new SubjectSpinnerAdapter(this.context, this.subjects);
			subjectSpinner.setAdapter(adapter);
		} catch (NullPointerException | ClassCastException e) {}

		try {
			NumberPicker timePicker = (NumberPicker) result.findViewById(R.id.Study_Time);
			timePicker.setMinValue(1);
			timePicker.setMaxValue(24);
		} catch (NullPointerException | ClassCastException e) {}
		
		return result;
	}

}
