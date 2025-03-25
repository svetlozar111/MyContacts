package com.example.mycontacts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ContactDetailsActivity extends AppCompatActivity {

    private TextView nameTextView, phoneTypeTextView, phoneNumberTextView, emailTypeTextView, emailTextView;
    private Button callButton, messageButton, mailButton;
    private ImageView contactIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_contact);

        nameTextView = findViewById(R.id.contact_name);
        phoneTypeTextView = findViewById(R.id.phone_type);
        phoneNumberTextView = findViewById(R.id.phone_number);
        emailTypeTextView = findViewById(R.id.email_type);
        emailTextView = findViewById(R.id.email_address);
        contactIcon = findViewById(R.id.contact_icon);

        callButton = findViewById(R.id.button_call);
        messageButton = findViewById(R.id.button_message);
        mailButton = findViewById(R.id.button_mail);

        // Get contact ID passed via intent
        Intent intent = getIntent();
        int contactId = intent.getIntExtra("contact_id", -1);

        if (contactId != -1) {
            // Fetch the contact from the contact list or data source using the contact ID
            Contact contact = getContactById(contactId);

            // Update UI with contact details
            nameTextView.setText(contact.getName());
            phoneTypeTextView.setText("Phone Type: " + contact.getPhoneType());
            phoneNumberTextView.setText("Phone Number: " + contact.getPhoneNumber());
            emailTypeTextView.setText("Email Type: " + contact.getEmailType());
            emailTextView.setText("Email: " + contact.getEmail());

            // Enable buttons based on availability of phone number and email
            if (!contact.getPhoneNumber().isEmpty()) {
                callButton.setEnabled(true);
                callButton.setOnClickListener(v -> dialPhoneNumber(contact.getPhoneNumber()));
            }
            if (!contact.getEmail().isEmpty()) {
                mailButton.setEnabled(true);
                mailButton.setOnClickListener(v -> sendEmail(contact.getEmail()));
            }
        }
    }

    private Contact getContactById(int contactId) {
        // Fetch contact by ID from data source
        // This is just a sample; replace it with your actual data retrieval logic
        // Example:
        // return contactList.get(contactId);
        return new Contact(contactId, "Alice", "mobile", "123456789", "work", "alice@example.com");
    }

    private void dialPhoneNumber(String phoneNumber) {
        // Trigger a phone call (intent to dial)
    }

    private void sendEmail(String email) {
        // Trigger an email (intent to send email)
    }
}
