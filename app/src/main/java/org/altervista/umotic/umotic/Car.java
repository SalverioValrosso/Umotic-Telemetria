package org.altervista.umotic.umotic;

import android.content.Context;

/**
 * Created by admin on 21/11/2018.
 */

public class Car {
    private String Targa;
    private String Brand;
    private String Model;
    private String Alias;
    private String text,subText;
    boolean isExpandable=true;
    private  int Photo;
    Context context;
    public Car(){
    }

    public Car(String targa, String brand, String model, String alias, Context context) {
        this.Targa = targa;
        this.Brand = brand;
        this.Model = model;
        this.Alias = alias;
        //this.Photo = photo;
        this.context=context;
    }

    public String getBrand() {
        return Brand;
    }

    public String getModel() {
        return Model;
    }

    public String getAlias() {
        return Alias;
    }

    public String getTarga() {
        return Targa;
    }

    public int getPhoto() {
        return Photo;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public void setModel(String model) {
        Model = model;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public void setTarga(String targa) {
        Targa = targa;
    }

    public void setPhoto(int photo) {
        Photo = photo;
    }

    public String getText() {
        String itemS= ""+Alias+"\n"+Targa;
        return itemS;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubText() {
        String details= context.getString(R.string.car_brand)+": "+Brand+"\n"+context.getString(R.string.car_model)+": "+Model;;
        return details;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }
}
