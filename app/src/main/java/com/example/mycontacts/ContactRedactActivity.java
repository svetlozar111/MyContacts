package com.example.mycontacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
public class ContactRedactActivity extends AppCompatActivity {
    private EditText editName, editPhone, editEmail;
    private Spinner spinnerPhoneType, spinnerEmailType;
    private Button buttonSave;
    private int contactId;
    private static ArrayList<Contact> contacts = ContactData.getContacts(); // Static container

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redact_contact);

        // Initialize views
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editEmail = findViewById(R.id.edit_email);
        spinnerPhoneType = findViewById(R.id.spinner_phone_type);
        spinnerEmailType = findViewById(R.id.spinner_email_type);
        buttonSave = findViewById(R.id.button_save);

        // Get contact ID from intent
        contactId = getIntent().getIntExtra("CONTACT_ID", -1);

        // Load contact details
        Contact contact = getContactById(contactId);
        if (contact != null) {
            editName.setText(contact.getName());
            editPhone.setText(contact.getPhoneNumber());
            editEmail.setText(contact.getEmail());
        }

        // Save button click event
        buttonSave.setOnClickListener(v -> saveContact());
    }

    private Contact getContactById(int id) {
        for (Contact contact : contacts) {
            if (contact.getId() == id) {
                return contact;
            }
        }
        return null;
    }

    private void saveContact() {
        Contact contact = getContactById(contactId);
        if (contact != null) {
            contact.setName(editName.getText().toString());
            contact.setPhoneNumber(editPhone.getText().toString());
            contact.setEmail(editEmail.getText().toString());
        }
        finish(); // Close activity
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v != null && (v instanceof EditText || v instanceof TextInputEditText)) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    hideKeyboard(v);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
