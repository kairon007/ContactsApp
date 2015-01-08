package com.jerryfeng.contactsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements View.OnClickListener,
        ExpandableListView.OnGroupExpandListener, ExpandableListView.OnGroupCollapseListener {

    //Widgets

    ExpandableListView contactsListView;
    ExpandableListAdapter contactsAdapter;
    ArrayList<Contact> contactsList;

    //Saving list view states
    int selectedGroup;
    int listViewPosition;
    int listViewOffset;

    //Saving activity states for change in screen config etc.
    static final String STATE_SELECTEDGROUP = "selectedgroup";
    static final String STATE_LISTVIEWPOS = "listviewpos";
    static final String STATE_LISTVIEWOFFSET = "listviewoffset";

    //Saving persistent data
    SharedPreferences sharedPrefTypes, sharedPrefValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiating list view
        contactsListView = (ExpandableListView)findViewById(R.id.main_contactsList);
        contactsList = new ArrayList<Contact>();
        contactsListView.setOnGroupExpandListener(this);
        contactsListView.setOnGroupCollapseListener(this);

        //Initialize view state variables (these are default values)
        selectedGroup = -1;
        listViewPosition = 0;
        listViewOffset = 0;

        //Called here for when app is first run (or after screen config change)
        loadContactList();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //Get position of top visible item
        listViewPosition = contactsListView.getFirstVisiblePosition();
        //Get scroll offset of top visible item
        View childView = contactsListView.getChildAt(0);
        if (childView != null) {
            listViewOffset = childView.getTop();
        }
        //Save list view state
        savedInstanceState.putInt(STATE_SELECTEDGROUP, selectedGroup);
        savedInstanceState.putInt(STATE_LISTVIEWPOS, listViewPosition);
        savedInstanceState.putInt(STATE_LISTVIEWOFFSET, listViewOffset);
        Log.d("saving instance", "pos " + listViewPosition + " offset " + listViewOffset);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //Recover list view state
        selectedGroup = savedInstanceState.getInt(STATE_SELECTEDGROUP);
        listViewPosition = savedInstanceState.getInt(STATE_LISTVIEWPOS);
        listViewOffset = savedInstanceState.getInt(STATE_LISTVIEWOFFSET);
        recoverListState();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Called here for when leaving this activity to another activity
        saveContactList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //Called here for when returning to this activity from another activity
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
                if (selectedGroup < 0) {
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
                    Contact contact = contactsList.get(selectedGroup);
                    Bundle bundle = new Bundle();
                    intentEdit.putExtra(getString(R.string.extra_listposition), selectedGroup);
                    //intentEdit.putExtra(getString(R.string.extra_name), contact.name);
                    //intentEdit.putExtra(getString(R.string.extra_number), contact.number);
                    //intentEdit.putExtra(getString(R.string.extra_email), contact.email);
                    //intentEdit.putExtra(getString(R.string.extra_image), contact.imageSrc);
                    startActivity(intentEdit);
                }
                return true;

            case R.id.menu_delete:
                AlertDialog.Builder alertDelete = new AlertDialog.Builder(this);
                alertDelete.setTitle("Delete contact");
                if (selectedGroup < 0) {
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
                            contactsList.remove(selectedGroup);
                            selectedGroup = -1;
                            saveContactList();
                            updateListAdapter();
                            recoverListState();
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

    }

    @Override
    public void onGroupExpand(int groupPosition) {
        //Collapse previously selected group
        if (groupPosition != selectedGroup) {
            contactsListView.collapseGroup(selectedGroup);
        }
        selectedGroup = groupPosition;
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        //Reset selected group back to default
        selectedGroup = -1;
    }

    /** Load contacts from SharedPreferences */
    private void loadContactList() {
        sharedPrefTypes = getSharedPreferences(getString(R.string.pref_types_key), Context.MODE_PRIVATE);
        sharedPrefValues = getSharedPreferences(getString(R.string.pref_values_key), Context.MODE_PRIVATE);

        //get updated length of list
        int listLength = sharedPrefTypes.getInt(getString(R.string.pref_listLength), 0);

        for (int iList = 0; iList < listLength; iList++) {
            Contact contact = new Contact();
            //get name field
            contact.name = sharedPrefValues.getString(getString(R.string.pref_name) + "_" + iList, "");
            //get number fields
            int numOfNumbers = sharedPrefTypes.getInt(getString(R.string.pref_numofnumbers) + "_" + iList, 0);
            for (int iNum = 0; iNum < numOfNumbers; iNum++) {
                Field number = new Field();
                number.type = sharedPrefTypes.getString(getString(R.string.pref_number)
                        + "_" + iList + "_" + iNum, "");
                number.value = sharedPrefValues.getString(getString(R.string.pref_number)
                        + "_" + iList + "_" + iNum, "");
                contact.numbers.add(number);
            }
            //get email fields
            int numOfEmails = sharedPrefTypes.getInt(getString(R.string.pref_numofemails) + "_" + iList, 0);
            for (int iEm = 0; iEm < numOfEmails; iEm++) {
                Field email = new Field();
                email.type = sharedPrefTypes.getString(getString(R.string.pref_email)
                        + "_" + iList + "_" + iEm, "");
                email.value = sharedPrefValues.getString(getString(R.string.pref_email)
                        + "_" + iList + "_" + iEm, "");
                contact.emails.add(email);
            }
            //get misc fields
            int numOfMisc = sharedPrefTypes.getInt(getString(R.string.pref_numofmisc) + "_" + iList, 0);
            for (int iMisc = 0; iMisc < numOfMisc; iMisc++) {
                Field misc = new Field();
                misc.type = sharedPrefTypes.getString(getString(R.string.pref_misc)
                        + "_" + iList + "_" + iMisc, "");
                misc.value = sharedPrefValues.getString(getString(R.string.pref_misc)
                        + "_" + iList + "_" + iMisc, "");
                contact.misc.add(misc);
            }
            if (iList >= contactsList.size()) {     //if index is beyond current list length,
                contactsList.add(contact);          //add as new entry
            } else {                                //if index is within current list length,
                contactsList.set(iList, contact);   //replace existing entry
            }
        }

        updateListAdapter();
        recoverListState();
    }

    /** Save contacts to SharedPreferences */
    private void saveContactList() {
        sharedPrefTypes = getSharedPreferences(getString(R.string.pref_types_key), Context.MODE_PRIVATE);
        sharedPrefValues = getSharedPreferences(getString(R.string.pref_values_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor eTypes = sharedPrefTypes.edit();
        SharedPreferences.Editor eValues = sharedPrefValues.edit();
        //save contact list length
        eTypes.putInt(getString(R.string.pref_listLength), contactsList.size());
        for (int iList = 0; iList < contactsList.size(); iList++) {
            //save name field
            eValues.putString(getString(R.string.pref_name) + "_" + iList,
                    contactsList.get(iList).name);
            //save number fields
            eTypes.putInt(getString(R.string.pref_numofnumbers) + "_" + iList,
                    contactsList.get(iList).numbers.size());
            for (int iNum = 0; iNum < contactsList.get(iList).numbers.size(); iNum++) {
                eTypes.putString(getString(R.string.pref_number) + "_" + iList + "_" + iNum,
                        contactsList.get(iList).numbers.get(iNum).type);
                eValues.putString(getString(R.string.pref_number) + "_" + iList + "_" + iNum,
                        contactsList.get(iList).numbers.get(iNum).value);
            }
            //save email fields
            eTypes.putInt(getString(R.string.pref_numofemails) + "_" + iList,
                    contactsList.get(iList).emails.size());
            for (int iEm = 0; iEm < contactsList.get(iList).emails.size(); iEm++) {
                eTypes.putString(getString(R.string.pref_email) + "_" + iList + "_" + iEm,
                        contactsList.get(iList).emails.get(iEm).type);
                eValues.putString(getString(R.string.pref_email) + "_" + iList + "_" + iEm,
                        contactsList.get(iList).emails.get(iEm).value);
            }
            //save misc fields
            eTypes.putInt(getString(R.string.pref_numofmisc) + "_" + iList,
                    contactsList.get(iList).misc.size());
            for (int iMisc = 0; iMisc < contactsList.get(iList).misc.size(); iMisc++) {
                eTypes.putString(getString(R.string.pref_misc) + "_" + iList + "_" + iMisc,
                        contactsList.get(iList).misc.get(iMisc).type);
                eValues.putString(getString(R.string.pref_misc) + "_" + iList + "_" + iMisc,
                        contactsList.get(iList).misc.get(iMisc).value);
            }
        }
        eTypes.commit();
        eValues.commit();
    }

    /** Called when list view's size is changed */
    private void updateListAdapter() {
        contactsAdapter = new ExpandableListAdapter(this, contactsList);
        contactsListView.setAdapter(contactsAdapter);
        contactsAdapter.notifyDataSetChanged();
    }

    /** Re-expand any previously expanded groups, and recover previous scroll position */
    private void recoverListState() {
        if (selectedGroup >= 0) {
            contactsListView.expandGroup(selectedGroup);
        }
        contactsListView.setSelectionFromTop(listViewPosition, listViewOffset);
    }
}
