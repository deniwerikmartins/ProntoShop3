package com.okason.prontoshop.models;

import android.database.Cursor;

import com.okason.prontoshop.util.Constants;

/**
 * Created by Valentine on 4/6/2016.
 */
public class Customer {
    private long id;
    private String customerName;
    private String emailAddress;
    private String phoneNumber;
    private String profileImagePath;
    private String streetAddress;
    private String streetAddress2;
    private String city;
    private String state;
    private String postalCode;
    private String note;
    private long dateAdded;
    private long dateOfLastTransaction;



    public Customer(){
        id = 0;
        customerName = "";
        emailAddress = "";
        phoneNumber = "";
        profileImagePath = "empty";
        streetAddress = "";
        streetAddress2 = "";
        city = "";
        state = "";
        postalCode = "";
        note = "";
        dateAdded = 0L;
        dateOfLastTransaction = 0L;


    }

    public Customer(long customerId, String name, String email, String imagePath, String phone, String street1, String street2,
                    String customerCity, String customerState, String zip, String customerNote, long createDate, long modifiedDate){
        id  = customerId;
        customerName = name;
        emailAddress = email;
        profileImagePath = imagePath;
        phoneNumber = phone;
        streetAddress = street1;
        streetAddress2 = street2;
        city = customerCity;
        state = customerState;
        postalCode = zip;
        note = customerNote;
        dateAdded = createDate;
        dateOfLastTransaction = modifiedDate;

    }

    public static Customer getCustomerFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME));
        String email = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EMAIL));
        String imagePath = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_IMAGE_PATH));
        String phone = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_PHONE));
        String street1 = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_STREET1));
        String street2 = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_STREET2));
        String city = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CITY));
        String state = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_STATE));
        String zip = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_STREET1));
        String note = cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NOTE));
        long createDate = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_DATE_CREATED));
        long modifiedDate = cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_LAST_UPDATED));

        Customer customer = new Customer(id, name, email, imagePath, phone, street1, street2, city, state, zip, note, createDate, modifiedDate);
        return customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getDateOfLastTransaction() {
        return dateOfLastTransaction;
    }

    public void setDateOfLastTransaction(long dateOfLastTransaction) {
        this.dateOfLastTransaction = dateOfLastTransaction;
    }


}
