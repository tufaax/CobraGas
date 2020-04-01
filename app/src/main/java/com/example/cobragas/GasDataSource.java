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

            initialValues.put("stationname", c.getStationName());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZipCode());
            initialValues.put("phonenumber", c.getPhoneNumber());
            initialValues.put("rprice", c.getrPrice());
            initialValues.put("pprice", c.getpPrice());
            initialValues.put("dprice", c.getdPrice());
            initialValues.put("distance", c.getDistance());

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

            updateValues.put("stationname", c.getStationName());
            updateValues.put("streetaddress", c.getStreetAddress());
            updateValues.put("city", c.getCity());
            updateValues.put("state", c.getState());
            updateValues.put("zipcode", c.getZipCode());
            updateValues.put("phonenumber", c.getPhoneNumber());
            updateValues.put("rprice", c.getrPrice());
            updateValues.put("pprice", c.getpPrice());
            updateValues.put("dprice", c.getdPrice());
            updateValues.put("distance", c.getDistance());

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

    public boolean deleteGas(int gasId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("stations", "_id=" + gasId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }

    public ArrayList<Gas> getStations(String sortField, String sortOrder) {

        ArrayList<Gas> gases = new ArrayList<Gas>();
        try {
            String query = "SELECT * FROM stations ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Gas newStation;
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                newStation = new Gas();
                newStation.setStationID(cursor.getInt(0));
                newStation.setStationName(cursor.getString(1));
                newStation.setStreetAddress(cursor.getString(2));
                newStation.setCity(cursor.getString(3));
                newStation.setState(cursor.getString(4));
                newStation.setZipCode(cursor.getString(5));
                newStation.setPhoneNumber(cursor.getString(6));
                newStation.setrPrice(cursor.getString(7));
                newStation.setpPrice(cursor.getString(8));
                newStation.setdPrice(cursor.getString(9));
                newStation.setDistance(cursor.getString(10));

                gases.add(newStation);
                cursor.moveToNext();

            }
            cursor.close();
        }
        catch (Exception e){
            gases = new ArrayList<Gas>();
        }
        return gases;
    }

    public Gas getSpecificStation(int stationId){
        Gas contact = new Gas();

        String query = "SELECT * FROM stations WHERE _id =" + stationId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            contact.setStationID(cursor.getInt(0));
            contact.setStationName(cursor.getString(1));
            contact.setStreetAddress(cursor.getString(2));
            contact.setCity(cursor.getString(3));
            contact.setState(cursor.getString(4));
            contact.setZipCode(cursor.getString(5));
            contact.setPhoneNumber(cursor.getString(6));
            contact.setrPrice(cursor.getString(7));
            contact.setpPrice(cursor.getString(8));
            contact.setdPrice(cursor.getString(9));
            contact.setDistance(cursor.getString(10));

            cursor.close();
        }
        return contact;
    }


}

