package com.jerryfeng.contactsapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Custom class
 */
public class Field implements Parcelable {
    private String type;
    private String value;

    public Field() {    //Constructor
        this.type = "";
        this.value = "";
    }

    public Field(Parcel in) {   //Constructor for parcelling
        this.type = in.readString();
        this.value = in.readString();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.value);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Field createFromParcel(Parcel source) {
            return new Field(source);
        }

        @Override
        public Field[] newArray(int size) {
            return new Field[size];
        }
    };
}
