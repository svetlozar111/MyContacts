package com.example.mycontacts;

public class Contact {
    private int id;
    private String name;
    private String phoneType;
    private String phoneNumber;
    private String emailType;
    private String email;

    public Contact(int id, String name, String phoneType, String phoneNumber, String emailType, String email) {
        this.id = id;
        this.name = name;
        this.phoneType = phoneType;
        this.phoneNumber = phoneNumber;
        this.emailType = emailType;
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhoneType() { return phoneType; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmailType() { return emailType; }
    public String getEmail() { return email; }
}

