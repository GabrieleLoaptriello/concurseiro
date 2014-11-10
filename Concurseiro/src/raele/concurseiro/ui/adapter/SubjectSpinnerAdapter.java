package raele.concurseiro.ui.adapter;

import java.util.List;

import raele.concurseiro.R;
import raele.concurseiro.entity.Subject;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SubjectSpinnerAdapter extends BaseAdapter {
	
	public static final int NEW_SUBJECT_PLACEHOLDER_ID = -1;

	private final Subject newSubjectPlaceholder;
	private List<Subject> subjects;
	private Context context;
	
	public SubjectSpinnerAdapter(Context context, List<Subject> subjects) {
		this.context = context;
		this.subjects = subjects;
		
		this.newSubjectPlaceholder = new Subject();
		this.newSubjectPlaceholder.setId(NEW_SUBJECT_PLACEHOLDER_ID);
		this.newSubjectPlaceholder.setName(this.context.getString(R.string.Subject_NewSubject));
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	@Override
	public int getCount() {
		return this.subjects.size() + 1;
	}

	@Override
	public Subject getItem(int index) {
		Subject result;
		if (index < this.subjects.size()) {
			result = this.subjects.get(index);
		} else if (index == this.subjects.size()) {
			result = this.newSubjectPlaceholder;
		} else {
			result = null;
		}
		return result;
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
