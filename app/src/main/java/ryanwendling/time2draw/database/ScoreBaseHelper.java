package ryanwendling.time2draw.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by wendlir on 5/7/17.
 */
// A SQLiteOpenHelper is a class designed to get rid of the grunt work of opening a SQLiteDatabase .
public class ScoreBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userDB.db";
    private static final String TABLE_USERS = "users";

    public static final String COLUMN_ID = "image_name";
    public static final String COLUMN_SCORE = "image_data";

    public ScoreBaseHelper(Context context, String name,
                           SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " +
                TABLE_USERS + "("
                + COLUMN_ID + " TEXT," + COLUMN_SCORE
                + " BLOB" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(String name, byte[] image) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, name);
        values.put(COLUMN_SCORE, image);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public byte[] findUser(String username) {
        String query = "Select * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + " =  \"" + username + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        byte[] image;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            image = cursor.getBlob(1);
            cursor.close();
        } else {
            image = null;

        }
        db.close();
        return image;
    }

    public boolean deleteUser(String userId) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + " =  \"" + userId + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String foundArtName = cursor.getString(0);
            db.delete(TABLE_USERS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(foundArtName) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}