package com.jerryfeng.contactsapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Custom class
 */
public class Contact implements Parcelable {
    //String imageSrc;
    private String name;
    private ArrayList<Field> numbers;
    private ArrayList<Field> emails;
    private ArrayList<Field> misc;

    public Contact() {  //Constructor
        this.name = "";
        this.numbers = new ArrayList<Field>();
        this.emails = new ArrayList<Field>();
        this.misc = new ArrayList<Field>();
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setNumberType(int indexInNumbers, String type) {
        this.numbers.get(indexInNumbers).setType(type);
    }

    public void setEmailType(int indexInEmails, String type) {
        this.numbers.get(indexInEmails).setType(type);
    }

    public void setMiscType(int indexInMisc, String type) {
        this.numbers.get(indexInMisc).setType(type);
    }

    public String getNumberType(int indexInNumbers) {
        return this.numbers.get(indexInNumbers).getType();
    }

    public String getEmailType(int indexInEmails) {
        return this.emails.get(indexInEmails).getType();
    }

    public String getMiscType(int indexInMisc) {
        return this.misc.get(indexInMisc).getType();
    }

    public void setNumberValue(int indexInNumbers, String value) {
        this.numbers.get(indexInNumbers).setValue(value);
    }

    public void setEmailValue(int indexInEmails, String value) {
        this.numbers.get(indexInEmails).setValue(value);
    }

    public void setMiscValue(int indexInMisc, String value) {
        this.numbers.get(indexInMisc).setValue(value);
    }

    public String getNumberValue(int indexInNumbers) {
        return this.numbers.get(indexInNumbers).getValue();
    }

    public String getEmailValue(int indexInEmails) {
        return this.emails.get(indexInEmails).getValue();
    }

    public String getMiscValue(int indexInMisc) {
        return this.misc.get(indexInMisc).getValue();
    }

    public CharSequence[] getNumberValues() {
        CharSequence[] numbers = new String[this.numbers.size()];
        for (int i = 0; i < this.numbers.size(); i++) {
            numbers[i] = this.numbers.get(i).getValue();
        }
        return numbers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeTypedList(this.numbers);
        dest.writeTypedList(this.emails);
        dest.writeTypedList(this.misc);
    }

    public Contact(Parcel in) { //Constructor for reading from parcel
        this.name = in.readString();
        in.readTypedList(this.numbers, Field.CREATOR);
        in.readTypedList(this.emails, Field.CREATOR);
        in.readTypedList(this.misc, Field.CREATOR);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
