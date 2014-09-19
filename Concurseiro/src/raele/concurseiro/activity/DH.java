package raele.concurseiro.activity;

import raele.util.android.Ident;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DH extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "concurseiro.db";
    public static final int DATABASE_VERSION = 4;
    
    public static final String LIKE = " like ";
    public static final String EQUALS = "=";
    public static final String NOT_EQUALS = "<>";
    public static final String LESS_THAN = "<";
    public static final String GREATER_THAN = ">";
    public static final String LESS_EQUALS = "<=";
    public static final String GREATER_EQUALS = ">=";
    public static final String AND = " and ";
    public static final String OR = " or ";
    
    public static String include(String str) {
    	return " like %" + str + "%";
    }
    
    public static String abs(String str) {
    	return " abs(" + str + ")";
    }
    
    public static String length(String str) {
    	return " length(" + str + ")";
    }
    
    public static String lower(String str) {
    	return " lower(" + str + ")";
    }
    
    public static String round(String str) {
    	return " round(" + str + ")";
    }
    
    public static String upper(String str) {
    	return " upper(" + str + ")";
    }
    
    public static final class Study {
    	public static final String TABLE_NAME = "study";
    	public static final String COLUMN_ID = "id";
    	public static final String COLUMN_TIME = "time";
    	
    	private static final String SQL_CREATE =
				"CREATE TABLE " + TABLE_NAME + " (" +
						COLUMN_ID + " integer primary key autoincrement, " +
						COLUMN_TIME + " integer" +
						")";
    	
    	private static final String SQL_DELETE =
    			"DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    
    public static final class Subject {
    	public static final String TABLE_NAME = "subject";
    	public static final String COLUMN_ID = "id";
    	public static final String COLUMN_NAME = "name";
    	
    	private static final String SQL_CREATE =
				"CREATE TABLE " + TABLE_NAME + " (" +
						COLUMN_ID + " integer primary key autoincrement, " +
						COLUMN_NAME + " text" +
						")";
    	
    	private static final String SQL_DELETE =
    			"DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public DH(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
    	Ident.begin();
    	try {
	    	Ident.log(Study.SQL_CREATE);
	        db.execSQL(Study.SQL_CREATE);
	        Ident.log(Subject.SQL_CREATE);
	        db.execSQL(Subject.SQL_CREATE);
    	} catch (SQLiteException e) {
    		Ident.error(e.getMessage());
    	}
        Ident.end();
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Ident.begin();
    	try {
	    	Ident.log(Study.SQL_DELETE);
	    	db.execSQL(Study.SQL_DELETE);
	    	Ident.log(Subject.SQL_DELETE);
	    	db.execSQL(Subject.SQL_DELETE);
	        this.onCreate(db);
    	} catch (SQLiteException e) {
    		Ident.error(e.getMessage());
    	}
        Ident.end();
    }
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Ident.begin();
		try {
			Ident.log(Study.SQL_DELETE);
	    	db.execSQL(Study.SQL_DELETE);
	    	Ident.log(Subject.SQL_DELETE);
	    	db.execSQL(Subject.SQL_DELETE);
	    	this.onCreate(db);
    	} catch (SQLiteException e) {
    		Ident.error(e.getMessage());
    	}
    	Ident.end();
    }
}