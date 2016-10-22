package com.okason.prontoshop.ui.addCustomer;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.okason.prontoshop.R;
import com.okason.prontoshop.models.Customer;
import com.okason.prontoshop.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCustomerDialogFragment extends DialogFragment implements AddCustomerContract.View{

    private AddCustomerContract.Action mPresenter;
    private boolean mInEditMode = false;

    @BindView(R.id.edit_text_customer_name)  EditText mNameEditText;
    @BindView(R.id.edit_text_customer_email) EditText mEmailEditText;
    @BindView(R.id.edit_text_customer_image_path) EditText mImagePathEditText;
    @BindView(R.id.edit_text_customer_phone) EditText mPhoneEditText;
    @BindView(R.id.edit_text_customer_street_address) EditText mStreet1EditText;
    @BindView(R.id.edit_text_customer_street_address_2) EditText mStreet2EditText;
    @BindView(R.id.edit_text_customer_city) EditText mCityEditText;
    @BindView(R.id.edit_text_customer_state) EditText mStateEditText;
    @BindView(R.id.edit_text_customer_zip_code) EditText mZipEditText;
    @BindView(R.id.edit_text_customer_note) EditText mCustomerNote;


    public static AddCustomerDialogFragment newInstance(long id){
        AddCustomerDialogFragment fragment = new AddCustomerDialogFragment();
        if (id > 0){
            Bundle args = new Bundle();
            args.putLong(Constants.COLUMN_ID, id);
            fragment.setArguments(args);
        }
        return fragment;
    }




    public AddCustomerDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AddCustomerPresenter(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogFragment = new AlertDialog.Builder(getActivity());
        if (savedInstanceState == null){
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View rootView = inflater.inflate(R.layout.fragment_add_customer, null);
            dialogFragment.setView(rootView);
            ButterKnife.bind(this, rootView);

            if (getArguments() != null && getArguments().containsKey(Constants.COLUMN_ID)){
                mPresenter.checkStatus(getArguments().getLong(Constants.COLUMN_ID));
            }

            View titleView = inflater.inflate(R.layout.dialog_title, null);
            TextView titleText = (TextView)titleView.findViewById(R.id.text_view_dialog_title);
            titleText.setText(mInEditMode ? "Edit Customer" : "Add Customer");
            dialogFragment.setCustomTitle(titleView);

            dialogFragment.setPositiveButton(mInEditMode ? "Update" : "Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogFragment.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        }

        return dialogFragment.create();
    }

    @Override
    public void populateForm(Customer customer) {
        mNameEditText.setText(customer.getCustomerName());
        mEmailEditText.setText(customer.getEmailAddress());
        mImagePathEditText.setText(customer.getProfileImagePath());
        mPhoneEditText.setText(customer.getPhoneNumber());
        mStreet1EditText.setText(customer.getStreetAddress());
        mStreet2EditText.setText(customer.getStreetAddress2());
        mCityEditText.setText(customer.getCity());
        mStateEditText.setText(customer.getState());
        mZipEditText.setText(customer.getPostalCode());
        mCustomerNote.setText(customer.getNote());
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setEditMode(boolean editMode) {
        mInEditMode = editMode;
    }

    @Override
    public void clearForm() {

    }


    private boolean validateInputs() {
        if (mNameEditText.getText().toString().isEmpty())
        {
            mNameEditText.setError(getString(R.string.name_is_required));
            mNameEditText.requestFocus();
            return false;
        }

        if (mEmailEditText.getText().toString().isEmpty())
        {
            mEmailEditText.setError(getString(R.string.email_is_required));
            mEmailEditText.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();

        if (d != null){
            Button positiveButton = (Button)d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean readyToCloseDialog = false;
                    if (validateInputs()) {
                        saveCustomer();
                        readyToCloseDialog = true;
                    }
                    if (readyToCloseDialog)
                        dismiss();
                }
            });
        }
    }

    private void saveCustomer() {
        Customer customer = new Customer();
        customer.setCustomerName(mNameEditText.getText().toString());
        customer.setEmailAddress(mEmailEditText.getText().toString());
        customer.setProfileImagePath(mImagePathEditText.getText().toString());
        customer.setPhoneNumber(mPhoneEditText.getText().toString());
        customer.setStreetAddress(mStreet1EditText.getText().toString());
        customer.setStreetAddress2(mStreet2EditText.getText().toString());
        customer.setCity(mCityEditText.getText().toString());
        customer.setState(mStateEditText.getText().toString());
        customer.setPostalCode(mZipEditText.getText().toString());
        customer.setNote(mCustomerNote.getText().toString());
        mPresenter.saveCustomer(customer);

    }

}
