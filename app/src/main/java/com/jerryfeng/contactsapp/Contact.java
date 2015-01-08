package com.jerryfeng.contactsapp;

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
}
