package com.example.cobragas;

import android.graphics.Bitmap;

import java.sql.Blob;
import java.util.Calendar;

public class Gas {
        private int stationID;
        private String stationName;
        private String streetAddress;
        private String city;
        private String state;
        private String zipCode;
        private String phoneNumber;
        private String rPrice;
        private String pPrice;
        private String dPrice;
        private String distance;





    public Gas() {
        stationID = -1;
    }

    public int getStationID() {
            return stationID;
        }
        public void setStationID(int i) {
            stationID = i;
        }
        public String getStationName() {
            return stationName;
        }
        public void setStationName(String s) {
            stationName = s;
        }
        public String getStreetAddress() {
            return streetAddress;
        }
        public void setStreetAddress(String s) {
            streetAddress = s;
        }
        public String getCity() {
            return city;
        }
        public void setCity(String s) {
            city = s;
        }
        public String getState() {
            return state;
        }
        public void setState(String s) {
            state = s;
        }
        public String getZipCode() {
            return zipCode;
        }
        public void setZipCode(String s) {
            zipCode = s;
        }
        public void setPhoneNumber(String s) {
            phoneNumber = s;
        }
        public String getPhoneNumber() {
            return phoneNumber;
        }
        public void setrPrice(String s) {
            rPrice = s;
        }
        public String getrPrice() {
            return rPrice;
        }
        public void setpPrice(String s) {
            pPrice = s;
        }
        public String getpPrice() {
            return pPrice;
        }
        public void setdPrice(String s) {
            dPrice = s;
        }
        public String getdPrice() {
            return dPrice;
        }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}
