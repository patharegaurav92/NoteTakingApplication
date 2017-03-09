package edu.csulb.android.notetakingapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

import static android.R.attr.name;
import static edu.csulb.android.notetakingapplication.TableData.TableInfo.TABLE_NAME;

/**
 * Created by Gaurav on 26-02-2017.
 */

public class DatabaseOperations extends SQLiteOpenHelper {
    public static final int database_version=2;
    public String CREATE_QUERY="CREATE TABLE " + TABLE_NAME
            + " (" + TableData.TableInfo.PHOTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TableData.TableInfo.PHOTO_CAPTION + " TEXT NOT NULL, "
            + TableData.TableInfo.PHOTO_PATH+ " TEXT NOT NULL, "
            + TableData.TableInfo.PHOTO_NUMBER+ " INTEGER"
            + ");";
    public DatabaseOperations(Context context) {
        super(context, TableData.TableInfo.DATABASE_NAME, null, database_version);
        Log.d("Database Operations","Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.d("Database Operations","Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void putInformation(DatabaseOperations dop,String caption,String path){
        SQLiteDatabase sq = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long count = getPhotoCount(dop);
        cv.put(TableData.TableInfo.PHOTO_CAPTION,caption);
        cv.put(TableData.TableInfo.PHOTO_PATH,path);
        cv.put(TableData.TableInfo.PHOTO_NUMBER,(int)(count+1));
        long k = sq.insert(TableData.TableInfo.TABLE_NAME,null,cv);
        Log.d("Database Operations","1 row inserted");

    }
    public Cursor getInformation(DatabaseOperations dop){
        SQLiteDatabase sq = dop.getReadableDatabase();
        String[] columns = {TableData.TableInfo.PHOTO_ID,TableData.TableInfo.PHOTO_CAPTION, TableData.TableInfo.PHOTO_PATH, TableData.TableInfo.PHOTO_NUMBER};
        Cursor CR = sq.query(TABLE_NAME,columns,null,null,null, null,null);
        return CR;

    }
    public void deleteUser(DatabaseOperations dop){
        SQLiteDatabase sq = dop.getWritableDatabase();
        sq.delete(TABLE_NAME,null,null);

    }

    public Cursor getPhotoNumber(DatabaseOperations dop,String photo_caption){
        SQLiteDatabase sq = dop.getReadableDatabase();
        String selection = TableData.TableInfo.PHOTO_CAPTION+" LIKE ?";
        String columns[] = {TableData.TableInfo.PHOTO_NUMBER};
        String args[] = {photo_caption};
        Cursor cr= sq.query(TableData.TableInfo.TABLE_NAME,columns,selection,args,null,null,null);
        return cr;
    }

    public Cursor getPhotoInformation(DatabaseOperations dop,String photo_number){
        SQLiteDatabase sq = dop.getReadableDatabase();
        String selection = TableData.TableInfo.PHOTO_NUMBER+" LIKE ?";
        String columns[] = {TableData.TableInfo.PHOTO_CAPTION, TableData.TableInfo.PHOTO_PATH};
        String args[] = {photo_number};
        Cursor cr= sq.query(TableData.TableInfo.TABLE_NAME,columns,selection,args,null,null,null);
        return cr;
    }
    public long getPhotoCount(DatabaseOperations dop) {
        SQLiteDatabase db = dop.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return cnt;
    }
    public Cursor getPhotoCaption(DatabaseOperations dop){
        SQLiteDatabase sq = dop.getReadableDatabase();
        String[] columns = {TableData.TableInfo.PHOTO_CAPTION};
        Cursor CR = sq.query(TABLE_NAME,columns,null,null,null, null,null);
        return CR;
    }
}
