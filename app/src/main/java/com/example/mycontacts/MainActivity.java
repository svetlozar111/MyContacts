package com.example.mycontacts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private List<Contact> contactList; // Store contacts here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_contacts);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactList = getSampleContacts(); // Load static contacts

        adapter = new ContactAdapter(contactList, this);
        recyclerView.setAdapter(adapter);
    }

    private List<Contact> getSampleContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1, "Alice", "mobile", "123456789", "work", "alice@example.com"));
        contacts.add(new Contact(2, "Bob", "mobile", "", "home", "bob@example.com"));
        contacts.add(new Contact(3, "Charlie", "home", "987654321", "", ""));
        contacts.add(new Contact(4, "David", "work", "555555555", "work", "david@example.com"));
        contacts.add(new Contact(5, "Emma", "mobile", "666666666", "", ""));
        contacts.add(new Contact(6, "effa", "mobile", "666666666", "", ""));
        contacts.add(new Contact(7, "gemma", "mobile", "666666666", "", ""));
        return contacts;
    }

    @Override
    public void onCallClick(String phoneNumber) {

    }

    @Override
    public void onContactClick(int contactId) {
        Contact selectedContact = getContactById(contactId);
        if (selectedContact != null) {
            Intent intent = new Intent(this, ContactDetailsActivity.class);
            intent.putExtra("CONTACT", selectedContact);  // Pass the entire object
            startActivity(intent);
        }
    }

    public Contact getContactById(int contactId) {
        for (Contact contact : contactList) {
            if (contact.getId() == contactId) {
                return contact;
            }
        }
        return null; // If contact is not found
    }
}
