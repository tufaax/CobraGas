package com.example.cobragas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class GasDataSource {

    private SQLiteDatabase database;
    private GasDBHelper dbHelper;

    public GasDataSource(Context context) {
        dbHelper = new GasDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    public boolean insertStation(Gas c)
    {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("stationname", c.getStationID());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZipCode());
            initialValues.put("phonenumber", c.getPhoneNumber());
            initialValues.put("rprice", c.getrPrice());
            initialValues.put("pprice", c.getpPrice());
            initialValues.put("dprice", c.getdPrice());

            if(c.getIcon() != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                c.getIcon().compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] photo = baos.toByteArray();
                initialValues.put("contactphoto", photo);
            }

            didSucceed = database.insert("stations", null, initialValues) > 0;

        }
        catch (Exception e) {
            //Do Nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateStation(Gas c)
    {
        boolean didSucceed = false;
        try {
            Long rowId = Long.valueOf(c.getStationID());
            ContentValues updateValues = new ContentValues();

            updateValues.put("stationname", c.getStationID());
            updateValues.put("streetaddress", c.getStreetAddress());
            updateValues.put("city", c.getCity());
            updateValues.put("state", c.getState());
            updateValues.put("zipcode", c.getZipCode());
            updateValues.put("phonenumber", c.getPhoneNumber());
            updateValues.put("rprice", c.getrPrice());
            updateValues.put("pprice", c.getpPrice());
            updateValues.put("dprice", c.getdPrice());

            didSucceed = database.update("stations", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            //Do Nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastStationId() {
        int lastId = -1;
        try
        {
            String query = "Select MAX(_id) from stations";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }



}

