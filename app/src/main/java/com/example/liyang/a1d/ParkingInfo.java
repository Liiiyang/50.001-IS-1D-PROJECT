package com.example.liyang.a1d;

/**
 * Created by Li Yang on 6/12/2017.
 */

public class ParkingInfo {
    private String parkingname;
    private String id;
    private double latitude;
    private double longitude;
    private String weekday1;
    private String weekday2;
    private String sunday1;
    private String sunday2;
    private String lot;
    private int saved;


    public ParkingInfo(){
    }

    public ParkingInfo(String id, double latitude,double longitude,String lot,String parkingname,int saved,String sunday1,String sunday2
            ,String weekday1,String weekday2){
        this.parkingname = parkingname;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.weekday1 = weekday1;
        this.weekday2 = weekday2;
        this.sunday1 = sunday1;
        this.sunday2 = sunday2;
        this.lot = lot;
        this.saved = saved;
    }
    public void setParkingname(String parkingname){
        this.parkingname = parkingname;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
    public void setWeekday1(String weekday1){
        this.weekday1 = weekday1;
    }
    public void setWeekday2(String weekday2){
        this.weekday2 = weekday2;
    }
    public void setSunday1(String sunday1){
        this.sunday1 = sunday1;
    }
    public void setSunday2(String sunday2){
        this.sunday2 = sunday2;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getParkingname(){
        return parkingname;
    }
    public String getId(){
        return id;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public String getWeekday1(){
        return weekday1;
    }
    public String getWeekday2(){
        return weekday2;
    }
    public String getSunday1(){
        return sunday1;
    }
    public String getSunday2(){
        return sunday2;
    }
    public String getLot(){
        return lot;
    }
    public int getSaved(){return saved;}

}
