package com.jerryfeng.contactsapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, ExpandableListView.OnGroupClickListener {

    //Widgets
    ImageView previewPhoto;
    TextView previewName, previewNumber, previewEmail;
    ImageButton buttonCall, buttonText;

    ExpandableListView contactsListView;
    ExpandableListAdapter contactsAdapter;
    ArrayList<Contact> contactsList;


    //Saving activity states for change in screen config etc.
    static final String STATE_PREVIEWNAME = "previewName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiating components

        previewName = (TextView)findViewById(R.id.main_previewName);
        previewNumber = (TextView)findViewById(R.id.main_previewNumber);
        previewEmail = (TextView)findViewById(R.id.main_previewEmail);

        buttonCall = (ImageButton)findViewById(R.id.main_buttonCall);
        buttonCall.setOnClickListener(this);
        buttonText = (ImageButton)findViewById(R.id.main_buttonText);
        buttonText.setOnClickListener(this);

        contactsListView = (ExpandableListView)findViewById(R.id.main_contactsList);
        initContactList();
        contactsAdapter = new ExpandableListAdapter(this, contactsList);
        contactsListView.setAdapter(contactsAdapter);
        contactsListView.setOnGroupClickListener(this);
    }

    private void initContactList() {
        contactsList = new ArrayList<Contact>();
        Contact sampleContact = new Contact();
        sampleContact.photoSrc="";
        sampleContact.name="John Smith";
        sampleContact.number="519 000 0000";
        sampleContact.email="johnsmith@gmail.com";
        contactsList.add(sampleContact);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_addNew:
                Intent intentAddNew = new Intent(this, AddNewActivity.class);
                intentAddNew.setAction(Intent.ACTION_VIEW);
                startActivity(intentAddNew);
                return true;
            case R.id.menu_edit:

                return true;
            case R.id.menu_delete:
                //if (contactsListView)
                AlertDialog.Builder alertDelete = new AlertDialog.Builder(this);
                alertDelete.setTitle("Delete contact");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_buttonCall:
                break;
            case R.id.main_buttonText:
                break;
        }
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        //Log.d("ContactsApp", parent + "");

        if (parent.isGroupExpanded(groupPosition)) {
            previewName.setText(R.string.main_previewName);
            previewNumber.setText(R.string.main_previewNumber);
            previewEmail.setText(R.string.main_previewEmail);
        }
        else {
            previewName.setText(contactsList.get(groupPosition).name);
            previewNumber.setText(contactsList.get(groupPosition).number);
            previewEmail.setText(contactsList.get(groupPosition).email);
        }

        return false;
    }
}
