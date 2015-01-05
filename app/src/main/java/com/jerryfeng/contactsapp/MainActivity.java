package com.jerryfeng.contactsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, ExpandableListView.OnGroupExpandListener, ExpandableListView.OnGroupCollapseListener {

    //Widgets
    ImageView previewPhoto;
    TextView previewName;
    ImageButton buttonCall, buttonText;

    ExpandableListView contactsListView;
    ExpandableListAdapter contactsAdapter;
    ArrayList<Contact> contactsList;
    int prevSelectedGroup;

    //Saving activity states for change in screen config etc.
    static final String STATE_PREVIEWNAME = "previewName";

    //Saving persistent data
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiating components

        previewName = (TextView)findViewById(R.id.main_previewName);
        /*
        buttonCall = (ImageButton)findViewById(R.id.main_buttonCall);
        buttonCall.setOnClickListener(this);
        buttonText = (ImageButton)findViewById(R.id.main_buttonText);
        buttonText.setOnClickListener(this);*/

        contactsListView = (ExpandableListView)findViewById(R.id.main_contactsList);
        contactsList = new ArrayList<Contact>();
        contactsListView.setOnGroupExpandListener(this);
        contactsListView.setOnGroupCollapseListener(this);
        prevSelectedGroup = -1;
        //loadContactList();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        //loadContactList();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveContactList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Called both when app is started & returning from a child activity
        //Call here or call separately in onCreate() and onRestart()?
        loadContactList();
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
                if (prevSelectedGroup < 0) {
                    AlertDialog.Builder alertEdit = new AlertDialog.Builder(this);
                    alertEdit.setTitle("Edit contact");
                    alertEdit.setMessage("Please select a contact to edit");
                    alertEdit.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    alertEdit.show();
                }
                else {
                    Intent intentEdit = new Intent(this, EditActivity.class);
                    intentEdit.setAction(Intent.ACTION_VIEW);
                    Contact contact = contactsList.get(prevSelectedGroup);
                    Bundle bundle = new Bundle();
                    intentEdit.putExtra(getString(R.string.extra_listposition), prevSelectedGroup);
                    intentEdit.putExtra(getString(R.string.extra_name), contact.name);
                    intentEdit.putExtra(getString(R.string.extra_number), contact.number);
                    intentEdit.putExtra(getString(R.string.extra_email), contact.email);
                    intentEdit.putExtra(getString(R.string.extra_image), contact.imageSrc);
                    startActivity(intentEdit);
                }
                return true;

            case R.id.menu_delete:
                AlertDialog.Builder alertDelete = new AlertDialog.Builder(this);
                alertDelete.setTitle("Delete contact");
                if (prevSelectedGroup < 0) {
                    alertDelete.setMessage("Please select a contact to delete");
                    alertDelete.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    alertDelete.show();
                }
                else {
                    alertDelete.setMessage("Are you sure you want to delete this contact?");
                    alertDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            contactsList.remove(prevSelectedGroup);
                            saveContactList();
                        }
                    });
                    alertDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    alertDelete.show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        //Collapse all other groups except for currently selected one
        if (groupPosition != prevSelectedGroup) {
            contactsListView.collapseGroup(prevSelectedGroup);
        }
        prevSelectedGroup = groupPosition;
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        //Reset selected group
        prevSelectedGroup = -1;
    }

    private void loadContactList() {
        sharedPref = getSharedPreferences(getString(R.string.pref_contacts_key), Context.MODE_PRIVATE);

        //get updated length of list
        int listLength = sharedPref.getInt(getString(R.string.pref_contactCount), 0);
        //get each contact from numbered pref keys
        for (int i = 0; i < listLength; i++) {
            Contact contact = new Contact();
            contact.name = sharedPref.getString(getString(R.string.pref_contactName) + i, "");
            contact.number = sharedPref.getString(getString(R.string.pref_contactNumber) + i, "");
            contact.email = sharedPref.getString(getString(R.string.pref_contactEmail) + i, "");
            contact.imageSrc = sharedPref.getString(getString(R.string.pref_contactImage) + i, "");

            if (i >= contactsList.size()) {     //if index is beyond current list length,
                contactsList.add(contact);      //add as new entry
            } else {                            //if index is within current list length,
                contactsList.set(i, contact);   //replace existing entry
            }
        }
        //Update list adapter
        contactsAdapter = new ExpandableListAdapter(this, contactsList);
        contactsListView.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();

    }

    private void saveContactList() {
        sharedPref = getSharedPreferences(getString(R.string.pref_contacts_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        //save length of list
        editor.putInt(getString(R.string.pref_contactCount), contactsList.size());
        //save each contact with numbered pref keys
        for (int i = 0; i < contactsList.size(); i++) {
            editor.putString(getString(R.string.pref_contactName) + i, contactsList.get(i).name);
            editor.putString(getString(R.string.pref_contactNumber) + i, contactsList.get(i).number);
            editor.putString(getString(R.string.pref_contactEmail) + i, contactsList.get(i).email);
            editor.putString(getString(R.string.pref_contactImage) + i, contactsList.get(i).imageSrc);
        }
        editor.commit();
        //Update list adapter
        contactsAdapter = new ExpandableListAdapter(this, contactsList);
        contactsListView.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();
    }

}
