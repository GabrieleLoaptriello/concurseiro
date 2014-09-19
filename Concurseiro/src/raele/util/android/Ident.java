package raele.util.android;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

public final class Ident {
	
	private Ident() {}
	
	private static final String TAG = "trace.";
	private static int t = 0;
	private static StringBuilder builder = new StringBuilder();
	
	public static void begin()
	{
		String classMethod = getClassMethod();
		Log.d("" + TAG + classMethod, space() + " -> " + classMethod);
		t++;
		builder.append("    ");
	}
	
	public static void end()
	{
		t--;
		builder = new StringBuilder();
		for (int i = 0; i < t; i++)
		{
			builder.append("    ");
		}
		String classMethod = getClassMethod();
		Log.d("" + TAG + classMethod, space() + " <- " + classMethod + (t==0?"\n":""));
	}
	
	public static void log(Object msg)
	{
		String classMethod = getClassMethod();
		Log.d("" + TAG + classMethod, space() + msg);
	}
	
	public static void error(Object msg)
	{
		String classMethod = getClassMethod();
		Log.e("" + TAG + classMethod, space() + msg);
	}
	
	public static String space()
	{
		return builder.toString();
	}
	
	/**
	 * Get the method name for a depth in call stack. <br />
	 * Utility function
	 * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
	 * @return method name
	 */
	private static String getClassMethod()
	{
		// Based on code found here: http://stackoverflow.com/questions/442747/getting-the-name-of-the-current-executing-method
		final int index = 4;
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		StackTraceElement target = ste[index];
		String methodName = target.getMethodName();
		String className = target.getClassName().replaceAll(".*\\.", "");
		return className + '.' + methodName;
	}

	public static void printStackTrace(Throwable e)
	{
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		error(sw.toString());
	}

}
