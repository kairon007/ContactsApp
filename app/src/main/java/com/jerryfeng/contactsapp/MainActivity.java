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

public class MainActivity extends ActionBarActivity implements ExpandableListView.OnGroupExpandListener,
        ExpandableListView.OnGroupCollapseListener {

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
                    //Send data as extras for selected contact
                    Contact contact = contactsList.get(selectedGroup);
                    intentEdit.putExtra(getString(R.string.extra_listposition), selectedGroup);
                    intentEdit.putExtra(getString(R.string.extra_contactparcel), contact);
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
                            //Delete selected contact, update list and adapter data
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

    private void loadContactList() {
        ContactsDataHandler db = new ContactsDataHandler(this);
        contactsList = (ArrayList<Contact>)db.getAllContacts().clone();

        db.close();

        updateListAdapter();
        recoverListState();
    }

    //TODO - Save contacts to database
    private void saveContactList() {
        ContactsDataHandler db = new ContactsDataHandler(this);

        db.close();
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
