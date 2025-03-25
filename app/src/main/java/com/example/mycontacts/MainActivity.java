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
    private List<Contact> contactList;

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
        return contacts;
    }

    @Override
    public void onCallClick(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "No phone number available", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Calling " + phoneNumber, Toast.LENGTH_SHORT).show();
            // Intent to dial the number can be added here
        }
    }

    @Override
    public void onContactClick(int contactId) {
        Intent intent = new Intent(this, ContactDetailsActivity.class);
        intent.putExtra("CONTACT_ID", contactId);
        startActivity(intent);
    }
}
