package raele.concurseiro.persistence;

import java.util.LinkedList;

import raele.concurseiro.entity.Study;
import raele.concurseiro.entity.Subject;
import raele.util.android.log.Ident;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DH = Database Helper
 */
public class DH extends SQLiteOpenHelper {
	
	public class QueryBuilder {
		
		private LinkedList<String> columns;
		private LinkedList<String> entities;
		private StringBuilder params;
		private String orderBy;
		
		public QueryBuilder() {
			this.columns = new LinkedList<String>();
			this.entities = new LinkedList<String>();
			this.params = new StringBuilder();
			this.orderBy = "";
		}
		
		public QueryBuilder column(String column) {
			if (!this.columns.isEmpty()) {
				this.columns.add(", ");
			}
			this.columns.add(column);
			return this;
		}
			
		public QueryBuilder column(String entity, String column) {
			if (!this.columns.isEmpty()) {
				this.columns.add(", ");
			}
			this.columns.add(entity + "." + column);
			return this;
		}
		
		public QueryBuilder table(String tableName) {
			if (!this.entities.isEmpty()) {
				this.entities.add(", ");
			}
			this.entities.add(tableName);
			return this;
		}
		
		public QueryBuilder table(String tableName, String alias) {
			if (!this.entities.isEmpty()) {
				this.entities.add(", ");
			}
			this.entities.add(tableName + " " + alias);
			return this;
		}
		
		public QueryBuilder where(String... params) {
			for (String param : params) {
				this.params.append(param);
			}
			return this;
		}
		
		public QueryBuilder and(String... params) {
			this.params.append(DH.AND);
			for (String param : params) {
				this.params.append(param);
			}
			return this;
		}
		
		public QueryBuilder or(String... params) {
			this.params.append(DH.OR);
			for (String param : params) {
				this.params.append(param);
			}
			return this;
		}
		
		public QueryBuilder orderBy(String column) {
			this.orderBy = column;
			return this;
		}
		
		public QueryBuilder orderBy(String entity, String column) {
			this.orderBy = entity + "." + column;
			return this;
		}
		
		public String build() {
			Ident.begin();
			
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT ");
			if (!this.columns.isEmpty()) {
				for (String column : this.columns) {
					builder.append(column);
				}
			} else {
				builder.append("*");
			}
			builder.append(" FROM ");
			for (String entity : this.entities) {
				builder.append(entity);
			}
			if (!"".equals(this.params.toString())) {
				builder.append(" WHERE ");
				builder.append(this.params.toString());
			}
			if (this.orderBy != null && !"".equals(this.orderBy)) {
				builder.append(" ORDER BY ");
				builder.append(this.orderBy);
			}
			String result = builder.toString();
			
			Ident.log("Sql statement built: " + result);
			Ident.end();
			
			return builder.toString();
		}
		
		public Cursor query() {
			Ident.begin();
			
			Ident.log("Building sql statement.");
			String sql = this.build();
			
			Ident.log("Querying database.");
			SQLiteDatabase database = DH.this.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			
			Ident.log("Got " + cursor.getCount() + " results.");
			Ident.end();
			
			return cursor;
		}
		
	}

	// SQL commands
	private static final String SQL_CREATE = "CREATE TABLE %S (%s)";
	private static final String SQL_DELETE = "DROP TABLE IF EXISTS %s";
	
	// SQLite types
	public static final String PRIMARY_KEY = " primary key autoincrement ";
	public static final String INTEGER = " INTEGER ";
	public static final String STRING = " TEXT ";
	public static final String FLOAT = " REAL ";
	public static final String BINARY = " BLOB ";
	
	// Public database info
	public static final String DATABASE_NAME = "concurseiro.db";
    public static final int DATABASE_VERSION = 4;

    // Query options
    public static final String LIKE = " like ";
    public static final String ANYTHING = "%";
    public static final String MATCH_REGEXP = " REGEXP ";
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
    
    public static String[] columns(String... columns) {
    	return columns;
    }

    public DH(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public QueryBuilder queryBuilder() {
    	return new QueryBuilder();
    }
    
    public void onCreate(SQLiteDatabase db) {
    	Ident.begin();
    	try {
	        db.execSQL(String.format(SQL_CREATE, Study.TABLE, Study.TYPES));
	        db.execSQL(String.format(SQL_CREATE, Subject.TABLE, Subject.TYPES));
    	} catch (SQLiteException e) {
    		Ident.error(e.toString());
    	}
        Ident.end();
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Ident.begin();
    	try {
	    	db.execSQL(String.format(SQL_DELETE, Study.TABLE));
	    	db.execSQL(String.format(SQL_DELETE, Subject.TABLE));
	        this.onCreate(db);
    	} catch (SQLiteException e) {
    		Ident.error(e.toString());
    	}
        Ident.end();
    }
    
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Ident.begin();
		try {
	    	db.execSQL(String.format(SQL_DELETE, Study.TABLE));
	    	db.execSQL(String.format(SQL_DELETE, Subject.TABLE));
	        this.onCreate(db);
    	} catch (SQLiteException e) {
    		Ident.error(e.toString());
    	}
    	Ident.end();
    }
}