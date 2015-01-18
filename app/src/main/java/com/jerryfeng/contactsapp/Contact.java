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
    private int id;
    private String name;
    private ArrayList<Field> numbers;
    private ArrayList<Field> emails;
    private ArrayList<Field> misc;

    public static final String NAME = "Name";
    public static final String NUMBER = "Phone number";
    public static final String EMAIL = "Email address";
    public static final String MISC = "Misc field";

    public Contact() {  //Constructor
        this.id = -1;
        this.name = "";
        this.numbers = new ArrayList<Field>();
        this.emails = new ArrayList<Field>();
        this.misc = new ArrayList<Field>();
    }

    public void setID (int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
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

    public int getNumbersCount() {
        return this.numbers.size();
    }

    public int getEmailsCount() {
        return this.emails.size();
    }

    public int getMiscCount() {
        return this.misc.size();
    }

    public String getMiscValue(int indexInMisc) {
        return this.misc.get(indexInMisc).getValue();
    }

    public void addNumber(String type, String value) {
        Field newField = new Field();
        newField.setType(type);
        newField.setValue(value);
        this.numbers.add(newField);
    }

    public void addEmail(String type, String value) {
        Field newField = new Field();
        newField.setType(type);
        newField.setValue(value);
        this.emails.add(newField);
    }
    public void addMisc(String type, String value) {
        Field newField = new Field();
        newField.setType(type);
        newField.setValue(value);
        this.misc.add(newField);
    }

    public void removeAllNumbers() {
        this.numbers.clear();
    }

    public void removeAllEmails() {
        this.emails.clear();
    }

    public void removeAllMisc() {
        this.misc.clear();
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
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.numbers);
        dest.writeTypedList(this.emails);
        dest.writeTypedList(this.misc);
    }

    public Contact(Parcel in) { //Constructor for reading from parcel
        this.id = in.readInt();
        this.name = in.readString();
        this.numbers = in.createTypedArrayList(Field.CREATOR);
        this.emails = in.createTypedArrayList(Field.CREATOR);
        this.misc = in.createTypedArrayList(Field.CREATOR);
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
