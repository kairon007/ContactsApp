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
import android.widget.EditText;
import android.widget.ImageButton;


public class AddNewActivity extends ActionBarActivity {

    //Widgets
    ImageButton photo;
    EditText fieldName, fieldNumber, fieldEmail;

    //Saving persistent data
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        //Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Instantiating components
        fieldName = (EditText)findViewById(R.id.addNew_name);
        fieldNumber = (EditText)findViewById(R.id.addNew_number);
        fieldEmail = (EditText)findViewById(R.id.addNew_email);


        //Receive intent
        Intent intentAddNew = getIntent();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddNew_save:
                if (fieldName.getText().toString().length() < 1) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setTitle("Save contact");
                    alert.setMessage("Please enter a name for this contact");
                    alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    alert.show();
                    return true;
                }

                sharedPref = getSharedPreferences(getString(R.string.pref_contacts_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                //update length of list
                int contactCount = sharedPref.getInt(getString(R.string.pref_contactCount), -1) + 1;
                editor.putInt(getString(R.string.pref_contactCount), contactCount);

                //save contact with key numbered with the next available index
                editor.putString(getString(R.string.pref_contactName) + (contactCount - 1),
                        fieldName.getText().toString());
                editor.putString(getString(R.string.pref_contactNumber) + (contactCount - 1),
                        fieldNumber.getText().toString());
                editor.putString(getString(R.string.pref_contactEmail) + (contactCount - 1),
                        fieldEmail.getText().toString());
                editor.putString(getString(R.string.pref_contactImage) + (contactCount - 1),
                        "");
                editor.commit();

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
