package com.example.mycontacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Contact> contacts;
    private Map<Character, Integer> sectionPositions;
    private OnContactClickListener listener;

    public interface OnContactClickListener {
        void onCallClick(String phoneNumber);
        void onContactClick(int contactId);
    }

    public ContactAdapter(List<Contact> contacts, OnContactClickListener listener) {
        this.listener = listener;
        this.contacts = contacts;
        sectionPositions = new TreeMap<>();
        groupContactsByInitial();
    }

    private void groupContactsByInitial() {
        char lastInitial = 0;
        for (int i = 0; i < contacts.size(); i++) {
            char initial = contacts.get(i).getName().charAt(0);
            if (initial != lastInitial) {
                sectionPositions.put(initial, i + sectionPositions.size());
                lastInitial = initial;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return sectionPositions.containsValue(position) ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
            return new ContactViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            int actualPosition = position - sectionPositions.size();
            if (actualPosition < 0 || actualPosition >= contacts.size()) return; // Prevent invalid index

            ((HeaderViewHolder) holder).headerText.setText(
                    String.valueOf(contacts.get(actualPosition).getName().charAt(0))
            );
        } else {
            int actualPosition = position - sectionPositions.size();
            if (actualPosition < 0 || actualPosition >= contacts.size()) return; // Prevent invalid index

            Contact contact = contacts.get(actualPosition);
            ContactViewHolder contactHolder = (ContactViewHolder) holder;
            contactHolder.name.setText(contact.getName());
            contactHolder.phone.setText(contact.getPhoneNumber().isEmpty() ? "No phone" : contact.getPhoneNumber());
            contactHolder.callButton.setOnClickListener(v -> listener.onCallClick(contact.getPhoneNumber()));
            holder.itemView.setOnClickListener(v -> listener.onContactClick(contact.getId()));
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size() + sectionPositions.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerText;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.header_text);
        }
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;
        Button callButton;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            phone = itemView.findViewById(R.id.contact_phone);
            callButton = itemView.findViewById(R.id.button_call);
        }
    }
}

