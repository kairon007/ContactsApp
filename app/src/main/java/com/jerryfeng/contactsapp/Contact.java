package com.jerryfeng.contactsapp;

import android.util.Log;

import java.util.ArrayList;

/**
 * Custom class
 */
public class Contact {
    //String imageSrc;
    String name;
    ArrayList<Field> numbers;
    ArrayList<Field> emails;
    ArrayList<Field> misc;

    public Contact() {  //Constructor
        numbers = new ArrayList<Field>();
        emails = new ArrayList<Field>();
        misc = new ArrayList<Field>();
    }

    public CharSequence[] getNumberValues() {
        CharSequence[] numbers = new String[this.numbers.size()];
        for (int i = 0; i < this.numbers.size(); i++) {
            numbers[i] = this.numbers.get(i).value;
        }
        return numbers;
    }
}
