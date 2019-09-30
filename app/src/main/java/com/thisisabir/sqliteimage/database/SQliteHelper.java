package com.thisisabir.sqliteimage.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class SQliteHelper extends SQLiteOpenHelper {


    public SQliteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void queryData(String sql)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    // Insert data method
    public void insertdata(String name,String price, byte[] image)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO FOOD VALUES (NULL, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindString(2, price);
        statement.bindBlob(3, image);

        statement.executeInsert();
    }

    // Get data method
    public Cursor getdata(String sql)
    {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);

    }

    // update data method
    public void updateData(String name, String price, byte[] image, int id)
    {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "UPDATE FOOD SET name = ?, price = ?, image = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, name);
        statement.bindString(2, price);
        statement.bindBlob(3, image);
        statement.bindDouble(4, (double)id);
        statement.execute();
        database.close();


    }

    // delete data method
    public void deleteData(int id)
    {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM FOOD WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();;
        statement.bindDouble(1, (double)id);
        statement.execute();
        database.close();
    }





}
