package com.jerryfeng.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom adapter for ExpandableListView
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

    private ArrayList<Contact> contactsList;
    private Context context;
    int selectedGroup;

    public ExpandableListAdapter(Context context, ArrayList<Contact> list) {
        this.context = context;
        this.contactsList = (ArrayList<Contact>)list.clone();
        this.selectedGroup = -1;
    }

    @Override
    public int getGroupCount() {
        return this.contactsList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.contactsList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.contactsList.get(groupPosition);
        /*switch (childPosition) {
            case 0: return this.contactsList.get(groupPosition).imageSrc;
            case 1: return this.contactsList.get(groupPosition).name;
            case 2: return this.contactsList.get(groupPosition).number;
            case 3: return this.contactsList.get(groupPosition).email;
            default: return null;
        }*/
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        //Instantiate components
        TextView groupHeaderView = (TextView)convertView.findViewById(R.id.list_group_textview);
        groupHeaderView.setText(this.contactsList.get(groupPosition).name);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        //Instantiate components
        TextView childNameView = (TextView)convertView.findViewById(R.id.list_item_name);
        childNameView.setText(this.contactsList.get(groupPosition).name);

        TextView childNumberView = (TextView)convertView.findViewById(R.id.list_item_number);
        childNumberView.setText(this.contactsList.get(groupPosition).number);

        TextView childEmailView = (TextView)convertView.findViewById(R.id.list_item_email);
        childEmailView.setText(this.contactsList.get(groupPosition).email);

        Button buttonCall = (Button)convertView.findViewById(R.id.list_buttonCall);
        buttonCall.setOnClickListener(this);

        Button buttonText = (Button)convertView.findViewById(R.id.list_buttonText);
        buttonText.setOnClickListener(this);

        //Update currently selected group position
        this.selectedGroup = groupPosition;

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_buttonCall:
                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:" + this.contactsList.get(selectedGroup).number));
                if (intentCall.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intentCall);
                }
                break;
            case R.id.list_buttonText:
                Intent intentText = new Intent(Intent.ACTION_SENDTO);
                intentText.setData(Uri.parse("smsto:" + this.contactsList.get(selectedGroup).number));
                if (intentText.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intentText);
                }
                break;
        }
    }
}
