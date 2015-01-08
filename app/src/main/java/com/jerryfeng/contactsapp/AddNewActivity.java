package com.jerryfeng.contactsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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

    //Fragments
    ArrayList<FieldFragment> listFieldNumber, listFieldEmail, listFieldMisc;
    int fragmentCount;

    //Saving persistent data
    SharedPreferences sharedPrefTypes, sharedPrefValues;

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
        listFieldNumber = new ArrayList<FieldFragment>();
        listFieldEmail = new ArrayList<FieldFragment>();
        listFieldMisc = new ArrayList<FieldFragment>();
        fragmentCount = 0;

        //Initialize fields
        if (findViewById(R.id.addNew_numbersContainer) != null
                && findViewById(R.id.addNew_emailsContainer) != null
                && findViewById(R.id.addNew_miscContainer) != null
                && savedInstanceState == null) {
            onClick(findViewById(R.id.addNew_addNumber));
            onClick(findViewById(R.id.addNew_addEmail));
            onClick(findViewById(R.id.addNew_addMisc));
        }

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

                sharedPrefTypes = getSharedPreferences(getString(R.string.pref_types_key), Context.MODE_PRIVATE);
                sharedPrefValues = getSharedPreferences(getString(R.string.pref_values_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor eTypes = sharedPrefTypes.edit();
                SharedPreferences.Editor eValues = sharedPrefValues.edit();

                //update length of list
                int contactCount = sharedPrefTypes.getInt(getString(R.string.pref_listLength), 0) + 1;
                eTypes.putInt(getString(R.string.pref_listLength), contactCount);
                //save name field
                eValues.putString(getString(R.string.pref_name) + "_" + (contactCount - 1),
                        fieldName.getText().toString());
                //save number fields
                int nonEmptyFields = 0;
                for (int iNum = 0; iNum < listFieldNumber.size(); iNum++) {
                    String fieldType = listFieldNumber.get(iNum).getFieldType();
                    String fieldValue = listFieldNumber.get(iNum).getFieldValue();
                    if (fieldType.length() > 0 && fieldValue.length() > 0) {
                        eTypes.putString(getString(R.string.pref_number) + "_" + (contactCount - 1)
                                + "_" + nonEmptyFields, fieldType);
                        eValues.putString(getString(R.string.pref_number) + "_" + (contactCount - 1)
                                + "_" + nonEmptyFields, fieldValue);
                        nonEmptyFields++;
                    }
                }
                eTypes.putInt(getString(R.string.pref_numofnumbers) + "_" + (contactCount - 1),
                        nonEmptyFields);

                //save email fields
                nonEmptyFields = 0;

                for (int iEm = 0; iEm < listFieldEmail.size(); iEm++) {
                    String fieldType = listFieldEmail.get(iEm).getFieldType();
                    String fieldValue = listFieldEmail.get(iEm).getFieldValue();
                    if (fieldType.length() > 0 && fieldValue.length() > 0) {
                        eTypes.putString(getString(R.string.pref_email) + "_" + (contactCount - 1)
                                + "_" + nonEmptyFields, fieldType);
                        eValues.putString(getString(R.string.pref_email) + "_" + (contactCount - 1)
                                + "_" + nonEmptyFields, fieldValue);
                        nonEmptyFields++;
                    }
                }
                eTypes.putInt(getString(R.string.pref_numofemails) + "_" + (contactCount - 1),
                        nonEmptyFields);

                //save misc fields
                nonEmptyFields = 0;
                for (int iMisc = 0; iMisc < listFieldMisc.size(); iMisc++) {
                    String fieldType = listFieldMisc.get(iMisc).getFieldType();
                    String fieldValue = listFieldMisc.get(iMisc).getFieldValue();
                    if (fieldType.length() > 0 && fieldValue.length() > 0) {
                        eTypes.putString(getString(R.string.pref_misc) + "_" + (contactCount - 1)
                                + "_" + nonEmptyFields, fieldType);
                        eValues.putString(getString(R.string.pref_misc) + "_" + (contactCount - 1)
                                + "_" + nonEmptyFields, fieldValue);
                        nonEmptyFields++;
                    }
                }
                eTypes.putInt(getString(R.string.pref_numofmisc) + "_" + (contactCount - 1),
                        nonEmptyFields);

                eTypes.commit();
                eValues.commit();

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNew_addNumber:
                FieldFragment fieldNumber = new FieldFragment();
                listFieldNumber.add(fieldNumber);
                //Should use argument bundle to pass info instead?
                fieldNumber.setContainerType(FieldFragment.CONTAINER_NUMBER);
                fieldNumber.setIndexInContainer(listFieldNumber.size() - 1);

                //Add fragment with unique tag
                getSupportFragmentManager().beginTransaction().add(R.id.addNew_numbersContainer,
                        fieldNumber, getString(R.string.frag_tag_number) + fragmentCount).commit();
                fragmentCount++;
                break;
            case R.id.addNew_addEmail:
                FieldFragment fieldEmail = new FieldFragment();
                listFieldEmail.add(fieldEmail);
                //Should use argument bundle to pass info instead?
                fieldEmail.setContainerType(FieldFragment.CONTAINER_EMAIL);
                fieldEmail.setIndexInContainer(listFieldEmail.size() - 1);

                //Add fragment with unique tag
                getSupportFragmentManager().beginTransaction().add(R.id.addNew_emailsContainer,
                        fieldEmail, getString(R.string.frag_tag_email) + fragmentCount).commit();
                fragmentCount++;
                break;
            case R.id.addNew_addMisc:
                FieldFragment fieldMisc = new FieldFragment();
                listFieldMisc.add(fieldMisc);
                //Should use argument bundle to pass info instead?
                fieldMisc.setContainerType(FieldFragment.CONTAINER_MISC);
                fieldMisc.setIndexInContainer(listFieldMisc.size() - 1);

                //Add fragment with unique tag
                getSupportFragmentManager().beginTransaction().add(R.id.addNew_miscContainer,
                        fieldMisc, getString(R.string.frag_tag_misc) + fragmentCount).commit();
                fragmentCount++;
                break;
        }
    }

    @Override
    public void onFragmentDelete(String tag) {
        //Remove fragment, called by fragment itself
        FieldFragment fragField = (FieldFragment)getSupportFragmentManager().findFragmentByTag(tag);
        switch (fragField.getContainerType()) {
            case FieldFragment.CONTAINER_NUMBER:
                //Do not remove the last field in container
                if (listFieldNumber.size() == 1)    return;
                listFieldNumber.remove(fragField);
                break;
            case FieldFragment.CONTAINER_EMAIL:
                //Do not remove the last field in container
                if (listFieldEmail.size() == 1)    return;
                listFieldEmail.remove(fragField);
                break;
            case FieldFragment.CONTAINER_MISC:
                //Do not remove the last field in container
                if (listFieldMisc.size() == 1)    return;
                listFieldMisc.remove(fragField);
                break;
        }
        getSupportFragmentManager().beginTransaction().remove(fragField).commit();

        Log.d("delete", "index: " + fragField.getIndexInContainer() +"count: " + fragmentCount);
    }
}
