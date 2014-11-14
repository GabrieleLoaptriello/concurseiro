package raele.concurseiro.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import raele.concurseiro.R;
import raele.concurseiro.entity.Subject;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SubjectSpinnerAdapter extends BaseAdapter {
	
	private static final long NO_SUBJECT_PLACEHOLDER_ID = -2;

	private final Subject noSubjectPlaceholder;
	private List<Subject> subjects;
	private Context context;
	
	public SubjectSpinnerAdapter(Context context, List<Subject> subjects) {
		this.context = context;
		
		this.noSubjectPlaceholder = new Subject();
		this.noSubjectPlaceholder.setId(NO_SUBJECT_PLACEHOLDER_ID);
		this.noSubjectPlaceholder.setName(this.context.getString(R.string.Subject_NoSubject));
		
		this.subjects = new ArrayList<Subject>(subjects.size() + 1);
		this.subjects.add(this.noSubjectPlaceholder);
		this.subjects.addAll(subjects);
	}

	public void addSubject(Subject subject) {
		this.subjects.add(subject);
	}

	@Override
	public int getCount() {
		return this.subjects.size();
	}

	@Override
	public Subject getItem(int index) {
		return this.subjects.get(index);
	}

	@Override
	public long getItemId(int index) {
		return this.getItem(index).getId();
	}

	@Override
	public View getView(int index, View arg1, ViewGroup arg2) {
		String text = ""+this.getItem(index);
		TextView result = new TextView(this.context);
		result.setText(text);
		return result;
	}

}
