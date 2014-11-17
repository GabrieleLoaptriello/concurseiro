package raele.concurseiro.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import raele.concurseiro.R;
import raele.concurseiro.persistence.Subject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SubjectSpinnerAdapter extends BaseAdapter {
	
	private final Subject noSubjectPlaceholder;
	private List<Subject> subjects;
	private LayoutInflater inflater;
	private Context context;
	
	public SubjectSpinnerAdapter(Context context, LayoutInflater inflater, List<Subject> subjects) {
		this.context = context;
		this.inflater = inflater;
		
		this.noSubjectPlaceholder = new Subject();
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
		Long id = this.getItem(index).getId();
		
		if (id == null) {
			id = -1L;
		}
		
		return id;
	}

	@Override
	public View getView(int index, View arg1, ViewGroup arg2) {
		String text = ""+this.getItem(index);
		
		View layout = this.inflater.inflate(R.layout.layout_spinner_subject, null);
		
		TextView textView = (TextView) layout.findViewById(R.id.SubjectSpinner_Name);
		textView.setText(text);
		
		return layout;
	}

}
