package raele.util.android.baseactivity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import raele.util.android.Ident;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class BaseActivity extends ActionBarActivity {

	private Integer layout;
	private Integer menu;
	private SparseArray<Method> onClickMethods;
	private SparseArray<Method> menuItemMethods;
	private SparseArray<Field> screenViewFields;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Ident.begin();
		super.onCreate(savedInstanceState);
		this.setup();
		Ident.end();
	}
	
	private void setup() {
		// Initialize attributes
		this.menuItemMethods = new SparseArray<Method>();
		this.onClickMethods = new SparseArray<Method>();
		this.screenViewFields = new SparseArray<Field>();
		this.menu = null;
		this.layout = null;
		
		// Check method annotations
		for (Method method : getMethods())
		{
			this.setupOnClickAction(method);
			this.setupMenuItemAction(method);
		}
		
		// Check field annotations
		for (Field field : getFields())
		{
			this.setupFromIntentField(field);
			this.setupFromScreenViewField(field);
		}
		
		// Check class annotations
		this.setupActionBarMenu();
		this.setupContentLayout();
		
		// Setup content view
		if (this.layout != null)
		{
			this.setContentView(this.layout);
			this.setupView(findViewById(android.R.id.content));
		}
		
		// Nullify attributes
		this.menuItemMethods = null;
		this.onClickMethods = null;
		this.screenViewFields = null;
	}

	private void setupActionBarMenu() {
		ActivityActionBarMenu annotation = this.getClass().getAnnotation(ActivityActionBarMenu.class);
		
		if (annotation != null)
		{
			this.menu = annotation.menu();
		}
	}

	private void setupContentLayout() {
		ActivityContentLayout annotation = this.getClass().getAnnotation(ActivityContentLayout.class);
		
		if (annotation != null)
		{
			this.layout = annotation.layout();
		}
	}

	private void setupOnClickAction(Method method) {
		ActionOnClick annotation = method.getAnnotation(ActionOnClick.class);
		
		if (annotation != null)
		{
			this.onClickMethods.put(annotation.viewId(), method);
		}
	}
	
	private void setupMenuItemAction(Method method) {
		ActionForMenuItem annotation = method.getAnnotation(ActionForMenuItem.class);
		
		if (annotation != null)
		{
			this.menuItemMethods.put(annotation.itemId(), method);
		}
	}

	private void setupFromIntentField(Field field) {
		FromIntentExtras annotation = field.getAnnotation(FromIntentExtras.class);
		
		if (annotation != null)
		{
			String key = annotation.key();
			Intent intent = this.getIntent();
			Bundle bundle = intent.getExtras();
			
			if (bundle != null)
			{
				Object value = bundle.get(key);
				boolean accessible = field.isAccessible();
				try {
					field.setAccessible(true);
					field.set(this, value);
				} catch (IllegalAccessException | IllegalArgumentException e) {
					Ident.error("Failed to assign " + value + " to field " + field.getName() + ". Cause: " + e.getMessage());
				} finally {
					field.setAccessible(accessible);
				}
			}
		}
	}

	private void setupFromScreenViewField(Field field) {
		FromScreenView annotation = field.getAnnotation(FromScreenView.class);
		
		if (annotation != null)
		{
			this.screenViewFields.put(annotation.viewId(), field);
		}
	}
	
	private void setupView(View view) {
		int id = view.getId();
		
		// Setup onclick event
		Method method = this.onClickMethods.get(id);
		if (method != null)
		{
			OnClickMethodInvoker listener = new OnClickMethodInvoker(this, method);
			view.setOnClickListener(listener);
		}
		
		// Setup dependency-injection
		Field field = this.screenViewFields.get(id);
		if (field != null)
		{
			boolean accessible = field.isAccessible();
			try {
				field.setAccessible(true);
				field.set(this, view);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				Ident.error("Failed to assign " + view + " to field " + field.getName() + ". Cause: " + e.getMessage());
			} finally {
				field.setAccessible(accessible);
			}
		}
		
		// Proliferate
		if (view instanceof ViewGroup)
		{
			ViewGroup group = (ViewGroup) view;
			
			for (int i = 0; i < group.getChildCount(); i++)
			{
				setupView(group.getChildAt(i));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (this.menu != null)
		{
			this.getMenuInflater().inflate(this.menu, menu);
		}
		
		return this.menu != null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Ident.begin();
		
		int itemId = item.getItemId();
		boolean everythingWentOk = false;
		
		Method method = this.menuItemMethods.get(itemId);
		
		if (method != null)
		{
			boolean accessible = method.isAccessible();
			try {
				method.setAccessible(true);
				method.invoke(this, item);
				everythingWentOk = true;
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				Ident.error("Couldn't execute method " + method.getName());
				Ident.error("Make sure this method accept only one parameter of type MenuItem.");
				Ident.printStackTrace(e);
			} finally {
				method.setAccessible(accessible);
			}
		}
		else
		{
			Ident.log("No method found for item " + item + " (id: " + itemId + ")");
		}
		
		Ident.end();
		return everythingWentOk || super.onOptionsItemSelected(item);
	}

	/**
	 * Get all declared methods from this object's class and any super class that
	 * subclass BaseActivity.
	 */
	private Method[] getMethods() {
		Class<?> currentClass = this.getClass();
		List<Method> methods = Arrays.asList(currentClass.getDeclaredMethods());
		
		while (!(currentClass = currentClass.getSuperclass()).equals(BaseActivity.class))
		{
			methods.addAll(Arrays.asList(currentClass.getDeclaredMethods()));
		}
		
		return methods.toArray(new Method[methods.size()]);
	}

	/**
	 * Get all declared fields from this object's class and any super class that
	 * subclass BaseActivity.
	 */
	private Field[] getFields() {
		Class<?> currentClass = this.getClass();
		List<Field> fields = Arrays.asList(currentClass.getDeclaredFields());
		
		while (!(currentClass = currentClass.getSuperclass()).equals(BaseActivity.class))
		{
			fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
		}
		
		return fields.toArray(new Field[fields.size()]);
	}

	/**
	 * The same as findViewById, but saves you from casting the result to a specified type.
	 */
	public <T> T findViewById(int id, Class<T> tClass) {
		return tClass.cast(findViewById(id));
	}
	
	/**
	 * Easy method to show a Toast message
	 */
	public void showToast(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Easy method to show a Toast message using a string resource
	 */
	public void showToast(int resource)
	{
		Toast.makeText(this, getString(resource), Toast.LENGTH_LONG).show();
	}
	
	public void startActivity(Class<? extends Activity> activity)
	{
		this.startActivity(activity, null);
	}
	
	public void startActivity(Class<? extends Activity> activity, String key, Serializable value)
	{
		Bundle bundle = new Bundle();
		bundle.putSerializable(key, value);
		this.startActivity(activity, bundle);
	}
	
	public void startActivity(Class<? extends Activity> activity, Bundle extras)
	{
		Intent intent = new Intent(this, activity);
		intent.putExtras(extras);
		this.startActivity(intent);
	}

}