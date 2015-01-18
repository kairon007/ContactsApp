package com.jerryfeng.contactsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.style.TtsSpan;

import org.apache.http.conn.ConnectTimeoutException;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Handles database for contact information
 */
public class ContactsDataHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsData";
    private static final String TABLE_NAME = "contactsTable";
    private static final String COL_ID = "id";
    private static final String COL_CONTAINER = "container";
    private static final String COL_TYPE = "type";
    private static final String COL_VALUE = "value";

    //Constructor
    public ContactsDataHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER,"
                + COL_CONTAINER + " TEXT," + COL_TYPE + " TEXT," + COL_VALUE + " TEXT" + " )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Add name row
        ContentValues row = new ContentValues();
        row.put(COL_ID, contact.getID());
        row.put(COL_CONTAINER, Contact.NAME);
        row.put(COL_TYPE, Contact.NAME);
        row.put(COL_VALUE, contact.getName());
        db.insert(TABLE_NAME, null, row);
        //Add number, email, and misc rows
        for (int iNum = 0; iNum < contact.getNumbersCount(); iNum++) {
            row.put(COL_ID, contact.getID());
            row.put(COL_CONTAINER, Contact.NUMBER);
            row.put(COL_TYPE, contact.getNumberType(iNum));
            row.put(COL_VALUE, contact.getNumberValue(iNum));
            db.insert(TABLE_NAME, null, row);
        }
        for (int iEm = 0; iEm < contact.getEmailsCount(); iEm++) {
            row.put(COL_ID, contact.getID());
            row.put(COL_CONTAINER, Contact.EMAIL);
            row.put(COL_TYPE, contact.getEmailType(iEm));
            row.put(COL_VALUE, contact.getEmailValue(iEm));
            db.insert(TABLE_NAME, null, row);
        }
        for (int iMisc = 0; iMisc < contact.getMiscCount(); iMisc++) {
            row.put(COL_ID, contact.getID());
            row.put(COL_CONTAINER, Contact.MISC);
            row.put(COL_TYPE, contact.getMiscType(iMisc));
            row.put(COL_VALUE, contact.getMiscValue(iMisc));
            db.insert(TABLE_NAME, null, row);
        }
        db.close();
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Contact contact = new Contact();

        //SQL: SELECT * FROM table WHERE id = id AND container = 'name'
        Cursor cName = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + "=" + id
                + " AND " + COL_CONTAINER + " = '" + Contact.NAME + "'", null);
        //SQL: SELECT * FROM table WHERE id = id AND container = 'number'
        Cursor cNumbers = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + "=" + id
                + " AND " + COL_CONTAINER + " = '" + Contact.NUMBER + "'", null);
        //SQL: SELECT * FROM table WHERE id = id AND container = 'email'
        Cursor cEmail = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + "=" + id
                + " AND " + COL_CONTAINER + " = '" + Contact.EMAIL + "'", null);
        //SQL: SELECT * FROM table WHERE id = id AND container = 'misc'
        Cursor cMisc = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + "=" + id
                + " AND " + COL_CONTAINER + " = '" + Contact.MISC + "'", null);
        //Return null if contact isn't found (no name matches with given id)
        if (cName.getCount() < 1) {
            db.close();
            return null;
        }
        //Set ID
        contact.setID(id);
        //Add name field
        cName.moveToFirst();
        contact.setName(cName.getString(cName.getColumnIndex(COL_VALUE)));
        if (cNumbers.getCount() > 0) {
            cNumbers.moveToFirst();     //Add number fields
            do {
                contact.addNumber(cNumbers.getString(cNumbers.getColumnIndex(COL_TYPE)),
                        cNumbers.getString(cNumbers.getColumnIndex(COL_VALUE)));
            } while (cNumbers.moveToNext());
        }
        if (cEmail.getCount() > 0) {    //Add email fields
            cEmail.moveToFirst();
            do {
                contact.addEmail(cEmail.getString(cEmail.getColumnIndex(COL_TYPE)),
                        cEmail.getString(cEmail.getColumnIndex(COL_VALUE)));
            } while(cEmail.moveToNext());
        }
        if (cMisc.getCount() > 0) {     //Add misc fields
            cMisc.moveToFirst();
            do {
                contact.addMisc(cMisc.getString(cMisc.getColumnIndex(COL_TYPE)),
                        cMisc.getString(cMisc.getColumnIndex(COL_VALUE)));
            } while(cMisc.moveToNext());
        }
        db.close();
        return contact;
    }

    public void updateContact(Contact contact) {
        deleteContact(contact);
        addContact(contact);
    }

    //TODO - return stuff in alphabetical order
    public ArrayList<Contact> getAllContacts() {    //Return all contacts stored in table
        ArrayList<Contact> list = new ArrayList<Contact>();
        int id = 0;
        //if (getHighestID() < 0) return list;
        while(id <= getHighestID()) {
            list.add(getContact(id));
            id++;
        }
        return list;
    }

    public int getContactsCount() { //Return number of contacts stored in table
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_CONTAINER + " = '"
                + Contact.NAME + "'", null);
        return c.getCount();
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + "=?", new String[] {String.valueOf(contact.getID())});
    }

    public int getNextAvailableID() {
        int id = 0;
        while(this.getContact(id) != null) {
            id++;
        }
        return id;
    }

    public int getHighestID() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID
                + "= (SELECT MAX(" + COL_ID + ") FROM " + TABLE_NAME + ")", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            return c.getInt(c.getColumnIndex(COL_ID));
        }
        return -1;
    }
}
