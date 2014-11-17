package raele.concurseiro.ui.activity;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.activeandroid.query.Select;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import raele.concurseiro.R;
import raele.concurseiro.persistence.Study;
import raele.concurseiro.persistence.Subject;
import raele.concurseiro.persistence.Topic;
import raele.util.android.baseactivity.ActionOnClick;
import raele.util.android.baseactivity.ActivityContentLayout;
import raele.util.android.baseactivity.BaseActivity;
import raele.util.android.baseactivity.FromIntentExtras;
import raele.util.android.baseactivity.FromScreenView;
import raele.util.android.log.Ident;

@ActivityContentLayout(layout = R.layout.activity_topic_selection)
public class TopicSelectionActivity extends BaseActivity {

	public static final String BUNDLE_PREMADE_STUDIES_KEY = "studies";
	
	public static class PremadeStudy implements Parcelable {
		private Integer time;
		private Subject subject;
		
		public PremadeStudy() {}
		
		public PremadeStudy(Parcel source) {
			this.time = source.readInt();
			long id = source.readLong();
			if (id != -1l) {
				this.subject = new Select() // Is there a better approach to this?
						.from(Subject.class)
						.where("Id = ?", id)
						.executeSingle();
			}
		}
		
		public Integer getTime() {
			return time;
		}
		public void setTime(Integer time) {
			this.time = time;
		}
		public Subject getSubject() {
			return subject;
		}
		public void setSubject(Subject subject) {
			this.subject = subject;
		}

	     public static final Parcelable.Creator<PremadeStudy> CREATOR
	             = new Parcelable.Creator<PremadeStudy>() {
	         public PremadeStudy createFromParcel(Parcel in) {
	             return new PremadeStudy(in);
	         }

	         public PremadeStudy[] newArray(int size) {
	             return new PremadeStudy[size];
	         }
	     };
		
		@Override
		public int describeContents() {
			return 0; // I don't have idea what I'm doing here... =]
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(this.time);
			if (this.subject != null && this.subject.getId() != null) {
				dest.writeLong(this.subject.getId());
			} else {
				dest.writeLong(-1l);
			}
		}
		
		public Study createStudy(Topic topic) {
			Study study = new Study();
			study.setCalendar(new GregorianCalendar());
			study.setTime(this.time);
			study.setTopic(topic);
			return study;
		}
		
	}

	@FromIntentExtras(key = BUNDLE_PREMADE_STUDIES_KEY)
	private ArrayList<PremadeStudy> studies;
	
	@FromScreenView(viewId = R.id.TopicSelection_TopicList)
	private ListView topicList;
	
	private Iterator<PremadeStudy> iterator;
	private PremadeStudy currentStudy;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Ident.begin();
		super.onCreate(savedInstanceState);
		
		this.topicList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		this.iterator = this.studies.iterator();
		this.actionNextStudy(null);
		
		Ident.end();
	}

	@ActionOnClick(viewId = R.id.TopicSelection_ConfirmButton)
	public void actionNextStudy(View view) {
		Ident.begin();
		
		Object selected = this.topicList.getSelectedItem();
		Ident.log("What is selected: " + selected);
		// TODO Extract Study from this selected object.
		
		if (this.iterator.hasNext()) {
			this.currentStudy = this.iterator.next();
			Ident.log("Next study is " + this.currentStudy);
			this.setupListAdapter(this.currentStudy);
		} else {
			Ident.log("No more studies to select topic.");
			this.topicList.setAdapter(null);
			// TODO Finish activity
		}
		
		Ident.end();
	}

	private void setupListAdapter(PremadeStudy study) {
		Ident.begin();
		ListAdapter adapter;
		
		if (study == null || study.getSubject() == null) {
			adapter = null;
		} else {
			List<Topic> topics = new Select()
					.from(Topic.class)
					.where("subject = ?", study.getSubject())
					.execute();
			
			ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>(topics.size());
			for (Topic topic : topics) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(""+R.id.TopicSelectionItem_Name, topic.getName());
				data.add(map);
			}
			
			Context context = this;
			int resource = R.layout.layout_item_topic_selection;
			String[] from = new String[] {""+R.id.TopicSelectionItem_Name};
			int[] to = new int[] {R.id.TopicSelectionItem_Name};
			
			adapter = new SimpleAdapter(context , data, resource, from, to);
		}
		
		this.topicList.setAdapter(adapter);
		Ident.end();
	}
	
	@ActionOnClick(viewId = R.id.TopicSelection_NewTopicButton)
	private void actionOnNewtopic(View view) {
		final EditText textEdit = new EditText(this);
		
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// FIXME Gambiarra!! :DDD Êêêeeee...!!!
				String text = textEdit.getText().toString();
				Topic newTopic = new Topic();
				newTopic.setName(text);
				newTopic.setSubject(TopicSelectionActivity.this.currentStudy.getSubject());
				newTopic.save();
				TopicSelectionActivity.this.setupListAdapter(TopicSelectionActivity.this.currentStudy);
			}
		};
		
		new AlertDialog.Builder(this)
				.setTitle(R.string.TopicSelection_DialogTitle)
				.setMessage(R.string.TopicSelection_DialogMessage)
				.setView(textEdit)
				.setPositiveButton(R.string.confirm, listener)
				.setNegativeButton(R.string.cancel, null)
				.create()
				.show();
	}
	
}

