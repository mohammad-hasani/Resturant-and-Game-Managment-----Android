package com.nutrica.client.Tools;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nutrica.client.G;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by me on 12/7/2016.
 */

public class Database {

    SQLiteDatabase db = null;

    private void openDatabase()
    {
        this.db = SQLiteDatabase.openOrCreateDatabase(G.FILE_DATABASE, null);
    }

    public Cursor getData(String query) {
        openDatabase();

        Cursor resultSet = this.db.rawQuery(query, null);
        resultSet.moveToFirst();
        return resultSet;
    }

    public void close()
    {
        this.db.close();
    }

}
