package com.okason.prontoshop.common;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.okason.prontoshop.R;
import com.okason.prontoshop.data.DatabaseHelper;
import com.okason.prontoshop.models.Customer;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mEmailSignInButton;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDatabase;
    private Customer mCustomer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mDbHelper = DatabaseHelper.newInstance(this);
        mDatabase = mDbHelper.getWritableDatabase();
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                processLogin();
            }
        });


    }

    private void processLogin() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError("Required field");
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Required field");
        } else if (!isEmailValid(email)) {
            mEmailView.setError("Invalid Email");
        }

        mCustomer = getCustomerWithThisEmail(email);
        if (mCustomer != null) {
            Toast.makeText(this, "Hello " + mCustomer.getCustomerName(), Toast.LENGTH_SHORT);
        } else {
            registerUser(email, password, mDatabase);
        }

    }

    private void registerUser(String email, String password, SQLiteDatabase mDatabase) {


    }

    private Customer getCustomerWithThisEmail(String email) {
        return null;
    }




    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


}

