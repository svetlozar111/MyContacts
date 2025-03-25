package com.example.mycontacts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ContactDetailsActivity extends AppCompatActivity {
    private TextView contactName, phoneType, phoneNumber, emailType, emailAddress;
    private Button buttonCall, buttonMessage, buttonMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_contact);

        Contact contact = (Contact) getIntent().getSerializableExtra("CONTACT"); // Retrieve the object

        if (contact != null) {
            // Initialize Views
            TextView contactName = findViewById(R.id.contact_name);
            TextView phoneType = findViewById(R.id.phone_type);
            TextView phoneNumber = findViewById(R.id.phone_number);
            TextView emailType = findViewById(R.id.email_type);
            TextView emailAddress = findViewById(R.id.email_address);
            Button buttonCall = findViewById(R.id.button_call);
            Button buttonMessage = findViewById(R.id.button_message);
            Button buttonMail = findViewById(R.id.button_mail);

            // Set Contact Details
            contactName.setText(contact.getName());
            phoneType.setText("Phone Type: " + contact.getPhoneType());
            phoneNumber.setText("Phone Number: " + contact.getPhoneNumber());
            emailType.setText("Email Type: " + contact.getEmailType());
            emailAddress.setText("Email: " + contact.getEmail());

            // Enable/Disable Buttons Based on Data
            buttonCall.setEnabled(!contact.getPhoneNumber().isEmpty());
            buttonMessage.setEnabled(!contact.getPhoneNumber().isEmpty());
            buttonMail.setEnabled(!contact.getEmail().isEmpty());
        }
    }

    private Contact getContactById(int contactId) {
        // Retrieve the contact from the MainActivity list
        return ((MainActivity) getApplicationContext()).getContactById(contactId);
    }
}
