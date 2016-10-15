package com.okason.prontoshop.models;

/**
 * Created by Valentine on 4/6/2016.
 */
public class Retailer {
    private long id;
    private String businessName;
    private String emailAddress;
    private String phoneNumber;
    private String streetAddress1;
    private String streetAddress2;
    private String city;
    private String state;
    private String zip;
    private String industry;
    private String contactPerson;

    public Retailer(){}

    public Retailer(long id, String name, String email, String phone, String street1,
                    String street2, String city, String state, String zip, String industry, String contact){
        this.id = id;
        this.businessName = name;
        this.emailAddress = email;
        this.phoneNumber = phone;
        this.streetAddress1 = street1;
        this.streetAddress2 = street2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.industry = industry;
        this.contactPerson = contact;
    }

}
