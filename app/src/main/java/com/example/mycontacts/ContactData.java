package com.example.mycontacts;

import java.util.ArrayList;

public class ContactData {
    private static ArrayList<Contact> contacts = new ArrayList<>();

    static {
        contacts.add(new Contact(1, "Alice", "mobile", "123456789", "work", "alice@example.com", "https://images.pexels.com/photos/674010/pexels-photo-674010.jpeg"));
        contacts.add(new Contact(2, "Bob", "mobile", "", "home", "bob@example.com", ""));
        contacts.add(new Contact(3, "Charlie", "home", "987654321", "", "", ""));
        contacts.add(new Contact(4, "David", "work", "555555555", "work", "david@example.com", ""));
        contacts.add(new Contact(5, "Emma", "mobile", "666666666", "", "", ""));
        contacts.add(new Contact(6, "effa", "mobile", "666666666", "", "", ""));
        contacts.add(new Contact(7, "gemma", "mobile", "666666666", "", "", ""));
    }

    public static ArrayList<Contact> getContacts() {
        return contacts;
    }
}
