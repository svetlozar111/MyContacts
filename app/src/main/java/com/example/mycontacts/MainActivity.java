package com.example.mycontacts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        contactList = ContactData.getContacts();

        adapter = new ContactAdapter(contactList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCallClick(String phoneNumber) {
        if (!phoneNumber.isEmpty()) {
            Toast.makeText(this, "Calling: " + phoneNumber, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No phone number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onContactClick(int contactId) {
        Contact selectedContact = getContactById(contactId);
        if (selectedContact != null) {
            Intent intent = new Intent(this, ContactDetailsActivity.class);
            intent.putExtra("CONTACT", selectedContact);
            startActivity(intent);
        }
    }

    public Contact getContactById(int contactId) {
        for (Contact contact : contactList) {
            if (contact.getId() == contactId) {
                return contact;
            }
        }
        return null;
    }

}
