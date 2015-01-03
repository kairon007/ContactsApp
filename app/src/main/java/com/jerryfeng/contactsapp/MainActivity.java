package com.jerryfeng.contactsapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    //Widgets
    ImageView previewImage;
    TextView previewName, previewNumber;
    ImageButton buttonCall, buttonText;
    ListView contactsList;


    //Saving activity states for change in screen config etc.
    static final String STATE_PREVIEWNAME = "previewName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiating components

        previewName = (TextView)findViewById(R.id.main_previewName);

        previewNumber = (TextView)findViewById(R.id.main_previewNumber);

        buttonCall = (ImageButton)findViewById(R.id.main_buttonCall);
        buttonCall.setOnClickListener(this);
        buttonText = (ImageButton)findViewById(R.id.main_buttonText);
        buttonText.setOnClickListener(this);

        contactsList = (ListView)findViewById(R.id.main_contactsList);
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
                
                return true;
            case R.id.menu_edit:
                return true;
            case R.id.menu_delete:
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
}
