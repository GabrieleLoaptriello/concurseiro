package raele.concurseiro.controller;

import raele.concurseiro.persistence.Study;
import android.content.Context;

public class StudyController extends BaseController<Study> {

	public StudyController(Context context) {
		super(Study.class, context);
	}

}
