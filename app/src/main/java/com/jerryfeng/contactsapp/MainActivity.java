package com.jerryfeng.contactsapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    //Widgets
    ImageView previewImage;
    TextView previewName, previewNumber;
    Button buttonAdd, buttonEdit, buttonDelete;
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

        buttonAdd = (Button)findViewById(R.id.main_buttonAdd);
        buttonAdd.setOnClickListener(this);

        buttonEdit = (Button)findViewById(R.id.main_buttonEdit);
        buttonEdit.setOnClickListener(this);

        buttonDelete = (Button)findViewById(R.id.main_buttonDelete);
        buttonDelete.setOnClickListener(this);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_buttonAdd:
                break;
            case R.id.main_buttonEdit:
                break;
            case R.id.main_buttonDelete:
                break;
        }
    }
}
