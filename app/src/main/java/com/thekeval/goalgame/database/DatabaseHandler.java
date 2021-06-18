package com.thekeval.goalgame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thekeval.goalgame.Model.PlayerModel;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "GoalGame";
    private static final String TABLE_NAME = "Players";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String SCORE = "score";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_createTable =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        NAME + " TEXT," +
                        PASSWORD + " TEXT," +
                        SCORE + " NUMBER" +
                        ")";

        db.execSQL(query_createTable);
        // addJson("");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );

        // Create tables again
        onCreate(db);
    }

    public boolean addPlayer(PlayerModel player) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, player.name);
        contentValues.put(PASSWORD, player.password);
        contentValues.put(SCORE, player.highestScore);
        long res = db.insert(TABLE_NAME, null, contentValues);
        return  (res != -1);
    }

    public boolean updatePlayer(PlayerModel player) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCORE, player.highestScore);
        long res = db.update(TABLE_NAME, contentValues, NAME+"=?", new String[] {player.name});
        return (res != -1);
    }

    private void addJson(String jSon) {
        String insertQuery =
                "INSERT INTO " + TABLE_NAME + " (json)" +
                        "VALUES (" + jSon + ")";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insertQuery);
    }

    public void updateJson(String jSon) {
        String updateQuery =
                "UPDATE " + TABLE_NAME + " SET json='" + jSon + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(updateQuery);
    }

    public String getJson() {
        String jSon = "";

        String getQuery =
                "SELECT * from " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(getQuery, null);

        if (cursor.moveToFirst()) {
            jSon = cursor.getString(0);
        }

        return jSon;
    }

}
