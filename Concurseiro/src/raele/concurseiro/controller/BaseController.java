package raele.concurseiro.controller;

import java.util.Collection;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;

import raele.util.android.log.Ident;
import android.content.Context;

public class BaseController<T extends Model> {
	
	private Context context;
	private Class<T> tClass;

	public BaseController(Class<T> tClass, Context context) {
		this.tClass = tClass;
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void put(T t) {
		Ident.begin();
		
		t.save();
		
		Ident.end();
	}

	public void put(Collection<T> collection) {
		Ident.begin();
		
		try {
			ActiveAndroid.beginTransaction();
			for (T t : collection) {
				t.save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}
		
		Ident.end();
	}
	
	public void remove(T t) {
		Ident.begin();
		
		t.delete();
		
		Ident.end();
	}
	
	public void remove(Long id) {
		Ident.begin();
		
		Model.delete(this.tClass, id);
		
		Ident.end();
	}
	
	public void remove(Collection<T> collection) {
		Ident.begin();
		
		try {
			ActiveAndroid.beginTransaction();
			for (T t : collection) {
				t.delete();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}
		
		Ident.end();
	}

}
