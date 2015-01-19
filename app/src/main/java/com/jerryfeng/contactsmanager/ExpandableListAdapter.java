package com.jerryfeng.contactsmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom adapter for ExpandableListView, handles group and child views
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

    private ArrayList<Contact> contactsList;
    private Context context;
    private int selectedGroup;

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
        //Return empty view if contact list is empty
        if (this.contactsList.size() < 1)   return convertView;

        //Instantiate components
        TextView groupHeaderView = (TextView)convertView.findViewById(R.id.list_group_title);
        groupHeaderView.setText(this.contactsList.get(groupPosition).getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_item, null);
        }

        //Update currently selected group position
        this.selectedGroup = groupPosition;

        //Instantiate containers
        LinearLayout numbersTypeContainer = (LinearLayout)convertView.findViewById(R.id.list_numbersTypeContainer);
        LinearLayout numbersValContainer = (LinearLayout)convertView.findViewById(R.id.list_numbersValContainer);
        LinearLayout emailsTypeContainer = (LinearLayout)convertView.findViewById(R.id.list_emailsTypeContainer);
        LinearLayout emailsValContainer = (LinearLayout)convertView.findViewById(R.id.list_emailsValContainer);
        LinearLayout miscTypeContainer = (LinearLayout)convertView.findViewById(R.id.list_miscTypeContainer);
        LinearLayout miscValContainer = (LinearLayout)convertView.findViewById(R.id.list_miscValContainer);

        //Reset containers
        numbersTypeContainer.removeAllViews();
        numbersValContainer.removeAllViews();
        emailsTypeContainer.removeAllViews();
        emailsValContainer.removeAllViews();
        miscTypeContainer.removeAllViews();
        miscValContainer.removeAllViews();

        //Fill containers
        float dpScale = this.context.getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, (int)(3 * dpScale + 0.5f), 0, (int)(3 * dpScale + 0.5f));
        for (int iNum = 0; iNum < contactsList.get(this.selectedGroup).getNumbersCount(); iNum++) {
            TextView viewType = new TextView(this.context);
            viewType.setTextSize(16);
            viewType.setLayoutParams(lp);
            TextView viewVal = new TextView(this.context);
            viewVal.setTextSize(16);
            viewVal.setLayoutParams(lp);
            viewType.setText(contactsList.get(this.selectedGroup).getNumberType(iNum));
            numbersTypeContainer.addView(viewType);
            viewVal.setText(contactsList.get(this.selectedGroup).getNumberValue(iNum));
            numbersValContainer.addView(viewVal);
        }
        for (int iEm = 0; iEm < contactsList.get(this.selectedGroup).getEmailsCount(); iEm++) {
            TextView viewType = new TextView(this.context);
            viewType.setTextSize(16);
            viewType.setLayoutParams(lp);
            TextView viewVal = new TextView(this.context);
            viewVal.setTextSize(16);
            viewVal.setLayoutParams(lp);
            viewType.setText(contactsList.get(this.selectedGroup).getEmailType(iEm));
            emailsTypeContainer.addView(viewType);
            viewVal.setText(contactsList.get(this.selectedGroup).getEmailValue(iEm));
            emailsValContainer.addView(viewVal);
        }
        for (int iMisc = 0; iMisc < contactsList.get(this.selectedGroup).getMiscCount(); iMisc++) {
            TextView viewType = new TextView(this.context);
            viewType.setTextSize(16);
            viewType.setLayoutParams(lp);
            TextView viewVal = new TextView(this.context);
            viewVal.setTextSize(16);
            viewVal.setLayoutParams(lp);
            viewType.setText(contactsList.get(this.selectedGroup).getMiscType(iMisc));
            miscTypeContainer.addView(viewType);
            viewVal.setText(contactsList.get(this.selectedGroup).getMiscValue(iMisc));
            miscValContainer.addView(viewVal);
        }

        ImageButton buttonCall = (ImageButton)convertView.findViewById(R.id.list_buttonCall);
        buttonCall.setOnClickListener(this);
        ImageButton buttonText = (ImageButton)convertView.findViewById(R.id.list_buttonText);
        buttonText.setOnClickListener(this);

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
                AlertDialog.Builder alertCall = new AlertDialog.Builder(this.context);
                alertCall.setTitle("Please choose a number to call");
                //Create list for user to choose a number
                alertCall.setMultiChoiceItems(this.contactsList.get(selectedGroup).getNumberValues(), null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                //Get intent for calling
                                Intent intentCall = new Intent(Intent.ACTION_DIAL);
                                intentCall.setData(Uri.parse("tel:" +
                                        contactsList.get(selectedGroup).getNumberValues()[which]));
                                if (intentCall.resolveActivity(context.getPackageManager()) != null) {
                                    context.startActivity(intentCall);
                                }
                                dialog.dismiss();
                            }
                        });
                alertCall.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                alertCall.show();
                break;
            case R.id.list_buttonText:
                AlertDialog.Builder alertText = new AlertDialog.Builder(this.context);
                alertText.setTitle("Please choose a number to message");
                //Create list for user to choose a number
                alertText.setMultiChoiceItems(this.contactsList.get(selectedGroup).getNumberValues(), null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                //Get intent for texting
                                Intent intentText = new Intent(Intent.ACTION_SENDTO);
                                intentText.setData(Uri.parse("smsto:" +
                                        contactsList.get(selectedGroup).getNumberValues()[which]));
                                if (intentText.resolveActivity(context.getPackageManager()) != null) {
                                    context.startActivity(intentText);
                                }
                                dialog.dismiss();
                            }
                        });
                alertText.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                alertText.show();
                break;
        }
    }
}