package com.jerryfeng.contactsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


public class EditActivity extends ActionBarActivity implements View.OnClickListener,
        FieldFragment.OnFragmentDeleteListener {

    //Widgets
    ImageButton photo;
    EditText fieldName;
    TextView buttonAddNumber, buttonAddEmail, buttonAddMisc;

    //Fragments
    ArrayList<FieldFragment> listFieldNumbers, listFieldEmails, listFieldMisc;
    int fragmentSerial;

    //Saving persistent data
    int listPosition;

    //Contact being edited
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Instantiating components
        fieldName = (EditText)findViewById(R.id.edit_fieldName);
        buttonAddNumber = (TextView)findViewById(R.id.edit_addNumber);
        buttonAddNumber.setOnClickListener(this);
        buttonAddEmail = (TextView)findViewById(R.id.edit_addEmail);
        buttonAddEmail.setOnClickListener(this);
        buttonAddMisc = (TextView)findViewById(R.id.edit_addMisc);
        buttonAddMisc.setOnClickListener(this);

        //Initialize fragment components
        listFieldNumbers = new ArrayList<FieldFragment>();
        listFieldEmails = new ArrayList<FieldFragment>();
        listFieldMisc = new ArrayList<FieldFragment>();
        fragmentSerial = 0;

        //Receive intent & contact data
        Intent intent = getIntent();
        contact = intent.getParcelableExtra(getString(R.string.extra_contactparcel));
        listPosition = intent.getIntExtra(getString(R.string.extra_listposition), -1);

        //Set up existing fields
        fieldName.setText(contact.getName());
        for (int i = 0; i < contact.getNumbersCount(); i++) {
            FieldFragment fieldNumber = new FieldFragment();
            listFieldNumbers.add(fieldNumber);
            Bundle args = new Bundle();
            //Create and pass arguments bundle
            args.putInt(FieldFragment.PARAM_CONTAINER, FieldFragment.CONTAINER_NUMBER);
            args.putInt(FieldFragment.PARAM_INDEX, listFieldNumbers.size() - 1);
            args.putString(FieldFragment.PARAM_TYPE, contact.getNumberType(i));
            args.putString(FieldFragment.PARAM_VALUE, contact.getNumberValue(i));
            fieldNumber.setArguments(args);
            //Add fragment with unique tag
            getSupportFragmentManager().beginTransaction().add(R.id.edit_numbersContainer,
                    fieldNumber, getString(R.string.frag_tag_number) + fragmentSerial).commit();
            fragmentSerial++;
        }
        for (int i = 0; i < contact.getEmailsCount(); i++) {
            FieldFragment fieldEmail = new FieldFragment();
            listFieldEmails.add(fieldEmail);
            Bundle args = new Bundle();
            //Create and pass arguments bundle
            args.putInt(FieldFragment.PARAM_CONTAINER, FieldFragment.CONTAINER_EMAIL);
            args.putInt(FieldFragment.PARAM_INDEX, listFieldEmails.size() - 1);
            args.putString(FieldFragment.PARAM_TYPE, contact.getEmailType(i));
            args.putString(FieldFragment.PARAM_VALUE, contact.getEmailValue(i));
            fieldEmail.setArguments(args);
            //Add fragment with unique tag
            getSupportFragmentManager().beginTransaction().add(R.id.edit_emailsContainer,
                    fieldEmail, getString(R.string.frag_tag_email) + fragmentSerial).commit();
            fragmentSerial++;
        }
        for (int i = 0; i < contact.getMiscCount(); i++) {
            FieldFragment fieldMisc = new FieldFragment();
            listFieldMisc.add(fieldMisc);
            Bundle args = new Bundle();
            //Create and pass arguments bundle
            args.putInt(FieldFragment.PARAM_CONTAINER, FieldFragment.CONTAINER_MISC);
            args.putInt(FieldFragment.PARAM_INDEX, listFieldMisc.size() - 1);
            args.putString(FieldFragment.PARAM_TYPE, contact.getMiscType(i));
            args.putString(FieldFragment.PARAM_VALUE, contact.getMiscValue(i));
            fieldMisc.setArguments(args);
            //Add fragment with unique tag
            getSupportFragmentManager().beginTransaction().add(R.id.edit_miscContainer,
                    fieldMisc, getString(R.string.frag_tag_misc) + fragmentSerial).commit();
            fragmentSerial++;
        }
        if (listFieldNumbers.size() < 1) {
            onClick(findViewById(R.id.edit_addNumber));
        }
        if (listFieldEmails.size() < 1) {
            onClick(findViewById(R.id.edit_addEmail));
        }
        if (listFieldMisc.size() < 1) {
            onClick(findViewById(R.id.edit_addMisc));
        }
    }

    //Handling screen config changes
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //TODO - Implement saving instance state
    }

    //Handling screen config changes
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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

                ContactsDataHandler db = new ContactsDataHandler(this);
                //save name field
                this.contact.setName(fieldName.getText().toString());
                //Clear fields, then save into empty contact
                this.contact.removeAllNumbers();
                this.contact.removeAllEmails();
                this.contact.removeAllMisc();
                //save number fields
                for (int iNum = 0; iNum < listFieldNumbers.size(); iNum++) {
                    String fieldType = listFieldNumbers.get(iNum).getFieldType();
                    String fieldValue = listFieldNumbers.get(iNum).getFieldValue();
                    if (fieldType.length() > 0 && fieldValue.length() > 0) {
                        this.contact.addNumber(fieldType, fieldValue);
                    }
                }
                //save email fields
                for (int iEm = 0; iEm < listFieldEmails.size(); iEm++) {
                    String fieldType = listFieldEmails.get(iEm).getFieldType();
                    String fieldValue = listFieldEmails.get(iEm).getFieldValue();
                    if (fieldType.length() > 0 && fieldValue.length() > 0) {
                        this.contact.addEmail(fieldType, fieldValue);
                    }
                }
                //save misc fields
                for (int iMisc = 0; iMisc < listFieldMisc.size(); iMisc++) {
                    String fieldType = listFieldMisc.get(iMisc).getFieldType();
                    String fieldValue = listFieldMisc.get(iMisc).getFieldValue();
                    if (fieldType.length() > 0 && fieldValue.length() > 0) {
                        this.contact.addMisc(fieldType, fieldValue);
                    }
                }
                db.updateContact(this.contact);
                db.close();

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
        switch (v.getId()) {
            case R.id.edit_addNumber:
                FieldFragment fieldNumber = new FieldFragment();
                listFieldNumbers.add(fieldNumber);
                //Create and pass arguments bundle
                args.putInt(FieldFragment.PARAM_CONTAINER, FieldFragment.CONTAINER_NUMBER);
                args.putInt(FieldFragment.PARAM_INDEX, listFieldNumbers.size() - 1);
                fieldNumber.setArguments(args);
                //Add fragment with unique tag
                getSupportFragmentManager().beginTransaction().add(R.id.edit_numbersContainer,
                        fieldNumber, getString(R.string.frag_tag_number) + fragmentSerial).commit();
                fragmentSerial++;
                break;
            case R.id.edit_addEmail:
                FieldFragment fieldEmail = new FieldFragment();
                listFieldEmails.add(fieldEmail);
                //Create and pass arguments bundle
                args.putInt(FieldFragment.PARAM_CONTAINER, FieldFragment.CONTAINER_EMAIL);
                args.putInt(FieldFragment.PARAM_INDEX, listFieldEmails.size() - 1);
                fieldEmail.setArguments(args);
                //Add fragment with unique tag
                getSupportFragmentManager().beginTransaction().add(R.id.edit_emailsContainer,
                        fieldEmail, getString(R.string.frag_tag_email) + fragmentSerial).commit();
                fragmentSerial++;
                break;
            case R.id.edit_addMisc:
                FieldFragment fieldMisc = new FieldFragment();
                listFieldMisc.add(fieldMisc);
                //Create and pass arguments bundle
                args.putInt(FieldFragment.PARAM_CONTAINER, FieldFragment.CONTAINER_MISC);
                args.putInt(FieldFragment.PARAM_INDEX, listFieldMisc.size() - 1);
                fieldMisc.setArguments(args);
                //Add fragment with unique tag
                getSupportFragmentManager().beginTransaction().add(R.id.edit_miscContainer,
                        fieldMisc, getString(R.string.frag_tag_misc) + fragmentSerial).commit();
                fragmentSerial++;
                break;
        }
    }

    @Override
    public void onFragmentDelete(String tag) {
        //Remove fragment, called by fragment itself
        FieldFragment field = (FieldFragment)getSupportFragmentManager().findFragmentByTag(tag);
        switch (field.getContainerType()) {
            //Do not remove the last field in container
            case FieldFragment.CONTAINER_NUMBER:
                if (listFieldNumbers.size() == 1)    return;
                listFieldNumbers.remove(field);
                break;
            case FieldFragment.CONTAINER_EMAIL:
                if (listFieldEmails.size() == 1)    return;
                listFieldEmails.remove(field);
                break;
            case FieldFragment.CONTAINER_MISC:
                if (listFieldMisc.size() == 1)    return;
                listFieldMisc.remove(field);
                break;
        }
        getSupportFragmentManager().beginTransaction().remove(field).commit();
    }
}
