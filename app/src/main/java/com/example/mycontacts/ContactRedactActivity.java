package com.example.mycontacts;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

public class ContactRedactActivity extends AppCompatActivity {
    private EditText editName, editPhone, editEmail;
    private Spinner spinnerPhoneType, spinnerEmailType;
    private Button buttonSave;
    private ImageButton imageButton;
    private static ArrayList<Contact> contacts = ContactData.getContacts();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redact_contact);

        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editEmail = findViewById(R.id.edit_email);
        spinnerPhoneType = findViewById(R.id.spinner_phone_type);
        spinnerEmailType = findViewById(R.id.spinner_email_type);
        buttonSave = findViewById(R.id.button_save);
        imageButton = findViewById(R.id.returnButton);

        ArrayAdapter<CharSequence> phoneAdapter = ArrayAdapter.createFromResource(
                this, R.array.phone_types, android.R.layout.simple_spinner_item);
        phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhoneType.setAdapter(phoneAdapter);

        ArrayAdapter<CharSequence> emailAdapter = ArrayAdapter.createFromResource(
                this, R.array.email_types, android.R.layout.simple_spinner_item);
        emailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmailType.setAdapter(emailAdapter);

        Contact contact = (Contact) getIntent().getSerializableExtra("CONTACT");

        editName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String text = editName.getText().toString();
                String capitalizedText = capitalizeFirstWordOnly(text);
                editName.setText(capitalizedText);
                editName.setSelection(capitalizedText.length());
            }
        });

        editEmail.setFilters(new InputFilter[]{emailFilter, new InputFilter.LengthFilter(50)});

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches() && s.length() > 0) {
                    editEmail.setError("Invalid email format");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        if (contact != null) {
            editName.setText(contact.getName());
            editPhone.setText(contact.getPhoneNumber());
            editEmail.setText(contact.getEmail());
            setSpinnerSelection(spinnerPhoneType, phoneAdapter, contact.getPhoneType());
            setSpinnerSelection(spinnerEmailType, emailAdapter, contact.getEmailType());
        }

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(ContactRedactActivity.this, ContactDetailsActivity.class);
            intent.putExtra("CONTACT", contact);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        buttonSave.setOnClickListener(v -> saveContact());
    }

    private final InputFilter emailFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@.";
            for (int i = start; i < end; i++) {
                if (!allowedCharacters.contains(String.valueOf(source.charAt(i)))) {
                    return "";
                }
            }
            return null;
        }
    };

    private String capitalizeFirstWordOnly(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String[] words = input.trim().split("\\s+");
        if (words.length > 0) {
            words[0] = words[0].substring(0, 1).toUpperCase() + words[0].substring(1).toLowerCase();
        }
        StringBuilder result = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            result.append(" ").append(words[i].toLowerCase());
        }
        return result.toString();
    }

    private void setSpinnerSelection(Spinner spinner, ArrayAdapter<CharSequence> adapter, String value) {
        if (value != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).toString().equalsIgnoreCase(value)) {
                    spinner.setSelection(i);
                    return;
                }
            }
        }
    }

    private void saveContact() {
        Contact contact = (Contact) getIntent().getSerializableExtra("CONTACT");

        if (contact != null) {
            String emailInput = editEmail.getText().toString().trim();
            if (!emailInput.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                editEmail.setError("Invalid email format");
                Toast.makeText(this, "Please enter a valid email!", Toast.LENGTH_SHORT).show();
                return;
            }

            contact.setName(editName.getText().toString());
            contact.setPhoneNumber(editPhone.getText().toString());
            contact.setEmail(emailInput);
            contact.setPhoneType(spinnerPhoneType.getSelectedItem().toString());
            contact.setEmailType(spinnerEmailType.getSelectedItem().toString());

            for (int i = 0; i < ContactData.getContacts().size(); i++) {
                if (ContactData.getContacts().get(i).getId() == contact.getId()) {
                    ContactData.getContacts().set(i, contact);
                    break;
                }
            }
        }

        Intent intent = new Intent(ContactRedactActivity.this, ContactDetailsActivity.class);
        intent.putExtra("CONTACT", contact);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
