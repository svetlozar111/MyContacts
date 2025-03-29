package com.example.mycontacts;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ContactRedactActivity extends AppCompatActivity {
    private EditText editName, editPhone, editEmail;
    private Spinner spinnerPhoneType, spinnerEmailType;
    private Button buttonSave;
    private ImageButton imageButton;
    private static ArrayList<Contact> contacts = ContactData.getContacts();
    private ImageView contactIcon;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Contact contact;
    private TextView characterCountTextView;
    private int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redact_contact);
        characterCountTextView = findViewById(R.id.characterCountTextView);
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editEmail = findViewById(R.id.edit_email);
        spinnerPhoneType = findViewById(R.id.spinner_phone_type);
        spinnerEmailType = findViewById(R.id.spinner_email_type);
        buttonSave = findViewById(R.id.button_save);
        imageButton = findViewById(R.id.returnButton);
        contactIcon = findViewById(R.id.contact_icon);

        ArrayAdapter<CharSequence> phoneAdapter = ArrayAdapter.createFromResource(
                this, R.array.phone_types, android.R.layout.simple_spinner_item);
        phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhoneType.setAdapter(phoneAdapter);

        ArrayAdapter<CharSequence> emailAdapter = ArrayAdapter.createFromResource(
                this, R.array.email_types, android.R.layout.simple_spinner_item);
        emailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmailType.setAdapter(emailAdapter);

        contact = (Contact) getIntent().getSerializableExtra("CONTACT");

        if (contact != null) {
            editName.setText(contact.getName());
            editPhone.setText(contact.getPhoneNumber());
            editEmail.setText(contact.getEmail());
            setSpinnerSelection(spinnerPhoneType, phoneAdapter, contact.getPhoneType());
            setSpinnerSelection(spinnerEmailType, emailAdapter, contact.getEmailType());

            if (contact.getImageUrl() != null && !contact.getImageUrl().isEmpty()) {
                Glide.with(this)
                        .load(contact.getImageUrl())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .transform(new CircleCrop())
                        .into(contactIcon);
            } else {
                contactIcon.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            Glide.with(this)
                                    .load(imageUri)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .transform(new CircleCrop())
                                    .into(contactIcon);

                            if (contact != null) {
                                contact.setImageUrl(imageUri.toString());
                            }
                        }
                    }
                }
        );
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(ContactRedactActivity.this, ContactDetailsActivity.class);
            intent.putExtra("CONTACT", contact);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        contactIcon.setOnClickListener(v -> showImageEditDialog());
        length=editName.getText().length();
        int remaining = 15 - length;
        characterCountTextView.setText(remaining + " / 15");
        buttonSave.setOnClickListener(v -> saveContact());
        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int remaining = 15 - s.length();
                characterCountTextView.setText(remaining + " / 15");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void showImageEditDialog() {
        String[] options = {"Enter URL", "Select from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Profile Picture");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                showUrlInputDialog();
            } else if (which == 1) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                imagePickerLauncher.launch(intent);
            }
        });
        builder.show();
    }

    private void showUrlInputDialog() {
        EditText urlInput = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Image URL")
                .setView(urlInput)
                .setPositiveButton("Load", (dialog, which) -> {
                    String url = urlInput.getText().toString().trim();
                    if (!url.isEmpty()) {
                        Glide.with(this)
                                .load(url)
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .transform(new CircleCrop())
                                .into(contactIcon);
                        contact.setImageUrl(url);
                    } else {
                        Toast.makeText(this, "URL cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveContact() {
        if (contact != null) {
            contact.setName(editName.getText().toString());
            contact.setPhoneNumber(editPhone.getText().toString());
            contact.setEmail(editEmail.getText().toString());
            contact.setPhoneType(spinnerPhoneType.getSelectedItem().toString());
            contact.setEmailType(spinnerEmailType.getSelectedItem().toString());

            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).getId() == contact.getId()) {
                    contacts.set(i, contact);
                    break;
                }
            }

            Toast.makeText(this, "Contact saved successfully!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ContactRedactActivity.this, ContactDetailsActivity.class);
            intent.putExtra("CONTACT", contact);
            startActivity(intent);
            finish();
        }
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
}
