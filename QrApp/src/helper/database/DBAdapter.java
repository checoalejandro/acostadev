package helper.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_USERID = "userid";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_NAME = "name";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_TYPE = "type";
	
	

	private static final String TAG = "DBAdapter";
	private static final String DATABASE_NAME = "socialgo";
	private static final String DATABASE_TABLE = "users";
	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "+DATABASE_TABLE+" (_id integer primary key autoincrement, "
														+ "userid integer not null, "
														+ "username text not null, "
														+ "name text not null, "
														+ "password text not null, "
														+ "email text not null);";
	
	public static final String KEY_CATID = "catid";
	public static final String KEY_CATNAME = "catname";
	public static final String KEY_PARENT = "parent";
	
	private static final String DATABASE_TABLE_CAT = "categories";
	private static final String DATABASE_CREATE_CAT = "CREATE TABLE IF NOT EXISTS "+DATABASE_TABLE_CAT+" (_id integer primary key autoincrement, "
														+ "catid integer not null, "
														+ "catname text not null);";
	
	private static final String DATABASE_TABLE_SUBCAT = "subcategories";
	private static final String DATABASE_CREATE_SUBCAT = "CREATE TABLE IF NOT EXISTS "+DATABASE_TABLE_SUBCAT+" (_id integer primary key autoincrement, "
														+ "catid integer not null, "
														+ "catname text not null," +
														"  parent integer not null);";
	
	public static final String KEY_BESTLISTID = "bestlistid";
	public static final String KEY_LISTNAME = "listname";
	public static final String KEY_DESC = "description";
	
	private static final String DATABASE_TABLE_BESTLISTS = "bestlists";
	private static final String DATABASE_CREATE_BESTLISTS = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_BESTLISTS + " (_id integer primary key autoincrement, "
														+ "bestlistid integer not null, "
														+ "listname text not null,"
														+ "description text not null);";
	
	public static final String KEY_BIZID = "bizid";
	
	private static final String DATABASE_TABLE_FAVORITES = "favorites";
	private static final String DATABASE_CREATE_FAVORITES = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_FAVORITES +" (_id integer primary key autoincrement, "
														+ "bizid integer not null, "
														+ "name text not null,"
														+ "catid integer not null);";
	
	public static final String KEY_CITYID = "cityid";
	
	private static final String DATABASE_TABLE_CITIES = "cities";
	private static final String DATABASE_CREATE_CITIES = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_CITIES +" (_id integer primary key autoincrement, "
														+ "name text not null," +
														  "cityid integer not null," +
														  "lat double not null," +
														  "lng double not null);";
	
	public static final String KEY_LNG = "lng";
	public static final String KEY_LAT = "lat";
	public static final String KEY_ID = "id";
	
	private static final String DATABASE_TABLE_ZONES = "zones";
	private static final String DATABASE_CREATE_ZONES = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_ZONES +" (_id integer primary key autoincrement, "
														+ "name text not null,"
														+ "lng double not null,"
														+ "lat double not null," +
														  "id int null," +
														  "type text not null);";
	
	private static final String DATABASE_TABLE_TMPZONES = "tmpzone";
	private static final String DATABASE_CREATE_TMPZONES = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_TMPZONES +" (_id integer primary key autoincrement, "
														+ "lng double not null,"
														+ "lat double not null);";
	
	private static final String DATABASE_TABLE_STREETS = "streets";
	private static final String DATABASE_CREATE_STREETS = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_STREETS +" (_id integer primary key autoincrement, "
														+ "name text not null,"
														+ "id integer not null);";
	
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBAdapter(Context ctx)
	{
		this.context = ctx;
		DBHelper = DatabaseHelper.getInstance(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		private static DatabaseHelper sInstance = null;
		
		DatabaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(DATABASE_CREATE);
			db.execSQL(DATABASE_CREATE_CAT);
			db.execSQL(DATABASE_CREATE_SUBCAT);
			db.execSQL(DATABASE_CREATE_BESTLISTS);
			db.execSQL(DATABASE_CREATE_FAVORITES);
			db.execSQL(DATABASE_CREATE_CITIES);
			db.execSQL(DATABASE_CREATE_ZONES);
			db.execSQL(DATABASE_CREATE_TMPZONES);
			db.execSQL(DATABASE_CREATE_STREETS);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(TAG, "Upgrading helper.database from version " + oldVersion
			+ " to "
			+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_BESTLISTS);
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_CAT);
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_SUBCAT);
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_FAVORITES);
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_CITIES);
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_ZONES);
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_STREETS);
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_TMPZONES);
			onCreate(db);
		}
		
		public static DatabaseHelper getInstance(Context context){
			if(sInstance == null){
				sInstance = new DatabaseHelper(context);
			}
			
			return sInstance;
		}
	}
	
	// create methods to update, delete, create in the data base
	
	//---opens the helper.database---
	public DBAdapter open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	//---closes the helper.database---
	public void close()
	{
		DBHelper.close();
	}
	
	//---insert a zone into the helper.database---
	public long insertZone(String name, double lat, double lng, int id,String type)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_LAT, lat);
		initialValues.put(KEY_LNG, lng);
		initialValues.put(KEY_ID, id);
		initialValues.put(KEY_TYPE, type);
		return db.insert(DATABASE_TABLE_ZONES, null, initialValues);
	}
	//---insert a tempzone into the helper.database---
	public long insertTMPZone(double lat, double lng)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ROWID, 1);
		initialValues.put(KEY_LAT, lat);
		initialValues.put(KEY_LNG, lng);
		return db.insert(DATABASE_TABLE_TMPZONES, null, initialValues);
	}
	//---insert a zone into the helper.database---
	public long insertStreet(String name, int id)
		{
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_NAME, name);
			initialValues.put(KEY_ID, id);
			return db.insert(DATABASE_TABLE_STREETS, null, initialValues);
		}
	
	//---insert a user into the helper.database---
	public long insertCity(String name, Integer cityid, double lat, double lng)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_CITYID, cityid);
		initialValues.put(KEY_LAT, lat);
		initialValues.put(KEY_LNG, lng);
		return db.insert(DATABASE_TABLE_CITIES, null, initialValues);
	}
	
	//---insert a user into the helper.database---
	public long insertUser(Integer userid, String username, String name, String password, String email)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_USERID, userid);
		initialValues.put(KEY_USERNAME, username);
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_PASSWORD, password);
		initialValues.put(KEY_EMAIL, email);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	//---deletes a particular title---
	public boolean deleteUser(long rowId)
	{
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	//---deletes a particular title---
	public boolean deleteCity(long rowId)
	{
		return db.delete(DATABASE_TABLE_CITIES, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public boolean deleteStreets(){
		return db.delete(DATABASE_TABLE_STREETS, KEY_ROWID + "!= -1" , null) > 0;
	}
	
	public boolean deleteLists()
	{
		return db.delete(DATABASE_TABLE_BESTLISTS, KEY_BESTLISTID + "> 0 ", null) > 0;
	}
	
	public boolean deleteFavorites(){
		return db.delete(DATABASE_TABLE_FAVORITES, KEY_BIZID + "> 0 ", null) > 0;
	}
	
	public boolean deleteFavorite(long bizid){
		return db.delete(DATABASE_TABLE_FAVORITES, KEY_BIZID + "=" + bizid, null) > 0;
	}
	public boolean deleteCities(){
		return db.delete(DATABASE_TABLE_CITIES, KEY_CITYID + ">= 0 ", null) > 0;
	}
	public boolean deleteZones(){
		return db.delete(DATABASE_TABLE_ZONES, KEY_ID + ">= 0 ", null) > 0;
	}
	public boolean deleteZone(String name){
		return db.delete(DATABASE_TABLE_ZONES, KEY_NAME + "= '" + name + "'" , null) > 0;
	}
	public boolean deleteTMPZone(){
		return db.delete(DATABASE_TABLE_TMPZONES, KEY_ROWID + " >= 0" , null) > 0;
	}
	//---retrieves all the zones---
	public Cursor getAllZones()
	{
		Cursor mCursor = db.query(true,DATABASE_TABLE_ZONES, new String[] {
														KEY_ROWID,
														KEY_NAME,
														KEY_LAT,
														KEY_LNG,
														KEY_ID,
														KEY_TYPE
													},
													null,
													null,
													null,
													null,
													null,
													null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		
	}
	//---retrieves all the users---
	public Cursor getAllUsers()
	{
		Cursor mCursor = db.query(true,DATABASE_TABLE, new String[] {
														KEY_ROWID,
														KEY_USERID,
														KEY_USERNAME,
														KEY_NAME,
														KEY_PASSWORD,
														KEY_EMAIL
													},
													null,
													null,
													null,
													null,
													null,
													null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		
	}
	//---retrieves all the streets---
	public Cursor getAllStreets()
	{
		Cursor mCursor = db.query(true,DATABASE_TABLE_STREETS, new String[] {
														KEY_ID,
														KEY_NAME
													},
													null,
													null,
													null,
													null,
													null,
													null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		
	}
	//---retrieves a particular street---
	public Cursor getStreet(long id) throws SQLException
	{
		Cursor mCursor = db.query(true, DATABASE_TABLE_STREETS, new String[] {
														KEY_NAME,
														KEY_ID
													},
													KEY_ID + "=" + id,
													null,
													null,
													null,
													null,
													null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	//---retrieves a temporary zone---
	public Cursor getTmpZone() throws SQLException
	{
		Cursor mCursor = db.query(true, DATABASE_TABLE_TMPZONES, new String[] {
													KEY_LAT,
													KEY_LNG
													},
													null,
													null,
													null,
													null,
													null,
													null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	//---retrieves a particular user---
	public Cursor getUser(long rowId) throws SQLException
	{
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
														KEY_ROWID,
														KEY_USERID,
														KEY_USERNAME,
														KEY_NAME,
														KEY_PASSWORD,
														KEY_EMAIL
													},
													KEY_ROWID + "=" + rowId,
													null,
													null,
													null,
													null,
													null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	//---retrieves a parent id from a subcategory---
	public Cursor getParentID(long parId) throws SQLException
	{
		Cursor mCursor = db.query(true, DATABASE_TABLE_SUBCAT, new String[] {
														KEY_PARENT
													},
													KEY_CATID + "=" + parId,
													null,
													null,
													null,
													null,
													null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor getCity() throws SQLException
	{
		Cursor mCursor = db.query(true, DATABASE_TABLE_CITIES, new String[] {
														KEY_ROWID,
														KEY_NAME,
														KEY_CITYID,
														KEY_LAT,
														KEY_LNG
													},
													null,
													null,
													null,
													null,
													null,
													null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	//---updates a title---
	public boolean updateUser(long rowId, Integer userid, String username, String name, String password, String email)
	{
		ContentValues args = new ContentValues();
	
		args.put(KEY_USERID, userid);
		args.put(KEY_USERNAME, username);
		args.put(KEY_NAME, name);
		args.put(KEY_PASSWORD, password);
		args.put(KEY_EMAIL, email);
		
		return db.update(DATABASE_TABLE, args,
		KEY_ROWID + "=" + rowId, null) > 0;
	}
	//---updates a title---
	public boolean updateTmpZone(double lat, double lng)
	{
		ContentValues args = new ContentValues();
	
		args.put(KEY_LAT, lat);
		args.put(KEY_LNG, lng);
		
		return db.update(DATABASE_TABLE_TMPZONES, args,
		KEY_ROWID + "= " + KEY_ROWID, null) > 0;
	}
	
	
//	Categories
	
	public Cursor getAllCategories()
	{
		Cursor mCursor = db.query(true,DATABASE_TABLE_CAT, new String[] {
														KEY_ROWID,
														KEY_CATID,
														KEY_CATNAME
													},
													null,
													null,
													null,
													null,
													null,
													null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		
	}
	
	public Cursor getAllSubCategories()
	{
		Cursor mCursor = db.query(true,DATABASE_TABLE_SUBCAT, new String[] {
														KEY_ROWID,
														KEY_CATID,
														KEY_CATNAME,
														KEY_PARENT
													},
													null,
													null,
													null,
													null,
													null,
													null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		
	}
	
	public Cursor getAllBestLists()
	{
		Cursor mCursor = db.query(true,DATABASE_TABLE_BESTLISTS, new String[] {
														KEY_ROWID,
														KEY_BESTLISTID,
														KEY_LISTNAME,
														KEY_DESC
													},
													null,
													null,
													null,
													null,
													null,
													null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		
	}
	
	public Cursor getAllFavorites()
	{
		Cursor mCursor = db.query(true,DATABASE_TABLE_FAVORITES, new String[] {
														KEY_ROWID,
														KEY_BIZID,
														KEY_NAME,
														KEY_CATID
													},
													null,
													null,
													null,
													null,
													null,
													null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		
	}
	public Cursor getFavorite(int bizid)
	{
		Cursor mCursor = db.query(true,DATABASE_TABLE_FAVORITES, new String[] {
														KEY_ROWID,
														KEY_BIZID,
														KEY_NAME,
														KEY_CATID
													},
													KEY_BIZID + " = " + bizid,
													null,
													null,
													null,
													null,
													null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		
	}
	
	public long insertCategory(Integer catid, String catname)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CATID, catid);
		initialValues.put(KEY_CATNAME, catname);
		return db.insert(DATABASE_TABLE_CAT, null, initialValues);
	}
	
	public long insertSubCategory(Integer catid, String catname, Integer parent)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CATID, catid);
		initialValues.put(KEY_CATNAME, catname);
		initialValues.put(KEY_PARENT, parent);
		return db.insert(DATABASE_TABLE_SUBCAT, null, initialValues);
	}
	
	public long insertBestList(Integer bestlistid, String listname, String description){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_BESTLISTID, bestlistid);
		initialValues.put(KEY_LISTNAME, listname);
		initialValues.put(KEY_DESC, description);
		return db.insert(DATABASE_TABLE_BESTLISTS, null, initialValues);
	}
	
	public long insertFavorite(Integer bizid, Integer catid,String name){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_BIZID, bizid);
		initialValues.put(KEY_CATID, catid);
		initialValues.put(KEY_NAME, name);
		return db.insert(DATABASE_TABLE_FAVORITES, null, initialValues);
	}
	
	public Cursor getCategories(int cat_id){
		
		String sql = "SELECT * FROM " + DATABASE_TABLE_SUBCAT + " WHERE " + KEY_PARENT + " = " + cat_id;
		
		return this.db.rawQuery(sql, null);
		
	}
	
	public Cursor getBestList(int bestlistid){
		String sql = "SELECT * FROM " + DATABASE_TABLE_BESTLISTS + " WHERE " + KEY_BESTLISTID + " = " + bestlistid;
		
		return this.db.rawQuery(sql, null);
	}
	
	public Cursor getCatName(int cat_id){
		
		String[] tableColumns = new String[] {
			    "catname"
			};
			String whereClause = "catid = ?";
			String[] whereArgs = new String[] {
			    "" + cat_id
			};
			
			Cursor c = db.query("subcategories", tableColumns, whereClause, whereArgs,
			        null, null, null);
			
			return c;

	}
}
