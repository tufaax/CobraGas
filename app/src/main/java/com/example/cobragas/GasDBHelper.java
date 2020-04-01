package com.example.cobragas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class GasDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "stations.db";
    private static final int DATABASE_VERSION = 3;

    // Database creation sql statement
    private static final String CREATE_TABLE_STATIONS =
            "create table stations (_id integer primary key autoincrement, "
                    + "stationname text not null, streetaddress text, "
                    + "city text, state text, zipcode text, "
                    + "phonenumber text, rprice text, "
                    + "pprice text, dprice text, distance text);";

    public GasDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_STATIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(GasDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS stations");
        onCreate(db);
    }

    public void close() {
    }


}