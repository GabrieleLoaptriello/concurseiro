package raele.concurseiro;

import java.util.List;

import raele.concurseiro.entity.Subject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SubjectSpinnerAdapter extends BaseAdapter {
	
	private List<Subject> subjects;
	private Context context;
	
	public SubjectSpinnerAdapter(Context context, List<Subject> subjects) {
		this.context = context;
		this.subjects = subjects;
	}

	@Override
	public int getCount() {
		return this.subjects.size();
	}

	@Override
	public Subject getItem(int index) {
		Subject result;
		try {
			result = this.subjects.get(index);
		} catch (IndexOutOfBoundsException e) {
			result = null;
		}
		return result;
	}

	@Override
	public long getItemId(int arg0) {
		return -1;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		String text = ""+this.subjects.get(position);
		TextView result = new TextView(this.context);
		result.setText(text);
		return result;
	}

}
