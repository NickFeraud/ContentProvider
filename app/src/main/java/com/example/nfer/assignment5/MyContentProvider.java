package com.example.nfer.assignment5;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.Toast;

import java.util.NavigableMap;

public class MyContentProvider extends ContentProvider {
    public final static String DBNAME = "UserDatabase";
    public final static String TNAME  =        "Users";
    public final static String ID     =           "id";
    public final static String NAME   =         "name";
    public final static String GENDER =       "gender";
    public final static String EMAIL  =        "email";
    public final static String CODE   =         "code";
    public final static String DEPT   =   "department";
    public final static String ACCESS =       "access";

    protected MyDBHelper mOpenHelper;

    public final static String SQL_CREATE_MAIN =
            "CREATE TABLE" + TNAME + " ( " +
                    ID + " TEXT PRIMARY KEY, " +
                    NAME   + " TEXT, "         +
                    GENDER + " TEXT, "         +
                    EMAIL  + " TEXT, "         +
                    CODE   + " TEXT, "      +
                    DEPT   + " TEXT, "         +
                    ACCESS + " INTEGER )"      ;

    public static final Uri CONTENT_URI =
            Uri.parse("content://com.example.nfer.assignment5.provider/" + TNAME);

    protected static final class MyDBHelper extends SQLiteOpenHelper{

        MyDBHelper(Context context){
            super(context, DBNAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(SQL_CREATE_MAIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2){

        }
    }

    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mOpenHelper.getWritableDatabase().delete(TNAME, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
            String id    =  values.getAsString("empIdVal")   ;
            String name  =  values.getAsString("nameVal")    ;
            String gend  =  values.getAsString("genderVal")  ;
            String email =  values.getAsString("emailVal")   ;
            String code  =  values.getAsString("codeVal")    ;
            String dept  =  values.getAsString("deptVale")   ;
            String accs  =  values.getAsString("adAccess")   ;

            long dbid  = mOpenHelper.getWritableDatabase().insert(TNAME, null, values);

            Toast.makeText(getContext(), "Added Item to Database -- sent from DB Class",
                    Toast.LENGTH_SHORT).show();
            return Uri.withAppendedPath(CONTENT_URI, ""+dbid);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.

        mOpenHelper = new MyDBHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return mOpenHelper.getReadableDatabase().query(TNAME, projection, selection, selectionArgs,
                null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return mOpenHelper.getWritableDatabase().update(TNAME, values, selection, selectionArgs);
    }
}
