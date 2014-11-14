package raele.concurseiro.controller;

import java.util.Collection;

import raele.concurseiro.entity.Entity;
import raele.concurseiro.persistence.DH;
import raele.util.android.log.Ident;
import android.content.Context;

public class BaseController<T extends Entity> {
	
	private Context context;

	public BaseController(Context context)
	{
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void put(T t) {
		Ident.begin();
		Ident.log("Persisting " + t);
		
		DH dh = new DH(this.context);
		dh.save(t);
		dh.close();
		
		Ident.log("Successfully persisted with id " + t.getId());
		Ident.end();
	}

	public void put(Collection<T> collection) {
		Ident.begin();
		
		for (T t : collection) {
			this.put(t);
		}
		
		Ident.end();
	}
	
	public void remove(T t) {
		Ident.begin();
		Ident.log("Deleting " + t);
		
		DH dh = new DH(this.context);
		dh.delete(t);
		dh.close();
		
		Ident.end();
	}
	
	public void remove(Collection<T> collection) {
		Ident.begin();
		
		for (T t : collection) {
			this.remove(t);
		}
		
		Ident.end();
	}

}
