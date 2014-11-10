package raele.util.android.baseactivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import raele.util.android.log.Ident;
import android.view.View;
import android.view.View.OnClickListener;

public class OnClickMethodInvoker implements OnClickListener {
	
	private Object object;
	private Method method;

	public OnClickMethodInvoker(Object toInvoke, String methodName)
	throws IllegalArgumentException
	{
		Ident.begin();
		try {
			object = toInvoke;
			method = toInvoke.getClass().getMethod(methodName, View.class);
		} catch (NoSuchMethodException e) {
			Ident.error(e.getMessage());
			throw new IllegalArgumentException(e);
		} finally {
			Ident.end();
		}
	}

	public OnClickMethodInvoker(Object toInvoke, Method method)
	{
		this.object = toInvoke;
		this.method = method;
	}

	@Override
	public void onClick(View v) {
		Ident.begin();
		boolean accessible = method.isAccessible();
		try {
			method.setAccessible(true);
			method.invoke(object, v);
		} catch (IllegalAccessException e) {
			Ident.error("Got IllegalAccessException: " + e.getMessage());
		} catch (InvocationTargetException e) {
			Ident.printStackTrace(e.getCause());
		} catch (IllegalArgumentException e) {
			Ident.error("Couldn't execute method " + method.getName());
			Ident.error("Make sure this method accept only one parameter of type View.");
			Ident.printStackTrace(e);
		} finally {
			method.setAccessible(accessible);
			Ident.end();
		}
	}
	
}