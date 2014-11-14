package raele.concurseiro.persistence;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import raele.concurseiro.entity.Entity;
import raele.concurseiro.entity.Study;
import raele.concurseiro.entity.Subject;
import raele.concurseiro.entity.Topic;
import raele.util.android.log.Ident;
import android.content.ContentValues;
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
		
		public QueryBuilder table(Class<? extends Entity> entityClass) {
			if (!this.entities.isEmpty()) {
				this.entities.add(", ");
			}
			this.entities.add(DH.tableName(entityClass));
			return this;
		}
		
		public QueryBuilder table(String tableName, String alias) {
			if (!this.entities.isEmpty()) {
				this.entities.add(", ");
			}
			this.entities.add(tableName + " " + alias);
			return this;
		}
		
		public QueryBuilder where(String column, String opperation, Object value) {
			this.params.append(column);
			this.params.append(opperation);
			this.params.append("'" + value + "'");
			return this;
		}
		
		public QueryBuilder and(String column, String opperation, Object value) {
			this.params.append(DH.AND);
			this.params.append(column);
			this.params.append(opperation);
			this.params.append("'" + value + "'");
			return this;
		}
		
		public QueryBuilder or(String column, String opperation, Object value) {
			this.params.append(DH.OR);
			this.params.append(column);
			this.params.append(opperation);
			this.params.append("'" + value + "'");
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
		
		/**
		 * Don't forget to close the Cursor!!
		 */
		public Cursor query() {
			Ident.begin();
			
			Ident.log("Building sql statement.");
			String sql = this.build();
			
			Ident.log("Querying database.");
			SQLiteDatabase database = DH.this.getReadableDatabase();
			Cursor cursor = database.rawQuery(sql, null);
			
			Ident.log("Got " + cursor.getCount() + " results.");
			Ident.end();

			DH.this.close();
			
			return cursor;
		} 
		
		public <T extends Entity> T querySingle(Class<T> entityClass) {
			Ident.begin();
			
			Ident.log("Querying...");
			Cursor cursor = this.query();

			Ident.log("Parsing the results...");
			T result = null;
			try {
				if (cursor.moveToNext()) {
					result = entityClass.newInstance();
					result.load(cursor);
					Ident.log("Got " + result);
				} else {
					Ident.log("Got nothing.");
				}
			} catch (InstantiationException e) {
				Ident.error("An error occured while parsing query results: " + e.toString());
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				Ident.error("An error occured while parsing query results: " + e.toString());
				throw new RuntimeException(e);
			} finally {
				cursor.close();
			}
			
			Ident.log("Returning " + result);
			Ident.end();
			
			return result;
		}
		
		public <T extends Entity> List<T> queryMultiple(Class<T> entityClass) {
			Ident.begin();
			
			Ident.log("Querying...");
			Cursor cursor = this.query();

			Ident.log("Parsing the results...");
			List<T> result = new ArrayList<T>(cursor.getCount());
			try {
				while (cursor.moveToNext())
				{
					T entity = entityClass.newInstance();
					entity.load(cursor);
					result.add(entity);
				}
			} catch (InstantiationException e) {
				Ident.error("An error occured while parsing query results: " + e.toString());
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				Ident.error("An error occured while parsing query results: " + e.toString());
				throw new RuntimeException(e);
			} finally {
				cursor.close();
			}
			
			Ident.log("Returning " + result.size() + " objects of type " + entityClass.getSimpleName());
			Ident.end();
			
			return result;
		}
		
	}

	// SQL commands
	private static final String SQL_CREATE = "CREATE TABLE %s (%s)";
	private static final String SQL_DELETE = "DROP TABLE IF EXISTS %s";
	
	// SQLite types
	public static final String PRIMARY_KEY = " PRIMARY KEY AUTOINCREMENT ";
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
    
    public void save(Entity entity) {
		Ident.begin();
		
		ContentValues values = entity.unload(new ContentValues());
		
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try {
			if (entity.getId() == null) {
				Ident.log("Inserting entity " + entity);
				long id = db.insert(DH.tableName(entity.getClass()), null, values);
				entity.setId(id);
				Ident.log("Entity inserted with id " + id);
			} else {
				Ident.log("Updating entity " + entity);
				db.update(DH.tableName(entity.getClass()), values, "id = ?", new String[] {""+entity.getId()});
			}
			db.setTransactionSuccessful();
			
			Ident.log("Transaction commited.");
		} catch (Exception e) {
			Ident.error("Rolling back: " + e.toString());
		} finally {
			db.endTransaction();
			db.close();
		}

		Ident.end();
    }
    
    public void delete(Entity entity) {
    	Ident.begin();
    	
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try {
			db.delete(DH.tableName(entity.getClass()), "id = ?", new String[] {""+entity.getId()});
			db.setTransactionSuccessful();
			
			Ident.log("Transaction commited successfully.");
		} catch (Exception e) {
			Ident.error("Rolling back transaction because: " + e);
		} finally {
			db.endTransaction();
			db.close();
		}
    	
    	Ident.end();
    }
    
    public static String tableName(Class<? extends Entity> entityClass) {
		return entityClass.getSimpleName();
	}

	public void onCreate(SQLiteDatabase db) {
    	Ident.begin();
    	try {
	        db.execSQL(String.format(SQL_CREATE, DH.tableName(Study.class), Study.TYPES));
	        db.execSQL(String.format(SQL_CREATE, DH.tableName(Subject.class), Subject.TYPES));
	        db.execSQL(String.format(SQL_CREATE, DH.tableName(Topic.class), Topic.TYPES));
    	} catch (SQLiteException e) {
    		Ident.error(e.toString());
    	}
        Ident.end();
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	Ident.begin();
    	try {
	    	db.execSQL(String.format(SQL_DELETE, DH.tableName(Study.class)));
	    	db.execSQL(String.format(SQL_DELETE, DH.tableName(Subject.class)));
	    	db.execSQL(String.format(SQL_DELETE, DH.tableName(Topic.class)));
	        this.onCreate(db);
    	} catch (SQLiteException e) {
    		Ident.error(e.toString());
    	}
        Ident.end();
    }
    
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Ident.begin();
		try {
	    	this.onUpgrade(db, oldVersion, newVersion);
    	} catch (SQLiteException e) {
    		Ident.error(e.toString());
    	}
    	Ident.end();
    }
}