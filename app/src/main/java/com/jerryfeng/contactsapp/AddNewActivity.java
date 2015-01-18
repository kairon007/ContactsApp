package com.jerryfeng.contactsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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


public class AddNewActivity extends ActionBarActivity implements View.OnClickListener,
        FieldFragment.OnFragmentDeleteListener {

    //Widgets
    ImageButton photo;
    EditText fieldName;
    TextView buttonAddNumber, buttonAddEmail, buttonAddMisc;

    //Fragment components
    private ArrayList<FieldFragment> listFieldNumbers, listFieldEmails, listFieldMisc;
    private int fragmentSerial;

    //Keys for savedInstanceState
    private static final String SAVE_FRAGMENT_SERIAL = "save_fragment_serial";

    private static final String SAVE_NUMBERS_TYPE = "save_numbers_type";
    private static final String SAVE_NUMBERS_VALUE = "save_numbers_value";
    private static final String SAVE_EMAILS_TYPE = "save_emails_type";
    private static final String SAVE_EMAILS_VALUE = "save_emails_value";
    private static final String SAVE_MISC_TYPE = "save_misc_type";
    private static final String SAVE_MISC_VALUE = "save_misc_value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        //Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Instantiating components
        fieldName = (EditText)findViewById(R.id.addNew_fieldName);
        buttonAddNumber = (TextView)findViewById(R.id.addNew_addNumber);
        buttonAddNumber.setOnClickListener(this);
        buttonAddEmail = (TextView)findViewById(R.id.addNew_addEmail);
        buttonAddEmail.setOnClickListener(this);
        buttonAddMisc = (TextView)findViewById(R.id.addNew_addMisc);
        buttonAddMisc.setOnClickListener(this);

        //Initialize fragment components
        listFieldNumbers = new ArrayList<FieldFragment>();
        listFieldEmails = new ArrayList<FieldFragment>();
        listFieldMisc = new ArrayList<FieldFragment>();
        fragmentSerial = 0;

        //Provide initial fields
        onClick(findViewById(R.id.addNew_addNumber));
        onClick(findViewById(R.id.addNew_addEmail));
        onClick(findViewById(R.id.addNew_addMisc));

    }

    //Handling screen config changes
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //TODO-Implement saving instance state for AddNewActivity
        /*
        savedInstanceState.putInt(SAVE_FRAGMENT_SERIAL, fragmentSerial);
        ArrayList<String> saveNumberTypes = new ArrayList<String>();
        ArrayList<String> saveNumberValues = new ArrayList<String>();
        for (int i = 0; i < this.listFieldNumbers.size(); i++) {
            saveNumberTypes.add(this.listFieldNumbers.get(i).getFieldType());
            saveNumberValues.add(this.listFieldNumbers.get(i).getFieldValue());
        }
        savedInstanceState.putStringArrayList(SAVE_NUMBERS_TYPE, saveNumberTypes);
        savedInstanceState.putStringArrayList(SAVE_NUMBERS_VALUE, saveNumberValues);

        ArrayList<String> saveEmailTypes = new ArrayList<String>();
        ArrayList<String> saveEmailValues = new ArrayList<String>();
        for (int i = 0; i < this.listFieldEmails.size(); i++) {
            saveEmailTypes.add(this.listFieldEmails.get(i).getFieldType());
            saveEmailValues.add(this.listFieldEmails.get(i).getFieldValue());
        }
        savedInstanceState.putStringArrayList(SAVE_EMAILS_TYPE, saveEmailTypes);
        savedInstanceState.putStringArrayList(SAVE_EMAILS_VALUE, saveEmailValues);

        ArrayList<String> saveMiscTypes = new ArrayList<String>();
        ArrayList<String> saveMiscValues = new ArrayList<String>();
        for (int i = 0; i < this.listFieldMisc.size(); i++) {
            saveMiscTypes.add(this.listFieldMisc.get(i).getFieldType());
            saveMiscValues.add(this.listFieldMisc.get(i).getFieldValue());
        }
        savedInstanceState.putStringArrayList(SAVE_MISC_TYPE, saveMiscTypes);
        savedInstanceState.putStringArrayList(SAVE_MISC_VALUE, saveMiscValues);*/
    }

    //Handling screen config changes
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //fragmentSerial = savedInstanceState.getInt(SAVE_FRAGMENT_SERIAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:     //Up button
                NavUtils.navigateUpFromSameTask(this);
                return true;

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

                Contact newContact = new Contact();
                ContactsDataHandler db = new ContactsDataHandler(this);
                //save name field
                newContact.setName(fieldName.getText().toString());
                //set unique ID for each new contact
                newContact.setID(db.getNextAvailableID());
                //save number fields
                for (int iNum = 0; iNum < listFieldNumbers.size(); iNum++) {
                    String fieldType = listFieldNumbers.get(iNum).getFieldType();
                    String fieldValue = listFieldNumbers.get(iNum).getFieldValue();
                    if (fieldType.length() > 0 && fieldValue.length() > 0) {
                        newContact.addNumber(fieldType, fieldValue);
                    }
                }
                //save email fields
                for (int iEm = 0; iEm < listFieldEmails.size(); iEm++) {
                    String fieldType = listFieldEmails.get(iEm).getFieldType();
                    String fieldValue = listFieldEmails.get(iEm).getFieldValue();
                    if (fieldType.length() > 0 && fieldValue.length() > 0) {
                        newContact.addEmail(fieldType, fieldValue);
                    }
                }
                //save misc fields
                for (int iMisc = 0; iMisc < listFieldMisc.size(); iMisc++) {
                    String fieldType = listFieldMisc.get(iMisc).getFieldType();
                    String fieldValue = listFieldMisc.get(iMisc).getFieldValue();
                    if (fieldType.length() > 0 && fieldValue.length() > 0) {
                        newContact.addMisc(fieldType, fieldValue);
                    }
                }
                db.addContact(newContact);
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
            case R.id.addNew_addNumber:
                FieldFragment fieldNumber = new FieldFragment();
                listFieldNumbers.add(fieldNumber);
                //Create and pass arguments bundle
                args.putInt(FieldFragment.PARAM_CONTAINER, FieldFragment.CONTAINER_NUMBER);
                args.putInt(FieldFragment.PARAM_INDEX, listFieldNumbers.size() - 1);
                fieldNumber.setArguments(args);
                //Add fragment with unique tag
                getSupportFragmentManager().beginTransaction().add(R.id.addNew_numbersContainer,
                        fieldNumber, getString(R.string.frag_tag_number) + fragmentSerial).commit();
                fragmentSerial++;
                break;
            case R.id.addNew_addEmail:
                FieldFragment fieldEmail = new FieldFragment();
                listFieldEmails.add(fieldEmail);
                //Create and pass arguments bundle
                args.putInt(FieldFragment.PARAM_CONTAINER, FieldFragment.CONTAINER_EMAIL);
                args.putInt(FieldFragment.PARAM_INDEX, listFieldEmails.size() - 1);
                fieldEmail.setArguments(args);
                //Add fragment with unique tag
                getSupportFragmentManager().beginTransaction().add(R.id.addNew_emailsContainer,
                        fieldEmail, getString(R.string.frag_tag_email) + fragmentSerial).commit();
                fragmentSerial++;
                break;
            case R.id.addNew_addMisc:
                FieldFragment fieldMisc = new FieldFragment();
                listFieldMisc.add(fieldMisc);
                //Create and pass arguments bundle
                args.putInt(FieldFragment.PARAM_CONTAINER, FieldFragment.CONTAINER_MISC);
                args.putInt(FieldFragment.PARAM_INDEX, listFieldMisc.size() - 1);
                fieldMisc.setArguments(args);
                //Add fragment with unique tag
                getSupportFragmentManager().beginTransaction().add(R.id.addNew_miscContainer,
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
