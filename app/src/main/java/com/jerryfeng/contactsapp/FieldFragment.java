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

    //Constants to assign input types to container types
    public static final int CONTAINER_NUMBER = InputType.TYPE_CLASS_PHONE;
    public static final int CONTAINER_EMAIL = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
    public static final int CONTAINER_MISC = InputType.TYPE_CLASS_TEXT;

    //Takes in parameters for the type of parent container it is in, its position index
    //in that parent container, value of the type field (if it exists), and value of the
    //value field (if it exists)
    public static final String PARAM_CONTAINER = "param_container";
    public static final String PARAM_INDEX = "param_index";
    public static final String PARAM_TYPE = "param_type";
    public static final String PARAM_VALUE = "param_value";

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

        //Read arguments & instantiate components
        Bundle args = getArguments();
        this.containerType = args.getInt(PARAM_CONTAINER);
        this.indexInContainer = args.getInt(PARAM_INDEX);

        fieldType = (EditText)v.findViewById((R.id.frag_fieldType));
        fieldType.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        if (args.getString(PARAM_TYPE) != null) {
            fieldType.setText(args.getString(PARAM_TYPE));
        }
        fieldType.setHint(getString(R.string.hint_type));

        fieldValue = (EditText)v.findViewById(R.id.frag_fieldValue);
        fieldValue.setInputType(this.containerType);
        if (args.getString(PARAM_VALUE) != null) {
            fieldValue.setText(args.getString(PARAM_VALUE));
        }

        deleteField = (TextView)v.findViewById(R.id.addNew_delete);
        deleteField.setOnClickListener(this);

        //Provide suggestions for type fields, depending on the field's position in its container
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

    protected int getContainerType() {
        return this.containerType;
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
