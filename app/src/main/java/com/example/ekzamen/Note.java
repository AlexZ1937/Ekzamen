package com.example.ekzamen;

import com.google.android.gms.maps.model.LatLng;

public class Note
{
    int ID;
    String header;
    String text;
    LatLng place;
    public Note()
    {
        ID=1;
        place=new LatLng(0,0);
        header="Some Header";
        text="Some Text";
    }

    public Note(int ID,String header,String text,LatLng place)
    {
        this.ID=ID;
        this.header=header;
        this.text=text;
        this.place=place;
    }

    public String getHeader() {
        return header;
    }

    public String getText() {
        return text;
    }

    public LatLng getPlace() {
        return place;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return header;
    }
}
