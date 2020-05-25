package n3vashis.example.discussrater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Database class that holds all the books
public class DbClass extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    public static final String BOOKS_TABLE_NAME = "books";
    public static final String BOOKS_COLUMN_ID = "id";
    public static final String BOOKS_COLUMN_NAME = "name";
    public static final String BOOKS_COLUMN_AUTHOR = "author";
    public static final String BOOKS_COLUMN_VOTE = "vote";
    public static final String BOOKS_COLUMN_IMAGE = "image";



    private static Context c;

    public DbClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        c = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table books " +
                "(id integer primary key, name text,author text,vote integer,image string)"
        );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS books");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insert(String title,String author, int votes,String image){
        SQLiteDatabase sq = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", title);
        contentValues.put("author", author);
        contentValues.put("vote", votes);
        contentValues.put("image",image);
        sq.insert(BOOKS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from books where id="+id+"", null );
        return res;
    }

    //to be called by class to put all the books into a Map key = title, value = image(string)
    public Map<String,String> loadExisting() {
        Map<String,String> array_list = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from books", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String key = (res.getString(res.getColumnIndex(BOOKS_COLUMN_NAME)));
            String value = (res.getString(res.getColumnIndex(BOOKS_COLUMN_IMAGE)));
            array_list.put(key,value);
            res.moveToNext();
        }
        return array_list;
    }

    //update the book's image
    public boolean updateContact (Integer id, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("image",image);
        db.update("books", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        //System.out.println("the id : " + id);
        return true;
    }

    //update the book's author
    public boolean updateAuthor (Integer id, String author) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("author",author);
        db.update("books", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        //System.out.println("the id : " + id);
        return true;
    }
}
