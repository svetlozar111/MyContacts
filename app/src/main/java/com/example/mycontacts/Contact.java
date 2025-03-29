package com.example.mycontacts;
import java.io.Serializable;


public class Contact implements Serializable {
    private int id;
    private String name;
    private String phoneType;
    private String phoneNumber;
    private String emailType;
    private String email;
    private String imageUrl;

    public Contact(int id, String name, String phoneType, String phoneNumber, String emailType, String email, String imageUrl) {
        this.id = id;
        this.name = name;
        this.phoneType = phoneType;
        this.phoneNumber = phoneNumber;
        this.emailType = emailType;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
