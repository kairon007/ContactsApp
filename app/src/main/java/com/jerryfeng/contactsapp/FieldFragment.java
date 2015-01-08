package com.jerryfeng.contactsapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class FieldFragment extends Fragment implements View.OnClickListener {

    private OnFragmentDeleteListener deleteListener;

    EditText fieldValue, fieldType;
    TextView deleteField;
    private int containerType;
    private int indexInContainer;

    public static final int CONTAINER_NUMBER = InputType.TYPE_CLASS_PHONE;
    public static final int CONTAINER_EMAIL = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
    public static final int CONTAINER_MISC = InputType.TYPE_CLASS_TEXT;

    public FieldFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_field, container, false);

        //read input type from arguments, instantiate components

        fieldType = (EditText)v.findViewById((R.id.frag_fieldType));
        fieldType.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        fieldValue = (EditText)v.findViewById(R.id.frag_fieldValue);
        fieldValue.setInputType(this.containerType);
        deleteField = (TextView)v.findViewById(R.id.addNew_delete);
        deleteField.setOnClickListener(this);

        fieldType.setHint(getString(R.string.hint_type));
        switch(this.containerType) {
            case CONTAINER_NUMBER:
                switch (this.indexInContainer) {
                    case 0: fieldType.setText("Mobile"); break;
                    case 1: fieldType.setText("Home"); break;
                    case 2: fieldType.setText("Work");
                }
                fieldValue.setHint(getString(R.string.hint_number));
                break;
            case CONTAINER_EMAIL:
                switch (this.indexInContainer) {
                    case 0: fieldType.setText("Work"); break;
                    case 1: fieldType.setText("Personal");
                }
                fieldValue.setHint(getString(R.string.hint_email));
                break;
            case CONTAINER_MISC:
                fieldValue.setHint(getString(R.string.hint_misc));
        }

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            deleteListener = (OnFragmentDeleteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentDeleteListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        deleteListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addNew_delete) {
            if (deleteListener != null) {
                deleteListener.onFragmentDelete(getTag());
            }
        }
    }

    public interface OnFragmentDeleteListener {
        public void onFragmentDelete(String tag);
    }

    protected void setContainerType(int inputType) {
        this.containerType = inputType;
    }

    protected int getContainerType() {
        return this.containerType;
    }

    protected void setIndexInContainer(int index) {
        this.indexInContainer = index;
    }

    protected int getIndexInContainer() {
        return this.indexInContainer;
    }

    protected String getFieldType() {
        return fieldType.getText().toString();
    }

    protected String getFieldValue() {
        return fieldValue.getText().toString();
    }
}
