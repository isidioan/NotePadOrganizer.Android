package com.isidioan.notepad.isidorosioannou.notepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by akis on 13/5/2017.
 */

public class DatabaseAdapt extends SQLiteOpenHelper {

    private static final String DATABASE = "notepadDB";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase sqlDB;
    private Context context;
    private static final String NOTE_TABLE = "noteApp";
    private static final String TODOLIST_TABLE = "todolistApp";
    private static final String CAMERANOTE_TABLE = "cameranoteApp";
    private static final String LOCATION_TABLE = "locationApp";
    public static final String NOTECOLUMN_ID = "_id";
    public static final String NOTECOLUMN_TITLE = "title";
    public static final String NOTECOLUMN_SUMMARY = "summary";
    public static final String NOTECOLUMN_DATE="date";
    public static final String TODO_ID= "_id";
    public static final String TODO_TEXT="todoText";
    public static final String TODO_CHECKED="todoCheck";
    public static final String CAMERA_ID="_id";
    public static final String CAMERA_TITLE="cameraTitle";
    public static final String CAMERA_DESC="cameraDesc";
    public static final String CAMERA_PATH = "cameraPath";
    public static final String LOCATION_ID = "_id";
    public static final String LOCATION_TITLE = "locationTitle";
    public static final String LOCATION_ADDRESS = "locationAddress";
    public static final String LOCATION_LAT = "locationLat";
    public static final String LOCATION_LONG= "locationLong";
    private static final String NOTETABLE_CREATE = "create table " + NOTE_TABLE + " ( "
            + NOTECOLUMN_ID + " integer primary key autoincrement, "
            + NOTECOLUMN_TITLE + " text not null, "
            + NOTECOLUMN_DATE + " text, "
            + NOTECOLUMN_SUMMARY + " text not null);";
    private static final String TODOLIST_CREATE = "create table " + TODOLIST_TABLE + " ( "
            + TODO_ID + " integer primary key autoincrement, "
            + TODO_TEXT + " text not null, "
            + TODO_CHECKED + " integer not null);";
    private static final String CAMERANOTE_CREATE = "create table " + CAMERANOTE_TABLE + " ( "
            + CAMERA_ID + " integer primary key autoincrement, "
            + CAMERA_TITLE + " text not null, "
            + CAMERA_DESC + " text not null, "
            + CAMERA_PATH + " BLOB );";
    private static final String LOCATION_CREATE = "create table " + LOCATION_TABLE + " ( "
            + LOCATION_ID + " integer primary key autoincrement, "
            + LOCATION_TITLE + " text not null, "
            + LOCATION_ADDRESS + " text, "
            + LOCATION_LAT + " real, "
            + LOCATION_LONG + " real);";


    public DatabaseAdapt(Context context) {
        super(context, DATABASE, null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NOTETABLE_CREATE);
        db.execSQL(TODOLIST_CREATE);
        db.execSQL(CAMERANOTE_CREATE);
        db.execSQL(LOCATION_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + NOTE_TABLE);
        db.execSQL("Drop table if exists " + TODOLIST_TABLE);
        db.execSQL("Drop table if exists " + CAMERANOTE_TABLE);
        db.execSQL("Drop table if exists " + LOCATION_TABLE);
        onCreate(db);
    }

    public void open() {
       try {
           sqlDB = this.getWritableDatabase();
       }
       catch (SQLiteException e){
           Toast toast = Toast.makeText(this.context,"Database unavailable",Toast.LENGTH_SHORT );
       }

    }

    public long createNote (String title , String summary, String date){

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTECOLUMN_TITLE,title);
        contentValues.put(NOTECOLUMN_SUMMARY,summary);
        contentValues.put(NOTECOLUMN_DATE, date);
        long insertId = sqlDB.insert(NOTE_TABLE,null,contentValues);
        return  insertId;
    }

    public long createTask (String text, int status){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODO_TEXT,text);
        contentValues.put(TODO_CHECKED,status);
        long insertId = sqlDB.insert(TODOLIST_TABLE,null,contentValues);
        return insertId;
    }

    public long createLocation (String title, String address, double lat,double longit){
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATION_TITLE,title);
        contentValues.put(LOCATION_ADDRESS,address);
        contentValues.put(LOCATION_LAT,lat);
        contentValues.put(LOCATION_LONG,longit);
        long insert =sqlDB.insert(LOCATION_TABLE,null,contentValues);
        return insert;
    }

    public long createCamera(DataImage image){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CAMERA_TITLE,image.getTitle());
        contentValues.put(CAMERA_DESC,image.getDesc());
        contentValues.put(CAMERA_PATH,image.getPath());
        long insert= sqlDB.insert(CAMERANOTE_TABLE,null,contentValues);
        return insert;
    }

    public int updateNote(int id, String title , String summary , String date){

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTECOLUMN_TITLE,title);
        contentValues.put(NOTECOLUMN_SUMMARY,summary);
        contentValues.put(NOTECOLUMN_DATE,date);
        int rows = sqlDB.update(NOTE_TABLE,contentValues,"_id = ?",new String[] {Integer.toString(id)});
      return rows;
    }

    public int updateTask(int id, int status){

        ContentValues contentValues = new ContentValues();
        contentValues.put(TODO_CHECKED,status);
        int row = sqlDB.update(TODOLIST_TABLE,contentValues,"_id = ?",new String[] {Integer.toString(id)});
        return  row;
    }

    public int updateTitleTask(int id, String title){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TODO_TEXT,title);
        int row = sqlDB.update(TODOLIST_TABLE,contentValues,"_id = ?",new String[]{Integer.toString(id)});
        return row;
    }

    public int updateLocation(int id, String title, String address, double latitude,double longitude){
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATION_TITLE,title);
        contentValues.put(LOCATION_ADDRESS,address);
        contentValues.put(LOCATION_LAT,latitude);
        contentValues.put(LOCATION_LONG,longitude);
        int row = sqlDB.update(LOCATION_TABLE,contentValues,"_id = ?",new String[] {Integer.toString(id)});
        return row;
    }
/*
    public int updateCamera(){

    }
*/
    public int deleteNote (int id){

        int rows = sqlDB.delete(NOTE_TABLE,"_id = ?", new String[] {Integer.toString(id)});
        return rows;
    }

    public int deleteTask (int id){

        int row = sqlDB.delete(TODOLIST_TABLE,"_id = ?", new String[] {Integer.toString(id)});
        return row;
    }

    public int deleteCamera(int id){
         int row = sqlDB.delete(CAMERANOTE_TABLE,"_id = ?",new String[] {Integer.toString(id)});
         return row;
    }

    public int deleteLocation(int id){
        int row = sqlDB.delete(LOCATION_TABLE,"_id = ?",new String[]{Integer.toString(id)});
        return row;
    }

    public Cursor loadAllNotes (){

        Cursor cursor = sqlDB.query(NOTE_TABLE, new String[]{NOTECOLUMN_ID,NOTECOLUMN_TITLE ,NOTECOLUMN_DATE, NOTECOLUMN_SUMMARY},null,null,null,null,null);
        return cursor;
    }
    public Cursor loadLocationTitles(){
        Cursor cursor = sqlDB.query(LOCATION_TABLE,new String[]{LOCATION_ID,LOCATION_TITLE,LOCATION_ADDRESS,LOCATION_LAT,LOCATION_LONG},null,null,null,null,null);
        cursor.moveToFirst();
        return cursor;
    }

    public ArrayList<DataImage> loadAllImages(){
        ArrayList<DataImage> imageList= new ArrayList<>();
        Cursor cursor = sqlDB.query(CAMERANOTE_TABLE,new String[]{CAMERA_ID,CAMERA_TITLE,CAMERA_DESC,CAMERA_PATH},null,null,null,null,null);
            for(cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()){
            DataImage image = cursorToImage(cursor);
            imageList.add(image);
        }
        cursor.close();
        return imageList;
    }

    public Cursor loadAllTasks (){

        Cursor cursor = sqlDB.query(TODOLIST_TABLE,new String[]{TODO_ID,TODO_TEXT,TODO_CHECKED},null,null,null,null,null);
        return cursor;
    }

    public DataImage getOneCamera(int id){
        Cursor cursor =sqlDB.query(CAMERANOTE_TABLE,new String[]{CAMERA_ID,CAMERA_TITLE,CAMERA_DESC,CAMERA_PATH},"_id = ?",new String[]{Integer.toString(id)},null,null,null);
        cursor.moveToFirst();
        int cid = cursor.getInt(cursor.getColumnIndex(CAMERA_ID));
        String text1 = cursor.getString(cursor.getColumnIndex(CAMERA_TITLE));
        String text2 = cursor.getString(cursor.getColumnIndex(CAMERA_DESC));
        byte [] bytes = cursor.getBlob(cursor.getColumnIndex(CAMERA_PATH));
        DataImage image = new DataImage(cid,text1,text2,bytes);
        return image;
    }

    private DataImage cursorToImage(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(DatabaseAdapt.CAMERA_ID));
        String text = cursor.getString(cursor.getColumnIndex(DatabaseAdapt.CAMERA_TITLE));
        String desc = cursor.getString(cursor.getColumnIndex(DatabaseAdapt.CAMERA_DESC));
        byte [] path = cursor.getBlob(cursor.getColumnIndex(DatabaseAdapt.CAMERA_PATH));
        DataImage image= new DataImage(id,text,desc,path);
        return image;
    }

}
