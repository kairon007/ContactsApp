package com.jerryfeng.contactsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;


public class EditActivity extends ActionBarActivity {

    //Widgets
    ImageButton photo;
    EditText fieldName, fieldNumber, fieldEmail;

    //Saving persistent data
    SharedPreferences sharedPref;
    int listPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Instantiating components
        fieldName = (EditText)findViewById(R.id.edit_name);
        fieldNumber = (EditText)findViewById(R.id.edit_number);
        fieldEmail = (EditText)findViewById(R.id.edit_email);

        //Receive intent
        Intent intent = getIntent();
        listPosition = intent.getIntExtra(getString(R.string.extra_listposition), -1);
        fieldName.setText(intent.getStringExtra(getString(R.string.extra_name)));
        fieldNumber.setText(intent.getStringExtra(getString(R.string.extra_number)));
        fieldEmail.setText(intent.getStringExtra(getString(R.string.extra_email)));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:     //Up button
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.menuEdit_save:
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
                /*
                sharedPref = getSharedPreferences(getString(R.string.pref_contacts_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                //save changes made to contact info
                editor.putString(getString(R.string.pref_contactName) + listPosition,
                        fieldName.getText().toString());
                editor.putString(getString(R.string.pref_contactNumber) + listPosition,
                        fieldNumber.getText().toString());
                editor.putString(getString(R.string.pref_contactEmail) + listPosition,
                        fieldEmail.getText().toString());
                editor.putString(getString(R.string.pref_contactImage) + listPosition,
                        "");
                editor.commit();*/

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
